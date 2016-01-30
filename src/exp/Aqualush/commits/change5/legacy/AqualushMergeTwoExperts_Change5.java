package exp.Aqualush.commits.change5.legacy;

import core.algo.MergeTwoExpert;
import core.algo.IRValueVoting_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import document.SimilarityMatrix;
import exp.Aqualush.AqualushSetting;
import visual.VisualCurve;

/**
 * Created by niejia on 15/12/2.
 */
public class AqualushMergeTwoExperts_Change5 {
    public static void main(String[] args) {
        TextDataset textDataset = new TextDataset("data/Aqualush/cleaned_commit/change5",
                AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange5);

        Result result_ir = IR.compute(textDataset, IRModelConst.VSM, new MergeTwoExpert(getExpertSM()), "Change5");
        result_ir.showMatrix();
        result_ir.showAveragePrecisionByRanklist();
        result_ir.showMeanAveragePrecisionByQuery();

        VisualCurve curve = new VisualCurve();
        curve.addLine(result_ir);
        curve.showChart();
    }

    private static SimilarityMatrix getExpertSM() {
        TextDataset textDataset = new TextDataset(AqualushSetting.Aqualush_Change5_GroupedByJSEP,
                AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange5);

        Result result_ir = IR.compute(textDataset, IRModelConst.VSM, new IRValueVoting_CSTI(), "Change5");
        return result_ir.getMatrix();
    }
}
