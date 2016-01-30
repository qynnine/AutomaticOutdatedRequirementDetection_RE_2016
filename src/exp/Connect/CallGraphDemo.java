package exp.Connect;

import core.type.Granularity;
import relation.CallRelationGraph;
import relation.RelationInfo;

/**
 * Created by niejia on 16/1/23.
 */
public class CallGraphDemo {
    public static void main(String[] args) {

        RelationInfo relationInfo = new RelationInfo("data/Connect/jar/CONNECT-3.3.jar", Granularity.METHOD, false);

        CallRelationGraph callGraph = new CallRelationGraph(relationInfo);

        System.out.println("Hi");
//
//        TextDataset textDataset_change = new TextDataset(AppConfigure.iTrust_CodeChangesGroupedByClass,
//                AppConfigure.iTrust_CleanedRequirement, AppConfigure.iTrustOracle);
//
//        String layoutPath = "data/iTrust/relation/callGraph_class.out";
//        VisualRelationGraph visualRelationGraph = new VisualRelationGraph(textDataset_change, callGraph, layoutPath, AppConfigure.iTrust_Changed_Artifact, "iTrust");
//        visualRelationGraph.show();

//        for (CodeVertex v : callGraph.getVertexes().values()) {
//            System.out.println(v.getName());
//        }
//
//        System.out.println(callGraph.getVertexes().size());
    }
}
