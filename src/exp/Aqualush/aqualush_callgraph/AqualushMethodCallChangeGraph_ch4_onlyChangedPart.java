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
public class AqualushMethodCallChangeGraph_ch4_onlyChangedPart {
    public static void main(String[] args) {
        RelationInfo relationInfo = new RelationInfo(AqualushSetting.Change3_JAR, AqualushSetting.Change4_JAR, AqualushSetting.MethodChanges4, Granularity.METHOD, false);
        relationInfo.setPruning(-1);

        CallRelationGraph callGraph = new CallRelationGraph(relationInfo);

        TextDataset textDataset_change = new TextDataset(AqualushSetting.Aqualush_Change4_GroupedByJSEP,
                AppConfigure.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange4);

        String layoutPath = "data/Aqualush/relation/changedCallGraph_method_ch4_pruned.out";
        VisualRelationGraph visualRelationGraph = new VisualRelationGraph(textDataset_change, callGraph, layoutPath, AqualushSetting.MethodChanges4, "Change4");
        visualRelationGraph.show();
    }
}
