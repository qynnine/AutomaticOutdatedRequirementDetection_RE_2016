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
public class iTrustVSM_GroupChangesAsOne {

    public static void main(String[] args) {

//
        TextDataset textDataset_asOne = new TextDataset("data/iTrust/code_changes_as_a_whole",
                AppConfigure.iTrust_CleanedRequirement, AppConfigure.iTrustOracle);

        Result result_ir_asOne = IR.compute(textDataset_asOne, IRModelConst.VSM, new IRValueVoting_CSTI(), "iTrust");
        result_ir_asOne.showMatrix();
        result_ir_asOne.showAveragePrecisionByRanklist();
        result_ir_asOne.showMeanAveragePrecisionByQuery();

        VisualCurve curve = new VisualCurve();
        curve.addLine(result_ir_asOne);
        curve.showChart();
    }
}
