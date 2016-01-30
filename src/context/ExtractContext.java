package context;

import core.type.Granularity;
import document.Artifact;
import document.ArtifactsCollection;
import exp.Aqualush.AqualushSetting;
import io.ArtifactsReader;
import relation.CallRelationGraph;
import relation.RelationInfo;
import relation.graph.CodeVertex;
import util._;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by niejia on 15/12/1.
 */
public class ExtractContext {

    private CallRelationGraph callGraph;
    private ArtifactsCollection corpus;

    private HashMap<String, List<String>> contextForVertex;
    private String outputPath;

    private String extractedCorpusPath;
    private String methodCommentCorpusPath;

    public ExtractContext(String jarFilePath, String corpusPath, String corpusWithContextPath, String projectName, String versionName, double threshold) {

        RelationInfo relationInfo = new RelationInfo(jarFilePath, Granularity.METHOD, true);
        relationInfo.setEnableExternalPackage();
        relationInfo.setPruning(threshold);
        this.callGraph = new CallRelationGraph(relationInfo);

        this.corpus = ArtifactsReader.getCollections(corpusPath + "/" + versionName, ".txt");
        this.contextForVertex = new HashMap<>();
        this.outputPath = corpusWithContextPath + "/" + versionName;
        this.extractedCorpusPath = "data/" + projectName + "/ExtractedCorpus";
        this.methodCommentCorpusPath = extractedCorpusPath + "/MethodComment/" + versionName;
        File outDir = new File(outputPath);
        if (outDir.exists()) {
            for (File f : outDir.listFiles()) {
                f.delete();
            }
        } else {
            outDir.mkdir();
        }
    }

    public void process(int level) {
        for (String target : corpus.keySet()) {
            int currentlevel = 1;
            List<String> currentFather = new ArrayList<>();
            List<String> currentChildren = new ArrayList<>();
            currentFather.add(target);
            currentChildren.add(target);

            while (currentlevel <= level) {
                List<String> tmpForFather = new ArrayList<>();
                for (String center : currentFather) {
                    List<CodeVertex> father = callGraph.getFathersByCall(center);

                    for (CodeVertex s : father) {
                        if (!tmpForFather.contains(s.getName())) {
                            tmpForFather.add(s.getName());
                        }
                    }
                }
                for (String s : tmpForFather) {
                    if (!currentFather.contains(s)) {
                        currentFather.add(s);
                    }
                }

                List<String> tmpForChild = new ArrayList<>();
                for (String center : currentChildren) {
                    List<CodeVertex> children = callGraph.getChildrenByCall(center);

                    for (CodeVertex s : children) {
                        if (!tmpForChild.contains(s.getName())) {
                            tmpForChild.add(s.getName());
                        }
                    }
                }
                for (String s : tmpForChild) {
                    if (!currentChildren.contains(s)) {
                        currentChildren.add(s);
                    }
                }
                currentlevel++;
            }

            currentChildren.remove(target);
            currentFather.remove(target);

            List<String> context = new ArrayList<>();
            for (String s : currentFather) {
                if (!context.contains(s)) {
                    context.add(s);
                }
            }

            for (String s : currentChildren) {
                if (!context.contains(s)) {
                    context.add(s);
                }
            }

//            System.out.println("method:" + target);
//            System.out.println("fathers:");
//            System.out.println(currentFather);
//            System.out.println("children:");
//            System.out.println(currentChildren);
            contextForVertex.put(target, context);
        }


        for (String target : corpus.keySet()) {
            Artifact c = corpus.get(target);
            StringBuilder sb = new StringBuilder();
            sb.append(c.text);
            sb.append("\n");
            for (String nb : contextForVertex.get(target)) {
                sb.append(nb);
                sb.append("\n");
                sb.append(getMethodDoc(nb));
                sb.append("\n");
            }

            _.writeFile(sb.toString(), outputPath + "/" + target + ".txt");
        }
    }

    public String getMethodDoc(String methodName) {
        String doc = _.readFile(methodCommentCorpusPath + "/" + methodName + ".txt");
        return doc;
    }

    public static void main(String[] args) {

        ExtractContext ec = new
                ExtractContext(AqualushSetting.Change3_JAR, AqualushSetting.MethodIdentifierCorpusPath, AqualushSetting.MethodCorpusWithContext, "Aqualush", "change3", 1);
        ec.process(2);

//        RelationInfo relationInfo = new RelationInfo(AqualushSetting.Change3_JAR, Granularity.METHOD, true);
//        relationInfo.setEnableExternalPackage();
//        relationInfo.setPruning(1.1);
//
//        CallRelationGraph callGraph = new CallRelationGraph(relationInfo);
//        for (CodeVertex v : callGraph.getNeighboursByCall("irrigation.Zone.getZoneReport")) {
//            System.out.println(v.getName());
//        }
//        TextDataset textDataset_change = new TextDataset(AqualushSetting.Aqualush_Change4_For_Every_Single_Method,
//                AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange4);

//        String layoutPath = "data/Aqualush/relation/change4CallGraph.out";
//        VisualRelationGraph visualRelationGraph = new VisualRelationGraph(textDataset_change, callGraph, layoutPath, AqualushSetting.MethodChanges4, "Aqualush");
//        visualRelationGraph.show();
    }
}
