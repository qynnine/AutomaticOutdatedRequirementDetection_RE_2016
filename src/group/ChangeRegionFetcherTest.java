package group;

import callGraph.JCallGraph;
import core.type.Granularity;
import relation.CallRelationGraph;
import relation.RelationInfo;
import util.AppConfigure;

public class ChangeRegionFetcherTest {

    public static void main(String[] args) {

        String changedClassPath = "data/iTrust/code_changes_grouped_by_class";

        JCallGraph oldCallGraph = new JCallGraph(AppConfigure.iTrust_v10_jarFile);
        JCallGraph newCallGraph = new JCallGraph(AppConfigure.iTrust_v11_jarFile);

        RelationInfo relationInfo = new RelationInfo(AppConfigure.iTrust_v10_jarFile, AppConfigure.iTrust_v11_jarFile, changedClassPath, Granularity.CLASS,false);
        relationInfo.setPruning(0.2);
        CallRelationGraph callGraph = new CallRelationGraph(relationInfo);

        CoChangeRegionFetcher fetcher = new CoChangeRegionFetcher(changedClassPath, callGraph,callGraph);
        fetcher.showChangeRegion();
    }
}