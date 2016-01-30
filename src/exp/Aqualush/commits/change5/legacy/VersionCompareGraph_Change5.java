package exp.Aqualush.commits.change5.legacy;

import core.dataset.TextDataset;
import core.type.Granularity;
import exp.Aqualush.AqualushSetting;
import relation.CallRelationGraph;
import relation.RelationInfo;
import visual.VersionCompareVisualRelationGraph;

/**
 * Created by niejia on 15/12/27.
 */
public class VersionCompareGraph_Change5 {
    public static void main(String[] args) {
        TextDataset textDataset_change = new TextDataset(AqualushSetting.Aqualush_Change5_For_Every_Single_Method
                ,AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange5);

        RelationInfo relationInfo = new RelationInfo(AqualushSetting.Change4_JAR, AqualushSetting.Change5_JAR, AqualushSetting.MethodChanges5,
                Granularity.METHOD, true);

        relationInfo.setPruning(-1);
//        relationInfo.setPruning(0.4);

        CallRelationGraph callGraph = new CallRelationGraph(relationInfo);

        String layoutPath = "data/Aqualush/relation/versionCompareGraph_change5.out";
        VersionCompareVisualRelationGraph visualRelationGraph = new VersionCompareVisualRelationGraph(textDataset_change, callGraph, layoutPath, AqualushSetting.MethodChanges5, "Aqualush");
        visualRelationGraph.show();
    }
}
