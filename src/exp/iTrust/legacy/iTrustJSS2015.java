package exp.iTrust.legacy;

import core.algo.IRValueVoting_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import util.AppConfigure;
import visual.VisualCurve;

/**
 * Created by niejia on 15/11/1.
 */
public class iTrustJSS2015 {

    public static void main(String[] args) {
        TextDataset textDataset_asOne = new TextDataset("data/iTrust/code_changes_as_a_whole",
                AppConfigure.iTrust_CleanedRequirement, AppConfigure.iTrustOracle);

        Result result_ir_asOne = IR.compute(textDataset_asOne, IRModelConst.JSD, new IRValueVoting_CSTI(), "iTrust");
//        result_ir_asOne.showMatrix();
        result_ir_asOne.showAveragePrecisionByRanklist();
        result_ir_asOne.showMeanAveragePrecisionByQuery();

        TextDataset textDataset_singleMethod = new TextDataset("data/iTrust/code_changes_for_every_single_method",
                AppConfigure.iTrust_CleanedRequirement, AppConfigure.iTrustOracle);

        Result result_ir_singleMethod = IR.compute(textDataset_singleMethod, IRModelConst.JSD, new IRValueVoting_CSTI(), "iTrust");
//        result_ir_singleMethod.showMatrix();
        result_ir_singleMethod.showAveragePrecisionByRanklist();
        result_ir_singleMethod.showMeanAveragePrecisionByQuery();

        TextDataset textDataset_singleClass = new TextDataset("data/iTrust/code_changes_for_every_single_class",
                AppConfigure.iTrust_CleanedRequirement, AppConfigure.iTrustOracle);

        Result result_ir_singleClass = IR.compute(textDataset_singleClass, IRModelConst.JSD, new IRValueVoting_CSTI(), "iTrust");
//        result_ir_singleClass.showMatrix();
        result_ir_singleClass.showAveragePrecisionByRanklist();
        result_ir_singleClass.showMeanAveragePrecisionByQuery();

        TextDataset textDataset_methodRegion = new TextDataset(AppConfigure.iTrust_RegionGroupedByMethod,
                AppConfigure.iTrust_CleanedRequirement, AppConfigure.iTrustOracle);

        Result result_ir_methodRegion = IR.compute(textDataset_methodRegion, IRModelConst.JSD, new IRValueVoting_CSTI(), "iTrust");
        result_ir_methodRegion.showMatrix();
        result_ir_methodRegion.showAveragePrecisionByRanklist();
        result_ir_methodRegion.showMeanAveragePrecisionByQuery();

        VisualCurve curve = new VisualCurve();

        curve.addLine(result_ir_asOne);
        curve.addLine(result_ir_singleMethod);
        curve.addLine(result_ir_singleClass);

        curve.addLine(result_ir_methodRegion);
        curve.showChart();
    }
}
