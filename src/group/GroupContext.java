package group;

import core.type.Granularity;
import relation.CallRelationGraph;
import relation.RelationInfo;
import relation.graph.CodeVertex;
import util.AppConfigure;

import java.util.HashSet;
import java.util.List;

/**
 * Created by niejia on 15/11/9.
 */
public class GroupContext {

    private CallRelationGraph callGraph;

    public GroupContext(List<HashSet<String>> changeRegion, CallRelationGraph callGraph) {
        this.callGraph = callGraph;

        for (HashSet<String> region : changeRegion) {
            System.out.println(extractContext(region, 1));
            break;
        }
    }

    private HashSet<String> extractContext(HashSet<String> region, int level) {
        HashSet<String> tmp = new HashSet<>(region);
        while (level > 0) {
            HashSet<String> nbs = new HashSet<>();
            for (String target : tmp) {
                List<CodeVertex> neighbours = callGraph.getNeighbours(target);
                for (CodeVertex vertex : neighbours) {
                    nbs.add(vertex.getName());
                }
            }
            level--;
            tmp.addAll(nbs);
        }

        HashSet<String> nbs_around = new HashSet<>();
        for (String s : tmp) {
            if (!region.contains(s)) {
                nbs_around.add(s);
            }
        }

        return nbs_around;
    }

    public void extract() {

    }

    public static void main(String[] args) {
        RelationInfo relationInfo = new RelationInfo(AppConfigure.Aqualush_version6_jarFile, AppConfigure.Aqualush_version2_jarFile,
                AppConfigure.Aqualush_Changed_Artifact_Change2, Granularity.METHOD,false);
        CallRelationGraph callGraph = new CallRelationGraph(relationInfo);

        CoChangeRegionFetcher fetcher = new CoChangeRegionFetcher(AppConfigure.Aqualush_Changed_Artifact_Change2, callGraph,callGraph);

        RelationInfo completedRelationInfo = new RelationInfo(AppConfigure.Aqualush_version6_jarFile, AppConfigure.Aqualush_version2_jarFile,
                AppConfigure.Aqualush_Changed_Artifact_Change2, Granularity.METHOD, true);
        completedRelationInfo.setEnableExternalPackage();

        CallRelationGraph completedGraph = new CallRelationGraph(completedRelationInfo);

        GroupContext context = new GroupContext(fetcher.getChangeRegion(), completedGraph );
        context.extract();
    }

}
