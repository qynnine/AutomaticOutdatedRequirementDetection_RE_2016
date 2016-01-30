package parser;

import core.type.Granularity;
import document.SimilarityMatrix;
import document.SingleLink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by niejia on 14-7-10.
 */
public class RTMParser {

    public static SimilarityMatrix createSimilarityMatrix(List<String> columnsList, String contents, Granularity granularity) {

        SimilarityMatrix rtm = new SimilarityMatrix();
        String[] lines = contents.split("\n");

        int dupliNum = 0;

        for (int i = 0; i < lines.length; i++) {
            String[] tokens = lines[i].split(" ");

            String source = tokens[0];
            String target = tokens[1];
            Double score = Double.valueOf(tokens[2].trim());
            if (granularity.equals(Granularity.CLASS)) {

                /*
                PatientDAO::getExpiredPrescriptions -> PatientDAO
                auth.getPatientID_jsp::_jspService -> auth.getPatientID_jsp
                 */
                target = target.split("::")[0];
            } else if (granularity.equals(Granularity.METHOD)) {
                /*
                PatientDAO::getExpiredPrescriptions -> PatientDAO#getExpiredPrescriptions
                auth.getPatientID_jsp::_jspService -> auth.getPatientID_jsp#_jspService
                 */
                target = target.replace("::", "#");
            }
            SingleLink link = new SingleLink(source, target, score);


            if (!rtm.isLinkAboveThreshold(source, target)) {
                rtm.addLink(link.getSourceArtifactId(), link.getTargetArtifactId(), link.getScore());

            } else {
                dupliNum++;
            }
        }

        System.out.printf("Remove %d duplicated traces in RTM\n", dupliNum);
        return sort(columnsList, rtm);
    }

    /*
    Sort the Matrix in a sequential format
     */
    private static SimilarityMatrix sort(List<String> columnsList, SimilarityMatrix sims) {
        SimilarityMatrix sorted_sims = new SimilarityMatrix();

        // store sorted sourceIds
        List<String> sorted_ids = new ArrayList<>();
        for (String id : columnsList) {
            sorted_ids.add(id);
        }

        // sort sims by A-z order with sourceId, then descending with score
        for (String sourceId : sorted_ids) {
            Map<String, Double> links = sims.getLinksForSourceId(sourceId);
            ArrayList<SingleLink> linksList = new ArrayList<>();
            for (String targetId : links.keySet()) {
                double score = sims.getScoreForLink(sourceId, targetId);
                linksList.add(new SingleLink(sourceId, targetId, score));
            }

            Collections.sort(linksList, Collections.reverseOrder());

            for (SingleLink sg : linksList) {
                sorted_sims.addLink(sg.getSourceArtifactId(), sg.getTargetArtifactId(), sg.getScore());
            }
        }
        return sorted_sims;
    }
}
