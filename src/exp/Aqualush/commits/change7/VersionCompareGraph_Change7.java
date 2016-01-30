package exp.Aqualush.commits.change7;

import core.dataset.TextDataset;
import core.type.Granularity;
import exp.Aqualush.AqualushSetting;
import relation.CallRelationGraph;
import relation.RelationInfo;
import visual.VersionCompareVisualRelationGraph;

/**
 * Created by niejia on 15/12/27.
 */
public class VersionCompareGraph_Change7 {

    public static void main(String[] args) {
        TextDataset textDataset_change = new TextDataset(AqualushSetting.Aqualush_Change7_For_Every_Single_Method
                ,AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange7);

        RelationInfo relationInfo = new RelationInfo(AqualushSetting.Change6_JAR, AqualushSetting.Change7_JAR, AqualushSetting.MethodChanges7,
                Granularity.METHOD, true);

        relationInfo.setPruning(-1);
//        relationInfo.setPruning(0.4);

        CallRelationGraph callGraph = new CallRelationGraph(relationInfo);

        String layoutPath = "data/Aqualush/relation/versionCompareGraph_change7.out";
        VersionCompareVisualRelationGraph visualRelationGraph = new VersionCompareVisualRelationGraph(textDataset_change, callGraph, layoutPath, AqualushSetting.MethodChanges7, "Aqualush");
        visualRelationGraph.show();
    }
}
