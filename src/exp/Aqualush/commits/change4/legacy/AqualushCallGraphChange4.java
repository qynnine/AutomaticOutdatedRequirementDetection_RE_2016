package exp.Aqualush.commits.change4.legacy;

import core.dataset.TextDataset;
import core.type.Granularity;
import exp.Aqualush.AqualushSetting;
import relation.CallRelationGraph;
import relation.RelationInfo;
import visual.VisualRelationGraph;

/**
 * Created by niejia on 15/12/1.
 */
public class AqualushCallGraphChange4 {

    public static void main(String[] args) {



        RelationInfo relationInfo = new RelationInfo(AqualushSetting.Change3_JAR,
                Granularity.METHOD, true);
        relationInfo.setEnableExternalPackage();
        relationInfo.setPruning(0.5);

        CallRelationGraph callGraph = new CallRelationGraph(relationInfo);

        TextDataset textDataset_change = new TextDataset(AqualushSetting.Aqualush_Change4_For_Every_Single_Method,
                AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange4);

        String layoutPath = "data/Aqualush/relation/change4CallGraph.out";
        VisualRelationGraph visualRelationGraph = new VisualRelationGraph(textDataset_change, callGraph, layoutPath, AqualushSetting.MethodChanges4, "Aqualush");
            visualRelationGraph.show();
    }
}
