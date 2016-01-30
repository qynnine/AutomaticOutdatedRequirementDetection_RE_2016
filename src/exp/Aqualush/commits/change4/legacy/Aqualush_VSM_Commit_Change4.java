package exp.Aqualush.commits.change4.legacy;

import core.algo.None_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import exp.Aqualush.AqualushSetting;
import visual.VisualCurve;

/**
 * Created by niejia on 15/12/2.
 */
public class Aqualush_VSM_Commit_Change4 {
    public static void main(String[] args) {
        TextDataset textDataset = new TextDataset("data/Aqualush/cleaned_commit/change4",
                AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange4);

        Result result_ir = IR.compute(textDataset, IRModelConst.VSM, new None_CSTI(), "Change4");
        result_ir.showMatrix();
        result_ir.showAveragePrecisionByRanklist();
        result_ir.showMeanAveragePrecisionByQuery();

        VisualCurve curve = new VisualCurve();
        curve.addLine(result_ir);
        curve.showChart();
    }
}
