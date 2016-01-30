package core.metrics.cut;


import core.metrics.Result;
import document.SimilarityMatrix;
import document.SingleLink;

/**
 * Created by niejia on 14-8-29.
 */
public class VariableCutResult extends Result {
    public VariableCutResult(SimilarityMatrix matrix, SimilarityMatrix oracle) {
        super(matrix, oracle);
        resultName = "variable cut";
        cutParameter = originMatrix.getVariableCut();
    }

    @Override
    public void showMetrics() {
    }

    public void setVariableCut(double val) {
        setCutParameter(val);
    }

    @Override
    public void setCutParameter(double val) {

        SimilarityMatrix sims = new SimilarityMatrix();
        originMatrix.setVariableCut(val);
        cutParameter = val;
        for (SingleLink link : originMatrix.allLinks()) {
            if (originMatrix.isLinkInVariableCut(link.getSourceArtifactId(), link.getTargetArtifactId())) {
                sims.addLink(link.getSourceArtifactId(), link.getTargetArtifactId(), link.getScore());
            } else {
                sims.addLink(link.getSourceArtifactId(), link.getTargetArtifactId(), 0.0);
            }
        }
        matrix = sims;
    }
}
