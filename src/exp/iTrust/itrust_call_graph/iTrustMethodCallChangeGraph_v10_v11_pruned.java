package exp.iTrust.itrust_call_graph;

import core.dataset.TextDataset;
import core.type.Granularity;
import exp.iTrust.ITrustSetting;
import relation.CallRelationGraph;
import relation.RelationInfo;
import visual.VisualRelationGraph;

/**
 * Created by niejia on 16/1/9.
 */
public class iTrustMethodCallChangeGraph_v10_v11_pruned {
    public static void main(String[] args) {

        RelationInfo relationInfo = new RelationInfo(ITrustSetting.v10_JAR, ITrustSetting.v11_JAR, ITrustSetting.MethodChangesV10_V11, Granularity.METHOD, false);
//        relationInfo.setPruning(2);
        CallRelationGraph callGraph = new CallRelationGraph(relationInfo);


        TextDataset textDataset_change = new TextDataset(ITrustSetting.iTrust_ChangeV11_GroupedByJSEP,
                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChangeV11);

        String layoutPath = "data/iTrust/relation/changedCallGraph_method_v10_v11_pruned.out";
        VisualRelationGraph visualRelationGraph = new VisualRelationGraph(textDataset_change, callGraph, layoutPath, ITrustSetting.MethodChangesV10_V11, "iTrust");
        visualRelationGraph.show();
    }
}
