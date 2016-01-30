package core.algo;

import core.dataset.TextDataset;
import document.ArtifactsCollection;
import document.LinksList;
import document.SimilarityMatrix;
import document.SingleLink;
import javafx.util.Pair;

import java.util.*;

/**
 * Created by niejia on 15/12/2.
 */
public class MergeTwoExpert implements CSTI{

    private SimilarityMatrix expertSM;

    public MergeTwoExpert(SimilarityMatrix expertSM) {
        this.expertSM = expertSM;
    }

    @Override
    public SimilarityMatrix improve(SimilarityMatrix matrix, TextDataset textDataset, String projectName) {
//        matrix = formatSimilarityToJSEPAlgo(matrix, textDataset);
//        expertSM = formatSimilarityToJSEPAlgo(matrix, textDataset);

        SimilarityMatrix matrix_merged = new SimilarityMatrix();

        for (String source : matrix.sourceArtifactsIds()) {
            Map<String, Double> links = matrix.getLinksForSourceId(source);

            LinksList linksList = new LinksList();

            for (String target : links.keySet()) {
                double score = matrix.getScoreForLink(source, target);
                linksList.add(new SingleLink(source, target, score + expertSM.getScoreForLink(source, target)));
            }
            Collections.sort(linksList, Collections.reverseOrder());
            for (SingleLink link : linksList) {
                matrix_merged.addLink(link.getSourceArtifactId(), link.getTargetArtifactId(), link.getScore());
            }
        }

        return matrix_merged;
    }

    private SimilarityMatrix formatSimilarityToJSEPAlgo(SimilarityMatrix sm, TextDataset textDataset) {
        SimilarityMatrix formattedSM = new SimilarityMatrix();
        ArtifactsCollection requirements = textDataset.getTargetCollection();
        int reqCount = requirements.size();

        HashMap<String, Integer> reqScore = new HashMap<>();
        System.out.println(" reqCount = " + reqCount);

        for (String source : sm.sourceArtifactsIds()) {
            Map<String, Double> links = sm.getLinksForSourceId(source);

            LinksList linksList_target2req = new LinksList();

            int currentScore = reqCount;
            for (String target : links.keySet()) {
                if (reqScore.containsKey(target)) {
                    reqScore.put(target, reqScore.get(target) + currentScore);
                } else {
                    reqScore.put(target, currentScore);
                }
                linksList_target2req.add(new SingleLink(source, target, currentScore * 1.0));
                currentScore--;
            }
            Collections.sort(linksList_target2req, Collections.reverseOrder());
            for (SingleLink link : linksList_target2req) {
                formattedSM.addLink(link.getSourceArtifactId(), link.getTargetArtifactId(), link.getScore());
            }
        }
        return formattedSM;
    }

    @Override
    public String getAlgorithmName() {
        return "MergeTwoExpert";
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
