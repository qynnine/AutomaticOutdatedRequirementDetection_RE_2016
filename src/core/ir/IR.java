package core.ir;


import core.algo.CSTI;
import core.dataset.TextDataset;
import core.metrics.MetricComputation;
import core.metrics.Result;
import core.metrics.cut.CutStrategy;
import document.SimilarityMatrix;

/**
 * Created by niejia on 15/2/23.
 */
public class IR {

    public static Result compute(TextDataset textDataset, String modelType, CSTI algorithm, String projectName) {
        Result result = null;

        try {
            Class modelTypeClass = Class.forName(modelType);
            IRModel irModel = (IRModel) modelTypeClass.newInstance();
            SimilarityMatrix similarityMatrix = irModel.Compute(textDataset);

            SimilarityMatrix matrix_improve = algorithm.improve(similarityMatrix, textDataset, projectName);

            MetricComputation metricComputation = new MetricComputation(matrix_improve, textDataset.getRtm());

            result = metricComputation.compute(CutStrategy.CONSTANT_THRESHOLD);
            result.setAlgorithmName(algorithm.getAlgorithmName());
            result.setCorrectImprovedTargetsList(algorithm.getCorrectImprovedTargetsList());
            result.setAlgorithmParameters(algorithm.getAlgorithmParameters());
            result.setLog(algorithm.getDetails());
        } catch (ClassNotFoundException e) {
            System.out.println("No such IR model exists");
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        result.setModel(modelType.toString());
//        result.setAlgorithmName(algorithm.getAlgorithmName());
//        result.setAlgorithmParameters(algorithm.getAlgorithmParameters());
//        result.setLog(algorithm.getDetails());

        return result;
    }

    public static DocVectorLookUp getDocVectorLookUp(TextDataset textDataset, String modelType, CSTI algorithm) {

        try {
            Class modelTypeClass = Class.forName(modelType);
            IRModel irModel = (IRModel) modelTypeClass.newInstance();

            SimilarityMatrix similarityMatrix = irModel.Compute(textDataset);
            return new DocVectorLookUp(irModel.getTermDocumentMatrixOfQueries());

        } catch (ClassNotFoundException e) {
            System.out.println("No such IR model exists");
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
