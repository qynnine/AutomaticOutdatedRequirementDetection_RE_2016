package visual;

import core.algo.None_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import document.LinksList;
import document.SimilarityMatrix;
import document.SingleLink;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.PersistentLayout;
import edu.uci.ics.jung.visualization.layout.PersistentLayoutImpl;
import edu.uci.ics.jung.visualization.renderers.DefaultEdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import io.ChangedArtifacts;
import javafx.util.Pair;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import relation.RelationGraph;
import relation.graph.CallEdge;
import relation.graph.CodeEdge;
import relation.graph.CodeVertex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by niejia on 15/3/1.
 */
public class VisualRelationGraph {

    private final HashSet<String> modifiedArtifactList;
    private final HashSet<String> removedArtifactList;
    private final HashSet<String> addedArtifactList;
    private RelationGraph relationGraph;

    private Graph<Integer, Integer> g;
    private final Factory<Integer> edgeFactory;
    private final Map<Integer, Number> edgeRelationWeightsMap;

    private final Map<Integer, String> vertexNameMap;
    private final Map<String, Integer> vertexIndexMap;

    private TextDataset textDataset;
    private SimilarityMatrix matrix;
    private String LAYOUT_FILE;
    private LinksList qualityScoreLinksList;
    private LinksList highestScoreLinksList;
    private String highestScoreTarget;
    private String secondHighestScoreTarget;

    private String firstValidHighestScoreTarget;

    //    private LinksList highestScoreLinksList;
    private PersistentLayout<Integer, Integer> persistentLayout;
    private VisualizationViewer<Integer, Integer> vv;

    private String currentUC = "";
    private List<String> ucRelatedCodes;
    private List<String> ucList;

    private Map<Integer, Pair<Integer, Integer>> callEdges;
    private Result result;
    private RelationGraph callGraph;
    private HashSet<String> changedTarget;

    public VisualRelationGraph(TextDataset textDataset, RelationGraph relationGraph, String layoutPath, String changedTarget, String projectName) {
        this.relationGraph = relationGraph;
        this.textDataset = textDataset;
        this.result = IR.compute(textDataset, IRModelConst.VSM, new None_CSTI(), projectName);
        this.qualityScoreLinksList = result.getMatrix().getQualityLinks();
        this.highestScoreLinksList = result.getMatrix().getHighestLinks();
        vertexNameMap = new HashMap<>();
        vertexIndexMap = new HashMap<>();
        edgeRelationWeightsMap = new HashMap<>();

        ucRelatedCodes = new ArrayList<>();
        ucList = new ArrayList<>();

        callEdges = new LinkedHashMap<>();
        callGraph = relationGraph;

        ChangedArtifacts parser = new ChangedArtifacts();
        parser.parse(changedTarget);
        this.changedTarget = parser.getWholeChangedArtifactList();
        this.addedArtifactList = parser.getAddedArtifactList();
        this.removedArtifactList = parser.getRemovedArtifactList();
        this.modifiedArtifactList = parser.getModifiedArtifactList();

        this.LAYOUT_FILE = layoutPath;

        edgeFactory = new Factory<Integer>() {
            int i = 1;

            @Override
            public Integer create() {
                return i++;
            }
        };
    }

    private void graphConvert() {
        g = new DirectedSparseGraph();

        Map<Integer, CodeVertex> vertexMap = relationGraph.getVertexes();

        for (Integer id : vertexMap.keySet()) {
            g.addVertex(id);
            vertexNameMap.put(id, vertexMap.get(id).getName());
            vertexIndexMap.put(vertexMap.get(id).getName(), id);
        }

        for (CodeEdge codeEdge : relationGraph.getCallEdges()) {
            Integer v1 = relationGraph.getVertexIdByName(codeEdge.getSource().getName());
            Integer v2 = relationGraph.getVertexIdByName(codeEdge.getTarget().getName());

            Number weight = ((CallEdge) codeEdge).getCallRelationSize();

            Integer edgeId = edgeFactory.create();
            g.addEdge(edgeId, v1, v2);
            callEdges.put(edgeId, new Pair<Integer, Integer>(v1, v2));
            if (weight == null) weight = 0.0;
            edgeRelationWeightsMap.put(edgeId, weight);
        }
    }

    public void show() {
        graphConvert();

        persistentLayout = new PersistentLayoutImpl<Integer, Integer>(new FRLayout<Integer, Integer>(g));
        vv = new VisualizationViewer<Integer, Integer>(persistentLayout);

        matrix = result.getMatrix();

        vv.getRenderContext().setVertexLabelTransformer(new Transformer<Integer, String>() {
            public String transform(Integer v) {
                String similarity = "";
                String code = vertexNameMap.get(v);

                if (!currentUC.equals("")) {
                    similarity = String.valueOf(matrix.getScoreForLink(currentUC, code));
                }
                return (vertexNameMap.get(v)) + "\n" + similarity;
            }
        });

        vv.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.cyan));
        vv.getRenderContext().setEdgeLabelRenderer(new DefaultEdgeLabelRenderer(Color.cyan));

        vv.getRenderContext().setEdgeLabelTransformer(new Transformer<Integer, String>() {
            public String transform(Integer eg) {
                return edgeRelationWeightsMap.get(eg).toString();
            }
        });

        vv.getRenderContext().setVertexIconTransformer(new Transformer<Integer, Icon>() {

            public Icon transform(final Integer cv) {
                return new Icon() {

                    public int getIconHeight() {
                        return 20;
                    }

                    public int getIconWidth() {
                        return 20;
                    }

                    public void paintIcon(Component c, Graphics g,
                                          int x, int y) {
                        if (vv.getPickedVertexState().isPicked(cv)) {
                            g.setColor(Color.yellow);
                        } else {
                            String v = vertexNameMap.get(cv);
                            // Gap show format
                            if (addedArtifactList.contains(v)) {
                                g.setColor(Color.RED);
                            } else if (removedArtifactList.contains(v)) {
                                g.setColor(Color.BLUE);
                            } else if (modifiedArtifactList.contains(v)) {
                                g.setColor(Color.CYAN);
                            } else {
                                g.setColor(Color.BLACK);
                            }
                        }

                        g.fillOval(x, y, 30, 30);
                        if (vv.getPickedVertexState().isPicked(cv)) {
                            g.setColor(Color.black);
                        } else {
                            g.setColor(Color.white);
                        }
                        String similarity = "";
                        String code = vertexNameMap.get(cv);
//                        System.out.println(" code = " + code );
//                        System.out.println(" ucRelatedCodes = " + ucRelatedCodes );
                        if (!currentUC.equals("") && ucRelatedCodes.contains(code)) {
                            similarity = String.valueOf(matrix.getScoreForLink(currentUC, code));
//                            System.out.println(currentUC+ " "+code+" = " + similarity );
                        }
                        g.drawString("" + cv, x + 6, y + 15);
                    }
                };
            }
        });

        vv.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<Integer>(vv.getPickedVertexState(), Color.white, Color.yellow));
//        vv.getRenderContext().setVertexFillPaintTransformer(new MyVertexFillPaintFunction<String>());
        vv.getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<Integer>(vv.getPickedEdgeState(), Color.black, Color.lightGray));

        vv.setBackground(Color.white);

        vv.setVertexToolTipTransformer(new ToStringLabeller<Integer>());

        final JFrame frame = new JFrame();
        Container content = frame.getContentPane();
        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        content.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final ModalGraphMouse gm = new DefaultModalGraphMouse<Integer, Integer>();
        vv.setGraphMouse(gm);

        final JComboBox ucBox = new JComboBox();
        for (String uc : textDataset.getSourceCollection().keySet()) {
            ucBox.addItem(uc);
            ucList.add(uc);
        }

        ucBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox type = (JComboBox) e.getSource();
                String uc = (String) type.getSelectedItem();
                currentUC = uc;
                ucRelatedCodes = new ArrayList<String>();

                for (SingleLink link : highestScoreLinksList) {
                    if (link.getSourceArtifactId().equals(currentUC)) {
                        highestScoreTarget = link.getTargetArtifactId();
                        break;
                    }
                }
                vv.repaint();
            }
        });

        final JButton persist = new JButton("Save Layout");
        persist.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    persistentLayout.persist(LAYOUT_FILE);
                } catch (IOException e1) {
                    System.err.println("got " + e1);
                }
            }
        });

        JButton restore = new JButton("Restore Layout");
        restore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    persistentLayout.restore(LAYOUT_FILE);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        JPanel controls = new JPanel();
        controls.add(persist);
        controls.add(restore);
        controls.add(ucBox);
        controls.add(((DefaultModalGraphMouse<Integer, Integer>) gm).getModeComboBox());
        content.add(controls, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);

    }

//    public static void main(String[] args) throws IOException {
//            TextDataset textDataset_change = new TextDataset(AppConfigure.iTrust_CodeChangesGroupedByClass,
//                    AppConfigure.iTrust_CleanedRequirement, AppConfigure.iTrustOracle);
//
//            JCallGraph oldCallGraph = new JCallGraph(AppConfigure.iTrust_v10_jarFile);
//            JCallGraph newCallGraph = new JCallGraph(AppConfigure.iTrust_v11_jarFile);
//
//            RelationInfo relationInfo = new RelationInfo(AppConfigure.iTrust_v10_jarFile, AppConfigure.iTrust_v11_jarFile);
//
//            CallRelationGraph callGraph = new CallRelationGraph(relationInfo);
//
//            String layoutPath = "data/iTrust/relation/PersistentLayoutDemo.out";
//            VisualRelationGraph visualRelationGraph = new VisualRelationGraph(textDataset_change, callGraph, layoutPath);
//            visualRelationGraph.show();
//
//    }
}
