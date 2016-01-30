package exp.Aqualush.legacy;

import callGraph.JCallGraph;
import util.AppConfigure;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by niejia on 15/11/21.
 */
public class CallGraphDemo {
    public static void main(String[] args) {
        JCallGraph oldCallGraph = new JCallGraph(AppConfigure.Aqualush_last_jarFile);

        Hashtable<String,Vector<String>> table = JCallGraph.isCalledByGraphMap;
        for (String callee : table.keySet()) {
            if (callee.equals("startup.Configurer.zoneSpec")) {
                System.out.println(table.get(callee));
            }
        }
    }
}
