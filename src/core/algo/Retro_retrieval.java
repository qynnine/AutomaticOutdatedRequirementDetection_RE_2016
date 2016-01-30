package core.algo;

import core.dataset.TextDataset;
import core.metrics.MetricComputation;
import core.metrics.Result;
import core.metrics.cut.CutStrategy;
import document.ArtifactsCollection;
import document.LinksList;
import document.SimilarityMatrix;
import document.SingleLink;
import javafx.util.Pair;
import parser.RetroOutParser;

import java.util.*;

/**
 * Created by niejia on 15/12/31.
 */
public class Retro_retrieval {

    public static Result computeRetroResult(TextDataset textDataset, String path, String version, int reqCount) {
        SimilarityMatrix sm = RetroOutParser.createSimilarity(path, version, reqCount);
        SimilarityMatrix improved = improve(sm, textDataset, version);

        MetricComputation metricComputation = new MetricComputation(improved, textDataset.getRtm());
        Result result = metricComputation.compute(CutStrategy.CONSTANT_THRESHOLD);
        result.setAlgorithmName("Retro vsm");
        result.setCorrectImprovedTargetsList(new ArrayList<String>());
        result.setAlgorithmParameters(new ArrayList<Pair<String, String>>());
        result.setLog("");

        return result;
    }


    public static SimilarityMatrix improve(SimilarityMatrix matrix, TextDataset textDataset, String projectName) {

//        System.out.println(" matrix = " + matrix );

        SimilarityMatrix matrix_vote = new SimilarityMatrix();

        // Make a matrix to show class2req score
        SimilarityMatrix matrix_target2req = new SimilarityMatrix();

        SimilarityMatrix oracle = textDataset.getRtm();

        ArtifactsCollection requirements = textDataset.getTargetCollection();
        int reqCount = requirements.size();

        HashMap<String, Integer> reqScore = new HashMap<>();

        HashMap<String, List<String>> voteDetails = new HashMap<>();

        System.out.println(" reqCount = " + reqCount );

        for (String source : matrix.sourceArtifactsIds()) {


            Map<String, Double> links = matrix.getLinksForSourceId(source);

            LinksList linksList_target2req = new LinksList();

            int currentScore = reqCount;
            for (String target : links.keySet()) {
//                System.out.println(source + " " + target + " " + currentScore);
                if (links.get(target) > 0.0) {
                    if (reqScore.containsKey(target)) {
                        reqScore.put(target, reqScore.get(target) + currentScore);
                    } else {
                        reqScore.put(target, currentScore);
                    }

                    if (oracle.isLinkAboveScaleThreshold(projectName, target)) {

                        if (voteDetails.get(target) == null) {
                            List<String> details = new ArrayList<>();
                            details.add(source + " " + currentScore);
                            voteDetails.put(target, details);
                        } else {
                            List<String> details = voteDetails.get(target);
                            details.add(source + " " + currentScore);
                            voteDetails.put(target, details);
                        }
                    }

                    linksList_target2req.add(new SingleLink(source, target, currentScore * 1.0));
                    currentScore--;
                }

            }

//            voteDetails.put(source+" "+,subVoteInfo)
            Collections.sort(linksList_target2req, Collections.reverseOrder());
            for (SingleLink link : linksList_target2req) {
                matrix_target2req.addLink(link.getSourceArtifactId(), link.getTargetArtifactId(), link.getScore());
            }
        }

//        System.out.println(" matrix_target2req = " + matrix_target2req );

        LinksList linksList = new LinksList();
        for (String req : reqScore.keySet()) {
            linksList.add(new SingleLink(projectName, req, reqScore.get(req) * 1.0));
        }

        Collections.sort(linksList, Collections.reverseOrder());

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

//        for (String req : voteDetails.keySet()) {
//            StringBuilder sb = new StringBuilder();
//            sb.append(projectName + " " + req + " " + matrix_vote.getScoreForLink(projectName, req));
//            sb.append("\n");
//            for (String s : voteDetails.get(req)) {
//                sb.append(s);
//                sb.append(" ");
//            }
//            sb.append("\n");
//            System.out.println(sb.toString());
//        }



//        System.out.println(matrix_target2req);
        return matrix_vote;
    }
}
