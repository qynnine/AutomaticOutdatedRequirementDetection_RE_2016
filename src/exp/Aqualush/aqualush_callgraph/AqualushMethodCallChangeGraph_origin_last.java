package exp.Aqualush.aqualush_callgraph;

import core.dataset.TextDataset;
import core.type.Granularity;
import exp.Aqualush.AqualushSetting;
import relation.CallRelationGraph;
import relation.RelationInfo;
import util.AppConfigure;
import visual.VisualRelationGraph;

/**
 * Created by niejia on 16/1/9.
 */

// Use different color to represent added, removed, modified method.

public class AqualushMethodCallChangeGraph_origin_last {

    public static void main(String[] args) {

        RelationInfo relationInfo = new RelationInfo(AppConfigure.Aqualush_origin_jarFile, AppConfigure.Aqualush_last_jarFile, AqualushSetting.MethodChanges99, Granularity.METHOD, true);
        relationInfo.setPruning(0.3);
        CallRelationGraph callGraph = new CallRelationGraph(relationInfo);

//        CoChangeRegionFetcher fetcher = new CoChangeRegionFetcher(AqualushSetting.MethodChanges99, callGraph,callGraph);
//        fetcher.showChangeRegion();

        TextDataset textDataset_change = new TextDataset(AppConfigure.Aqualush_CodeChangesGroupedByClass,
                AppConfigure.Aqualush_CleanedRequirement, AppConfigure.AqualushOracle);

        String layoutPath = "data/Aqualush/relation/changedCallGraph_method_origin_last_complete_threshold_0.3.out";
        VisualRelationGraph visualRelationGraph = new VisualRelationGraph(textDataset_change, callGraph, layoutPath, AqualushSetting.MethodChanges99, "Aqualush");
        visualRelationGraph.show();
    }
}
