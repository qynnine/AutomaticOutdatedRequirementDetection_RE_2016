package exp.Aqualush.aqualush_callgraph;

import core.type.Granularity;
import relation.CallRelationGraph;
import relation.RelationInfo;
import util.AppConfigure;

/**
 * Created by niejia on 15/12/26.
 */
public class AqualushMethodCallGraphOrigin {
    public static void main(String[] args) {
        RelationInfo relationInfo = new RelationInfo(AppConfigure.Aqualush_last_jarFile, Granularity.METHOD, true);
        relationInfo.setEnableExternalPackage();
        CallRelationGraph callGraph = new CallRelationGraph(relationInfo);

//        TextDataset textDataset_change = new TextDataset(AppConfigure.Aqualush_CodeChangesGroupedByClass,
//                AppConfigure.Aqualush_CleanedRequirement, AppConfigure.AqualushOracle);
//
//        String layoutPath = "data/Aqualush/relation/callGraph_method.out";
//        PureVisualRelationGraph visualRelationGraph = new PureVisualRelationGraph(callGraph, layoutPath, AppConfigure.Aqualush_Changed_Artifact, "Aqualush");
//        visualRelationGraph.show();
    }
}
