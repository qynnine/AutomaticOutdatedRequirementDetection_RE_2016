package exp.Connect.callgraph;

import rukia.parser.ProjectCallRelationsStaticAnalyser;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by niejia on 16/1/30.
 */
public class Main {

    public static void main(String[] args) {

        String commitVersion = "3ee520200c79e8ba3629f0c0acfc1e2a3307bf2f";
        String projectVersion = "data/Connect/code/" + commitVersion;

        ProjectCallRelationsStaticAnalyser analyser = new ProjectCallRelationsStaticAnalyser(projectVersion);

        Hashtable<String, Vector<String>> callGraphMap = analyser.getCallGraphMap();

//        for (String v : callGraphMap.keySet()) {
//            System.out.println(v + " " + callGraphMap.get(v).size());
//        }
    }

}
