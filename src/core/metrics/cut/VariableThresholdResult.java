package core.metrics.cut;

import core.metrics.Result;
import document.SimilarityMatrix;
import document.SingleLink;

/**
 * Created by niejia on 14-8-29.
 */
public class VariableThresholdResult extends Result {
    public VariableThresholdResult(SimilarityMatrix matrix, SimilarityMatrix oracle) {
        super(matrix, oracle);
        resultName = "variable threshold";
        cutParameter = originMatrix.getVariableThreshold();
    }

    @Override
    public void showMetrics() {
    }

    public void setVariableThreshold(double val) {
        setCutParameter(val);
    }

    @Override
    public void setCutParameter(double val) {

        SimilarityMatrix sims = new SimilarityMatrix();
        originMatrix.setVariableThreshold(val);
        cutParameter = val;

        for (SingleLink link : originMatrix.allLinks()) {
            if (originMatrix.isLinkAboveVariableThreshold(link.getSourceArtifactId(), link.getTargetArtifactId())) {
                sims.addLink(link.getSourceArtifactId(), link.getTargetArtifactId(), link.getScore());
            } else {
                sims.addLink(link.getSourceArtifactId(), link.getTargetArtifactId(), 0.0);
            }
        }
        matrix = sims;
    }
}
