package exp.Aqualush.aqualush_callgraph;

import callGraph.JCallGraph;
import core.dataset.TextDataset;
import core.type.Granularity;
import group.CoChangeRegionFetcher;
import relation.CallRelationGraph;
import relation.RelationInfo;
import util.AppConfigure;
import visual.VisualRelationGraph;

/**
 * Created by niejia on 15/12/12.
 */
public class AqualushMethodCallGraph {
    public static void main(String[] args) {
        JCallGraph oldCallGraph = new JCallGraph(AppConfigure.Aqualush_origin_jarFile);
        JCallGraph newCallGraph = new JCallGraph(AppConfigure.Aqualush_last_jarFile);

        RelationInfo relationInfo = new RelationInfo(AppConfigure.Aqualush_origin_jarFile, AppConfigure.Aqualush_last_jarFile, AppConfigure.Aqualush_Changed_Artifact, Granularity.METHOD, false);
        CallRelationGraph callGraph = new CallRelationGraph(relationInfo);

        CoChangeRegionFetcher fetcher = new CoChangeRegionFetcher(AppConfigure.Aqualush_Changed_Artifact, callGraph,callGraph);
        fetcher.showChangeRegion();

        TextDataset textDataset_change = new TextDataset(AppConfigure.Aqualush_CodeChangesGroupedByClass,
                AppConfigure.Aqualush_CleanedRequirement, AppConfigure.AqualushOracle);

        String layoutPath = "data/Aqualush/relation/callGraph_method.out";
        VisualRelationGraph visualRelationGraph = new VisualRelationGraph(textDataset_change, callGraph, layoutPath, AppConfigure.Aqualush_Changed_Artifact, "Aqualush");
        visualRelationGraph.show();
    }
}
