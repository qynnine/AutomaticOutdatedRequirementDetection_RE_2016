package core.algo;

import core.dataset.TextDataset;
import document.ArtifactsCollection;
import document.LinksList;
import document.SimilarityMatrix;
import document.SingleLink;
import javafx.util.Pair;

import java.util.*;

/**
 * Created by niejia on 15/11/4.
 */
public class IRValueVoting_CSTI implements CSTI {

    @Override
    public SimilarityMatrix improve(SimilarityMatrix matrix, TextDataset textDataset, String projectName) {
        SimilarityMatrix matrix_vote = new SimilarityMatrix();
        System.out.println(" matrix = " + matrix );


//        for (String source : matrix.sourceArtifactsIds()) {
//            Map<String, Double> links = matrix.getLinksForSourceId(source);
//            Double min = Double.MAX_VALUE;
//            Double max = Double.MIN_VALUE;
//
//            for (Double d : links.values()) {
//                min = Math.min(min, d);
//                max = Math.max(max, d);
//            }
//
//            for (String target : links.keySet()) {
//                double normalized = (matrix.getScoreForLink(source, target) - min) / (max - min);
//                matrix.setScoreForLink(source, target, normalized);
//            }
//        }

//        System.out.println(" matrix = " + matrix );

        SimilarityMatrix oracle = textDataset.getRtm();
        ArtifactsCollection requirements = textDataset.getTargetCollection();
        HashMap<String, List<String>> voteDetails = new HashMap<>();

        HashMap<String, Double> reqScore = new HashMap<>();

        for (String source : matrix.sourceArtifactsIds()) {
            Map<String, Double> links = matrix.getLinksForSourceId(source);

            for (String target : links.keySet()) {
                if (links.get(target) > 0.0) {
                    if (reqScore.containsKey(target)) {
                        reqScore.put(target, reqScore.get(target) + matrix.getScoreForLink(source, target));
                    } else {
                        double score = matrix.getScoreForLink(source, target);
                        reqScore.put(target, score);
                    }


                    if (oracle.isLinkAboveScaleThreshold(projectName, target)) {

                        if (voteDetails.get(target) == null) {
                            List<String> details = new ArrayList<>();
                            details.add(source + " " + matrix.getScoreForLink(source, target));
                            voteDetails.put(target, details);
                        } else {
                            List<String> details = voteDetails.get(target);
                            details.add(source + " " + matrix.getScoreForLink(source, target));
                            voteDetails.put(target, details);
                        }
                    }
                }
            }
        }

        LinksList linksList = new LinksList();
        for (String req : reqScore.keySet()) {
            linksList.add(new SingleLink(projectName, req, reqScore.get(req) * 1.0));
        }

        Collections.sort(linksList, Collections.reverseOrder());

        for (SingleLink link : linksList) {
            matrix_vote.addLink(link.getSourceArtifactId(), link.getTargetArtifactId(), link.getScore());
        }

        System.out.println("Vote Details");
        for (SingleLink link : linksList) {
            matrix_vote.addLink(link.getSourceArtifactId(), link.getTargetArtifactId(), link.getScore());

            // Show Vote Details
            if (oracle.isLinkAboveThreshold(link.getSourceArtifactId(), link.getTargetArtifactId())) {
                String req = link.getTargetArtifactId();
                StringBuilder sb = new StringBuilder();
                sb.append(projectName + " " + req + " " + matrix_vote.getScoreForLink(projectName, req));
                sb.append("\n");
                sb.append("[\n");
                for (String s : voteDetails.get(req)) {
                    String[] tokens = s.split(" ");
                    sb.append(tokens[0] + " " + req + " " + tokens[1]);
                    sb.append("\n");
                }
                sb.append("]\n");
                sb.append("\n");
                System.out.println(sb.toString());
            }
        }

        return matrix_vote;
    }

    @Override
    public String getAlgorithmName() {
        return "RegionBased";
    }

    @Override
    public List<Pair<String, String>> getAlgorithmParameters() {
        List parameters = new ArrayList();
        Pair<String, String> p = new Pair<>("None", "");
        parameters.add(p);
        return parameters;
    }

    @Override
    public String getDetails() {
        return "";
    }

    @Override
    public List<String> getCorrectImprovedTargetsList() {
        return null;
    }
}
