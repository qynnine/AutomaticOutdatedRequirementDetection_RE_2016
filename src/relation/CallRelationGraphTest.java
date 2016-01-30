package relation;

import callGraph.JCallGraph;
import core.type.Granularity;
import exp.Aqualush.AqualushSetting;
import org.junit.Test;
import relation.graph.CodeVertex;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by niejia on 15/11/4.
 */
public class CallRelationGraphTest {

//    public static void main(String[] args) {
//        String changedClassPath = "data/iTrust/code_changes_grouped_by_class";
//        HashSet<String> changedTarget = new HashSet<>();
//        File file = new File(changedClassPath);
//        for (File f : file.listFiles()) {
//            changedTarget.add(f.getName().split(".java")[0]);
//        }
//
//        JCallGraph oldCallGraph = new JCallGraph(AppConfigure.iTrust_v10_jarFile);
//        JCallGraph newCallGraph = new JCallGraph(AppConfigure.iTrust_v11_jarFile);
//
//        RelationInfo relationInfo = new RelationInfo(AppConfigure.iTrust_v10_jarFile, AppConfigure.iTrust_v11_jarFile,changedClassPath, Granularity.CLASS, false);
//        relationInfo.setPruning(0.15);
//        CallRelationGraph callGraph = new CallRelationGraph(relationInfo);
//
//    }

    @Test
    public void testCallGraphNum() throws Exception {
        String newVersionJar = AqualushSetting.Change4_JAR;
        RelationInfo relationInfo_new = new RelationInfo(newVersionJar, Granularity.CLASS, true);
        JCallGraph callGraph = new JCallGraph(newVersionJar);
        Hashtable<String, Vector<String>> callGraphMap = JCallGraph.callGraphMap;

        System.out.println(callGraphMap.get("device.KeyPress"));
        relationInfo_new.setEnableExternalPackage();
        relationInfo_new.setPruning(-1);
        CallRelationGraph newVersionCallGraph = new CallRelationGraph(relationInfo_new);
        for (CodeVertex cv : newVersionCallGraph.getVertexes().values()) {
            String name = cv.getName();
            String tokens[] = name.split("\\.");
            if (Character.isUpperCase(tokens[tokens.length - 1].charAt(0))) {
                System.out.println(name);
            }
        }
        System.out.println(newVersionCallGraph.getFathersByCall("device.KeyPress"));
    }
}
