package core.algo;

import core.dataset.TextDataset;
import document.SimilarityMatrix;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niejia on 15/3/3.
 */
public class None_CSTI implements CSTI {
    @Override
    public SimilarityMatrix improve(SimilarityMatrix matrix, TextDataset textDataset, String projectName) {
        return matrix;
    }

    @Override
    public String getAlgorithmName() {
        return "IR_Only";
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
