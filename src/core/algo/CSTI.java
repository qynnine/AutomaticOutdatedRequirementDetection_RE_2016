package core.algo;

import core.dataset.TextDataset;
import document.SimilarityMatrix;
import javafx.util.Pair;

import java.util.List;

/**
 * Created by niejia on 15/3/3.
 */
public interface CSTI {
    public SimilarityMatrix improve(SimilarityMatrix matrix, TextDataset textDataset, String projectName);
    public String getAlgorithmName();

    public List<Pair<String, String>> getAlgorithmParameters();

    public String getDetails();

    public List<String> getCorrectImprovedTargetsList();
}
