package exp.iTrust.legacy;

import core.algo.JSS2015_CSTI;
import core.algo.IRValueVoting_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import util.AppConfigure;
import visual.VisualCurve;

/**
 * Created by niejia on 15/11/12.
 */
public class iTrustVSM_Compare_Method_Class_Based {
    public static void main(String[] args) {
        TextDataset textDataset_class = new TextDataset(AppConfigure.iTrust_CodeChangesGroupedByClass,
                AppConfigure.iTrust_CleanedRequirement, AppConfigure.iTrustOracle);

        Result result_ir_class = IR.compute(textDataset_class, IRModelConst.VSM, new JSS2015_CSTI(), "iTrust");
        result_ir_class.showMatrix();
        result_ir_class.showAveragePrecisionByRanklist();
        result_ir_class.showMeanAveragePrecisionByQuery();


        TextDataset textDataset_method = new TextDataset(AppConfigure.iTrust_RegionGroupedByMethod,
                AppConfigure.iTrust_CleanedRequirement, AppConfigure.iTrustOracle);

        Result result_ir_method = IR.compute(textDataset_method, IRModelConst.VSM, new IRValueVoting_CSTI(), "iTrust");
        result_ir_method.showMatrix();
        result_ir_method.showAveragePrecisionByRanklist();
        result_ir_method.showMeanAveragePrecisionByQuery();

        VisualCurve curve = new VisualCurve();
        curve.addLine(result_ir_class);
        curve.addLine(result_ir_method);
        curve.showChart();
    }
}
