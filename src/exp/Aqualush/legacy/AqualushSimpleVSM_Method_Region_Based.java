package exp.Aqualush.legacy;

import core.algo.IRValueVoting_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import util.AppConfigure;

/**
 * Created by niejia on 15/11/9.
 */
public class AqualushSimpleVSM_Method_Region_Based {

    public static void main(String[] args) {
        TextDataset textDataset_method = new TextDataset(AppConfigure.Aqualush_RegionGroupedByMethod,
                AppConfigure.Aqualush_CleanedRequirement, AppConfigure.AqualushOracle);

        Result result_ir_method = IR.compute(textDataset_method, IRModelConst.VSM, new IRValueVoting_CSTI(), "Aqualush");
//        result_ir_method.showMatrix();
        result_ir_method.showAveragePrecisionByRanklist();
        result_ir_method.showMeanAveragePrecisionByQuery();
//        result_ir_method

        result_ir_method.showWilcoxonDataCol("y");

//        VisualCurve curve = new VisualCurve();
//        curve.addLine(result_ir_method);
//        curve.showChart();

    }
}
