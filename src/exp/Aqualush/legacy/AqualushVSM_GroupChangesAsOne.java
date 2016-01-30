package exp.Aqualush.legacy;

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
public class AqualushVSM_GroupChangesAsOne {

    public static void main(String[] args) {
        TextDataset textDataset_AsOne = new TextDataset("data/Aqualush/code_changes_as_a_whole",
                AppConfigure.Aqualush_CleanedRequirement, AppConfigure.AqualushOracle);

        Result result_ir_asOne = IR.compute(textDataset_AsOne, IRModelConst.VSM, new IRValueVoting_CSTI(), "Aqualush");
        result_ir_asOne.showMatrix();
        result_ir_asOne.showAveragePrecisionByRanklist();
        result_ir_asOne.showMeanAveragePrecisionByQuery();

        VisualCurve curve = new VisualCurve();
        curve.addLine(result_ir_asOne);
        curve.showChart();
    }
}
