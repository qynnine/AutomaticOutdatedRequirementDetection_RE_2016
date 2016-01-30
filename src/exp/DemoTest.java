package exp;

import core.algo.None_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import util.AppConfigure;

/**
 * Created by niejia on 15/11/11.
 */
public class DemoTest {
    public static void main(String[] args) {
        TextDataset textDataset = new TextDataset("data/Demo/query",
                "data/Demo/document", AppConfigure.iTrustOracle);

        Result result_ir = IR.compute(textDataset, IRModelConst.VSM, new None_CSTI(), "iTrust");
        result_ir.showMatrix();
        result_ir.showAveragePrecisionByRanklist();
        result_ir.showMeanAveragePrecisionByQuery();

//        VisualCurve curve = new VisualCurve();
//        curve.addLine(result_ir);
//        curve.showChart();
    }
}
