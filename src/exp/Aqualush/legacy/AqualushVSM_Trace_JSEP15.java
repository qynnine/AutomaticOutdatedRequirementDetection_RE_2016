package exp.Aqualush.legacy;

import core.algo.None_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import util.AppConfigure;

/**
 * Created by niejia on 15/11/18.
 */
public class AqualushVSM_Trace_JSEP15 {
    public static void main(String[] args) {
        TextDataset textDataset = new TextDataset(AppConfigure.Aqualush_CodeChangesGroupedByClass,
                AppConfigure.Aqualush_CleanedRequirement, AppConfigure.AqualushOracle_JSEP15);

        Result result_ir = IR.compute(textDataset, IRModelConst.VSM, new None_CSTI(), "Aqualush");
//        result_ir.showMatrix();
        result_ir.showAveragePrecisionByRanklist();
        result_ir.showMeanAveragePrecisionByQuery();

        result_ir.showWilcoxonDataCol("x");

//        VisualCurve curve = new VisualCurve();
//        curve.addLine(result_ir);
//        curve.showChart();
    }
}
