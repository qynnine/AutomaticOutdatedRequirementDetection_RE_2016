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
 * Created by niejia on 15/11/8.
 */
public class SimpleVSM_Compare_JSS2015_MethodRegion {

    public static void main(String[] args) {

        TextDataset textDataset = new TextDataset(AppConfigure.iTrust_CodeChangesGroupedByClass,
                AppConfigure.iTrust_CleanedRequirement, AppConfigure.iTrustOracle);

        Result result_ir = IR.compute(textDataset, IRModelConst.VSM, new JSS2015_CSTI(),"iTrust");
        result_ir.showMatrix();
        result_ir.showAveragePrecisionByRanklist();
        result_ir.showMeanAveragePrecisionByQuery();

        TextDataset textDataset_methodRegion = new TextDataset(AppConfigure.iTrust_RegionGroupedByMethod,
                AppConfigure.iTrust_CleanedRequirement, AppConfigure.iTrustOracle);

        Result result_methodRegion = IR.compute(textDataset_methodRegion, IRModelConst.VSM, new IRValueVoting_CSTI(), "iTrust");
//        result_methodRegion.showMatrix();

        result_ir.showAveragePrecisionByRanklist();
        result_ir.showMeanAveragePrecisionByQuery();
        result_methodRegion.showAveragePrecisionByRanklist();
        result_methodRegion.showMeanAveragePrecisionByQuery();

        VisualCurve curve = new VisualCurve();
        curve.addLine(result_ir);
        curve.addLine(result_methodRegion);
        curve.showChart();

    }
}
