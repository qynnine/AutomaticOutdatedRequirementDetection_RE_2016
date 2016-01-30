package exp.iTrust.legacy;

import core.algo.IRValueVoting_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import util.AppConfigure;

/**
 * Created by niejia on 15/11/8.
 */
public class SimpleVSM_Standard_Method_Region_Based {

    public static void main(String[] args) {

//
            TextDataset textDataset_methodRegion = new TextDataset(AppConfigure.iTrust_RegionGroupedByMethod,
                    AppConfigure.iTrust_CleanedRequirement, AppConfigure.iTrustOracle);

            Result result_ir_methodRegion = IR.compute(textDataset_methodRegion, IRModelConst.VSM, new IRValueVoting_CSTI(), "iTrust");
        result_ir_methodRegion.showWilcoxonDataCol("y");
//        result_ir_methodRegion.showMatrix();
            result_ir_methodRegion.showAveragePrecisionByRanklist();
            result_ir_methodRegion.showMeanAveragePrecisionByQuery();
//
//        VisualCurve curve = new VisualCurve();
//        curve.addLine(result_ir_methodRegion);
//        curve.showChart();
    }
}
