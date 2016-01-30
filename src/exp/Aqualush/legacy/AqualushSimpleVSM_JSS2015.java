package exp.Aqualush.legacy;

import core.algo.JSS2015_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import util.AppConfigure;

/**
 * Created by niejia on 15/11/6.
 */
public class AqualushSimpleVSM_JSS2015 {

    public static void main(String[] args) {
        TextDataset textDataset = new TextDataset(AppConfigure.Aqualush_CodeChangesGroupedByClass,
                AppConfigure.Aqualush_CleanedRequirement, AppConfigure.AqualushOracle);

        Result result_ir = IR.compute(textDataset, IRModelConst.VSM, new JSS2015_CSTI(), "Aqualush");
//        result_ir.showMatrix();
        result_ir.showAveragePrecisionByRanklist();
        result_ir.showMeanAveragePrecisionByQuery();

        result_ir.showWilcoxonDataCol("x");

//        VisualCurve curve = new VisualCurve();
//        curve.addLine(result_ir);
//        curve.showChart();

    }
}
