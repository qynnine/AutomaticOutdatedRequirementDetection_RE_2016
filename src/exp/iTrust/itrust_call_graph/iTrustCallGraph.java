package exp.iTrust.itrust_call_graph;

import core.dataset.TextDataset;
import core.type.Granularity;
import relation.CallRelationGraph;
import relation.RelationInfo;
import util.AppConfigure;
import visual.VisualRelationGraph;

/**
 * Created by niejia on 15/12/26.
 */
public class iTrustCallGraph {
    public static void main(String[] args) {

        RelationInfo relationInfo = new RelationInfo(AppConfigure.iTrust_v10_jarFile, Granularity.METHOD, true);

        CallRelationGraph callGraph = new CallRelationGraph(relationInfo);

        TextDataset textDataset_change = new TextDataset(AppConfigure.iTrust_CodeChangesGroupedByClass,
                AppConfigure.iTrust_CleanedRequirement, AppConfigure.iTrustOracle);

        String layoutPath = "data/iTrust/relation/callGraph_class.out";
        VisualRelationGraph visualRelationGraph = new VisualRelationGraph(textDataset_change, callGraph, layoutPath, AppConfigure.iTrust_Changed_Artifact, "iTrust");
        visualRelationGraph.show();

//        for (CodeVertex v : callGraph.getVertexes().values()) {
//            System.out.println(v.getName());
//        }
//
//        System.out.println(callGraph.getVertexes().size());
    }
}
