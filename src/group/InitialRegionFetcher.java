package group;

import io.ChangedArtifacts;
import relation.CallRelationGraph;
import relation.graph.CodeVertex;

import java.util.*;

/**
 * Created by niejia on 16/1/3.
 */
public class InitialRegionFetcher {
    private ChangedArtifacts changedArtifacts;

    private CallRelationGraph newCallGraph;
    private CallRelationGraph oldCallGraph;
    private CallRelationGraph changedPartCallGraph;

    private Map<String, HashSet<String>> regionForArtifactsList;

    public InitialRegionFetcher(ChangedArtifacts changedArtifacts, CallRelationGraph newCallGraph, CallRelationGraph oldCallGraph) {
        this.changedArtifacts = changedArtifacts;

        this.newCallGraph = newCallGraph;
        this.oldCallGraph = oldCallGraph;
        this.regionForArtifactsList = new LinkedHashMap<>();

        findInitialRegion();
    }

    private void findInitialRegion() {
        findInitialRegionForEachVertex(changedArtifacts.getAddedArtifactList(), newCallGraph);
        findInitialRegionForEachVertex(changedArtifacts.getRemovedArtifactList(), oldCallGraph);
    }

    private void findInitialRegionForEachVertex(Set<String> vertexes, CallRelationGraph callRelationGraph) {
        for (String vertexName : vertexes) {
            List<CodeVertex> subGraphVertexes = new ArrayList<>();
            callRelationGraph.searhNeighbourConnectedGraphByCall(vertexName, subGraphVertexes);

            HashSet<String> region = new HashSet<>();
            region.add(vertexName);
            for (CodeVertex v : subGraphVertexes) {
                region.add(v.getName());
            }
            regionForArtifactsList.put(vertexName, region);
        }
    }

    public Map<String, HashSet<String>> getChangeRegion() {
      return regionForArtifactsList;
    }
}
