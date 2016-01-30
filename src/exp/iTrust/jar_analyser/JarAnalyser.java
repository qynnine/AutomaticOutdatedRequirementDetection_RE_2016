package exp.iTrust.jar_analyser;

import callGraph.JCallGraph;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by niejia on 15/12/27.
 */
public class JarAnalyser {
    public static void main(String[] args) {
//        JCallGraph jCallGraph = new JCallGraph(AppConfigure.iTrust_v10_jarFile);


        JCallGraph oldCallGraph = new JCallGraph("data/jar/original.jar");
        Hashtable<String, Vector<String>> authorGraphMap = JCallGraph.callGraphMap;

//        JCallGraph newCallGraph = new JCallGraph("data/jar/iTrust-v11_mine.jar");
//        Hashtable<String, Vector<String>> mineGraphMap = JCallGraph.callGraphMap;

        System.out.println("Say hi");
    }
}
