package core.algo;

import core.dataset.TextDataset;
import document.ArtifactsCollection;
import document.LinksList;
import document.SimilarityMatrix;
import document.SingleLink;
import javafx.util.Pair;

import java.util.*;

/**
 * Created by niejia on 15/11/17.
 */
public class RegionMaxIRValue_CSTI implements CSTI {


    @Override
    public SimilarityMatrix improve(SimilarityMatrix matrix, TextDataset textDataset, String projectName) {
        SimilarityMatrix matrix_vote = new SimilarityMatrix();
        System.out.println(" matrix = " + matrix );
        ArtifactsCollection requirements = textDataset.getTargetCollection();

        HashMap<String, Double> reqScore = new HashMap<>();

        for (String source : matrix.sourceArtifactsIds()) {
            Map<String, Double> links = matrix.getLinksForSourceId(source);

            for (String target : links.keySet()) {
                if (reqScore.containsKey(target)) {
                    reqScore.put(target, Math.max(reqScore.get(target), matrix.getScoreForLink(source, target)));
                } else {
                    double score = matrix.getScoreForLink(source, target);
                    reqScore.put(target, score);
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

        return matrix_vote;
    }

    @Override
    public String getAlgorithmName() {
        return "RegionMaxIRValue";
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
