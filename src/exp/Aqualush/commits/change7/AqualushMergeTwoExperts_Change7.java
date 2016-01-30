package exp.Aqualush.commits.change7;

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
public class AqualushMergeTwoExperts_Change7 {
    public static void main(String[] args) {
        TextDataset textDataset = new TextDataset("data/Aqualush/cleaned_commit/change7",
                AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange7);

        Result result_ir = IR.compute(textDataset, IRModelConst.VSM, new MergeTwoExpert(getExpertSM()), "Change7");
        result_ir.showMatrix();
        result_ir.showAveragePrecisionByRanklist();
        result_ir.showMeanAveragePrecisionByQuery();

        int n = 35;
        System.out.println("AveragePrecision At " + n + ": " + result_ir.getAveragePrecisionByRanklistAtCutN(n));

        VisualCurve curve = new VisualCurve();
        curve.addLine(result_ir);
        curve.showChart();
    }

    private static SimilarityMatrix getExpertSM() {
        TextDataset textDataset = new TextDataset(AqualushSetting.Aqualush_Change7_GroupedByJSEP,
                AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange7);

        Result result_ir = IR.compute(textDataset, IRModelConst.VSM, new IRValueVoting_CSTI(), "Change7");
        return result_ir.getMatrix();
    }
}
