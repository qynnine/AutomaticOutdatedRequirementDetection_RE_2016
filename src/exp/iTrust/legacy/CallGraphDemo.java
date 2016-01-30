package exp.iTrust.legacy;

import callGraph.JCallGraph;
import util.AppConfigure;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by niejia on 15/11/19.
 */
public class CallGraphDemo {
    public static void main(String[] args) {
        JCallGraph oldCallGraph = new JCallGraph(AppConfigure.iTrust_sample_jarFile);

        Hashtable<String,Vector<String>> table = JCallGraph.isCalledByGraphMap;
        for (Vector<String> v : table.values()) {
            for (String s : v) {
                System.out.println(s);
            }
        }
    }
}
