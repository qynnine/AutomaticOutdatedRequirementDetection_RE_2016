package exp.iTrust.legacy;

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
public class iTrustVSM_NoGroupJustSingleMethod {

    public static void main(String[] args) {
        TextDataset textDataset_singleMethod = new TextDataset("data/iTrust/code_changes_for_every_single_method",
                AppConfigure.iTrust_CleanedRequirement, AppConfigure.iTrustOracle);

        Result result_ir_singleMethod = IR.compute(textDataset_singleMethod, IRModelConst.VSM, new IRValueVoting_CSTI(), "iTrust");
        result_ir_singleMethod.showMatrix();
        result_ir_singleMethod.showAveragePrecisionByRanklist();
        result_ir_singleMethod.showMeanAveragePrecisionByQuery();

        VisualCurve curve = new VisualCurve();
        curve.addLine(result_ir_singleMethod);
        curve.showChart();
    }
}
