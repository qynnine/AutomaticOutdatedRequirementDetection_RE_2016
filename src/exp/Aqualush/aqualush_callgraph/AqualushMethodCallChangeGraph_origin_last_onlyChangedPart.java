package exp.Aqualush.aqualush_callgraph;

import callGraph.JCallGraph;
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
public class AqualushMethodCallChangeGraph_origin_last_onlyChangedPart {
    public static void main(String[] args) {
        JCallGraph oldCallGraph = new JCallGraph(AppConfigure.Aqualush_origin_jarFile);
        JCallGraph newCallGraph = new JCallGraph(AppConfigure.Aqualush_last_jarFile);

//        RelationInfo relationInfo = new RelationInfo(false, AppConfigure.Aqualush_origin_jarFile, AppConfigure.Aqualush_last_jarFile, AqualushSetting.MethodChanges99, Granularity.METHOD);
        RelationInfo relationInfo = new RelationInfo(AppConfigure.Aqualush_origin_jarFile, AppConfigure.Aqualush_last_jarFile, AqualushSetting.MethodChanges99, Granularity.METHOD, false);
        relationInfo.setPruning(-1);

        CallRelationGraph callGraph = new CallRelationGraph(relationInfo);

//        CoChangeRegionFetcher fetcher = new CoChangeRegionFetcher(AqualushSetting.MethodChanges99, callGraph,callGraph);
//        fetcher.showChangeRegion();

        TextDataset textDataset_change = new TextDataset(AqualushSetting.Aqualush_Change99_GroupedByJSEP,
                AppConfigure.Aqualush_CleanedRequirement, AppConfigure.AqualushOracle);

        String layoutPath = "data/Aqualush/relation/changedCallGraph_method_origin_last_pruned.out";
        VisualRelationGraph visualRelationGraph = new VisualRelationGraph(textDataset_change, callGraph, layoutPath, AqualushSetting.MethodChanges99, "Aqualush");
        visualRelationGraph.show();
    }
}
