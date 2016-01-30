package exp.iTrust.legacy;

import callGraph.JCallGraph;
import core.dataset.TextDataset;
import core.type.Granularity;
import group.CoChangeRegionFetcher;
import relation.CallRelationGraph;
import relation.RelationInfo;
import util.AppConfigure;
import visual.VisualRelationGraph;

/**
 * Created by niejia on 15/10/31.
 */
public class iTrustClassCallGraph {

    public static void main(String[] args) {

//        String changedClassPath = "data/iTrust/code_changes_grouped_by_class";

        JCallGraph oldCallGraph = new JCallGraph(AppConfigure.iTrust_v10_jarFile);
        JCallGraph newCallGraph = new JCallGraph(AppConfigure.iTrust_v11_jarFile);

        RelationInfo relationInfo = new RelationInfo(AppConfigure.iTrust_v10_jarFile, AppConfigure.iTrust_v11_jarFile, AppConfigure.iTrust_Changed_Artifact, Granularity.METHOD, false);
//        relationInfo.setPruning(0.3);
        CallRelationGraph callGraph = new CallRelationGraph(relationInfo);

            CoChangeRegionFetcher fetcher = new CoChangeRegionFetcher(AppConfigure.iTrust_Changed_Artifact, callGraph,callGraph);
            fetcher.showChangeRegion();

        TextDataset textDataset_change = new TextDataset(AppConfigure.iTrust_CodeChangesGroupedByClass,
                AppConfigure.iTrust_CleanedRequirement, AppConfigure.iTrustOracle);

        String layoutPath = "data/iTrust/relation/callGraph_class.out";
        VisualRelationGraph visualRelationGraph = new VisualRelationGraph(textDataset_change, callGraph, layoutPath, AppConfigure.iTrust_Changed_Artifact, "iTrust");
        visualRelationGraph.show();
    }
}
