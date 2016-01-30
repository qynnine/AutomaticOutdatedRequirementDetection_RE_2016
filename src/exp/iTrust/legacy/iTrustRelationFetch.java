package exp.iTrust.legacy;

import callGraph.JCallGraph;
import core.type.Granularity;
import relation.CallRelationGraph;
import relation.RelationInfo;
import relation.graph.CodeVertex;
import util.AppConfigure;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by niejia on 15/11/1.
 */
public class iTrustRelationFetch {

    public static void main(String[] args) {

        String changedClassPath = "data/iTrust/code_changes_grouped_by_class";
        HashSet<String> changedTarget = new HashSet<>();
        File file = new File(changedClassPath);
        for (File f : file.listFiles()) {
            changedTarget.add(f.getName().split(".java")[0]);
        }

        JCallGraph oldCallGraph = new JCallGraph(AppConfigure.iTrust_v10_jarFile);
        JCallGraph newCallGraph = new JCallGraph(AppConfigure.iTrust_v11_jarFile);

        RelationInfo relationInfo = new RelationInfo(AppConfigure.iTrust_v10_jarFile, AppConfigure.iTrust_v11_jarFile,changedClassPath, Granularity.CLASS,false);
        relationInfo.setPruning(0.15);
        CallRelationGraph callGraph = new CallRelationGraph(relationInfo);

        List<CodeVertex> nb = new ArrayList<>();
        callGraph.searhNeighbourConnectedGraphByCall("edu.ncsu.csc.itrust.action.EditApptAction", nb);
        for (CodeVertex cv : nb) {
            System.out.println(cv.getName());
        }


    }
}
