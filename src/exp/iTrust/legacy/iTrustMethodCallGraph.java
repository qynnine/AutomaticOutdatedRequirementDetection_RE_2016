package exp.iTrust.legacy;

import callGraph.JCallGraph;
import core.type.Granularity;
import relation.CallRelationGraph;
import relation.RelationInfo;
import relation.graph.CodeVertex;
import util.AppConfigure;

/**
 * Created by niejia on 15/11/17.
 */
public class iTrustMethodCallGraph {
    public static void main(String[] args) {

        JCallGraph oldCallGraph = new JCallGraph(AppConfigure.iTrust_v10_jarFile);
        JCallGraph newCallGraph = new JCallGraph(AppConfigure.iTrust_v11_jarFile);

        RelationInfo relationInfo = new RelationInfo(AppConfigure.iTrust_v10_jarFile, AppConfigure.iTrust_v11_jarFile, AppConfigure.iTrust_Changed_Artifact, Granularity.METHOD, true);

        CallRelationGraph callGraph = new CallRelationGraph(relationInfo);

        for (CodeVertex v : callGraph.getVertexes().values()) {
            System.out.println(v.getName());
        }

        System.out.println(callGraph.getVertexes().size());
    }
}
