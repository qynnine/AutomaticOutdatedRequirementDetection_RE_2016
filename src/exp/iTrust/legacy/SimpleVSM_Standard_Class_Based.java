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
public class SimpleVSM_Standard_Class_Based {

    public static void main(String[] args) {
        TextDataset textDataset = new TextDataset(AppConfigure.iTrust_StandardClass,
                AppConfigure.iTrust_CleanedRequirement, AppConfigure.iTrustOracle);

        Result result_ir = IR.compute(textDataset, IRModelConst.JSD, new JSS2015_CSTI(),"iTrust");
        result_ir.showMatrix();
        result_ir.showAveragePrecisionByRanklist();
        result_ir.showMeanAveragePrecisionByQuery();

        VisualCurve curve = new VisualCurve();
        curve.addLine(result_ir);
        curve.showChart();
    }
}
