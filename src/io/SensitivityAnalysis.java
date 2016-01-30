package io;

import util._;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by niejia on 15/11/6.
 */
public class SensitivityAnalysis {

    private TreeMap<Double, Double> averagePrecision;
    private TreeMap<Double, Double> meanAveragePrecision;

    public SensitivityAnalysis(String path) {
        System.out.println(" path = " + path );
        String input = _.readFile(path);
        averagePrecision = new TreeMap<>();
        meanAveragePrecision = new TreeMap<>();

        String[] lines = input.split("\n");

        double low = 0.14;
        double high = 0.46;

//        double low = 0.14;
//        double high = 0.50;

        double threshold = 0.05;
        for (int i = 0; i < lines.length; ) {
            if (lines[i].startsWith("callThreashold") || lines[i].startsWith("dataThreashold")) {
//                System.out.println(" threshold = " + threshold );

                i++;
                String l = lines[i].split("=")[1];
                String subl = l.substring(1, l.length());
                double avp = Double.parseDouble(subl);
                if (threshold > low && threshold < high) {
                    averagePrecision.put(threshold, avp);
                }

                i++;

                String l2 = lines[i].split("=")[1];
                String subl2 = l2.substring(1, l2.length());
                double mavp = Double.parseDouble(subl2);
//                System.out.println(" mavp = " + mavp );
                if (threshold > low && threshold < high) {
                    meanAveragePrecision.put(threshold, mavp);
                }
                threshold += 0.05;
            } else {
                i++;
            }
        }

        List<Double> apScore = new ArrayList<>();
        for (Double d : averagePrecision.values()) {
            apScore.add(d);
        }

        Collections.sort(apScore, Collections.reverseOrder());
        double maxAP = apScore.get(0);
        double minAP = apScore.get(apScore.size() - 1);
        System.out.println("Max ap: " + apScore.get(0));
        System.out.println("Mini ap: " + apScore.get(apScore.size() - 1));

        List<Double> mapScore = new ArrayList<>();
        for (Double d : meanAveragePrecision.values()) {
            mapScore.add(d);
        }

        Collections.sort(mapScore, Collections.reverseOrder());
        double maxMAP = mapScore.get(0);
        double minMAP = mapScore.get(mapScore.size() - 1);
        System.out.println("Max map: " + mapScore.get(0));
        System.out.println("Mini map: " + mapScore.get(mapScore.size() - 1));

        double baseAP = 0.38830036210111346;
        double baseMAP = 0.4797267471857778;

        System.out.println("AP");
        System.out.println("Max AP diff: " + ((maxAP / baseAP) - 1.0));
        System.out.println("Mini AP diff: " + (1.0 - (minAP / baseAP)));

        System.out.println("MAP");
        System.out.println("Max MAP diff: " + ((maxMAP / baseMAP) - 1.0));
        System.out.println("Mini MAP diff: " + (1.0 - (minMAP / baseMAP)));

    }

    public static void main(String[] args) {
//        SensitivityAnalysis analysis1 = new SensitivityAnalysis("data/SensitivityAnalysis/iTrust VSM Call.txt");
//        SensitivityAnalysis analysis2 = new SensitivityAnalysis("data/SensitivityAnalysis/iTrust JS Call.txt");

//        SensitivityAnalysis analysis3 = new SensitivityAnalysis("data/SensitivityAnalysis/iTrust VSM Data.txt");
//        SensitivityAnalysis analysis4 = new SensitivityAnalysis("data/SensitivityAnalysis/iTrust JS Data.txt");
//
//        SensitivityAnalysis analysis5 = new SensitivityAnalysis("data/SensitivityAnalysis/Gantt VSM Call.txt");
//        SensitivityAnalysis analysis6 = new SensitivityAnalysis("data/SensitivityAnalysis/Gantt JS Call.txt");
//
        SensitivityAnalysis analysis7 = new SensitivityAnalysis("data/SensitivityAnalysis/Gantt VSM Data.txt");
//        SensitivityAnalysis analysis8 = new SensitivityAnalysis("data/SensitivityAnalysis/Gantt JS Data.txt");
    }
}
