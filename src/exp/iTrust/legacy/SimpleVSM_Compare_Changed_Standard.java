package exp.iTrust.legacy;

import core.algo.JSS2015_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import util.AppConfigure;
import visual.VisualCurve;

/**
 * Created by niejia on 15/11/2.
 */
public class SimpleVSM_Compare_Changed_Standard {

    public static void main(String[] args) {
        TextDataset textDataset_change = new TextDataset(AppConfigure.iTrust_CodeChangesGroupedByClass,
                AppConfigure.iTrust_CleanedRequirement, AppConfigure.iTrustOracle);

        Result result_ir_change = IR.compute(textDataset_change, IRModelConst.VSM, new JSS2015_CSTI(),"iTrust");

        TextDataset textDataset_standard = new TextDataset(AppConfigure.iTrust_StandardClass,
                AppConfigure.iTrust_CleanedRequirement, AppConfigure.iTrustOracle);

        Result result_ir_standard = IR.compute(textDataset_standard, IRModelConst.VSM, new JSS2015_CSTI(),"iTrust");

//        result_ir_change.showMatrix();
        result_ir_change.showAveragePrecisionByRanklist();
        result_ir_change.showMeanAveragePrecisionByQuery();
        result_ir_standard.showAveragePrecisionByRanklist();
        result_ir_standard.showMeanAveragePrecisionByQuery();

        VisualCurve curve = new VisualCurve();
        curve.addLine(result_ir_change);
        curve.addLine(result_ir_standard);
        curve.showChart();
    }
}
