package exp.Aqualush.commits.change99;

import core.algo.IRValueVoting_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import exp.Aqualush.AqualushSetting;
import visual.VisualCurve;

/**
 * Created by niejia on 15/12/5.
 */
public class Aqualush_VSM_ChangeRegion_Change99 {
    public static void main(String[] args) {
        TextDataset textDataset = new TextDataset(AqualushSetting.Aqualush_Change99_GroupedByMethod,
                AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange99);

        Result result_ir = IR.compute(textDataset, IRModelConst.VSM_ALL, new IRValueVoting_CSTI(), "Change99");
        result_ir.showMatrix();
        result_ir.showAveragePrecisionByRanklist();
        result_ir.showMeanAveragePrecisionByQuery();

        VisualCurve curve = new VisualCurve();
        curve.addLine(result_ir);
        curve.showChart();
    }
}
