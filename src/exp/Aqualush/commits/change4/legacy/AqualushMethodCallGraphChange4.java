package exp.Aqualush.commits.change4.legacy;

import core.dataset.TextDataset;
import core.type.Granularity;
import exp.Aqualush.AqualushSetting;
import relation.CallRelationGraph;
import relation.RelationInfo;
import visual.VisualRelationGraph;

/**
 * Created by niejia on 15/12/13.
 */
public class AqualushMethodCallGraphChange4 {
    public static void main(String[] args) {


        TextDataset textDataset_change = new TextDataset(AqualushSetting.Aqualush_Change4_For_Every_Single_Method
                ,AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange4);

        RelationInfo relationInfo = new RelationInfo(AqualushSetting.Change4_JAR,
                Granularity.METHOD, true);

        relationInfo.setEnableExternalPackage();
        relationInfo.setPruning(-1);
//        relationInfo.setPruning(0.4);

        CallRelationGraph callGraph = new CallRelationGraph(relationInfo);

        String layoutPath = "data/Aqualush/relation/change4CallGraph.out";
        VisualRelationGraph visualRelationGraph = new VisualRelationGraph(textDataset_change, callGraph, layoutPath, AqualushSetting.MethodChanges4, "Aqualush");
        visualRelationGraph.show();
    }
}
