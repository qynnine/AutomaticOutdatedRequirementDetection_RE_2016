package exp.Aqualush.legacy;

import core.algo.IRValueVoting_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import util.AppConfigure;
import visual.VisualCurve;

/**
 * Created by niejia on 15/11/13.
 */
public class AqualushVSMCompare4 {
    public static void main(String[] args) {
        TextDataset textDataset_AsOne = new TextDataset("data/Aqualush/code_changes_as_a_whole",
                AppConfigure.Aqualush_CleanedRequirement, AppConfigure.AqualushOracle);

        Result result_ir_asOne = IR.compute(textDataset_AsOne, IRModelConst.VSM, new IRValueVoting_CSTI(), "Aqualush");
//        result_ir_asOne.showMatrix();
        result_ir_asOne.showAveragePrecisionByRanklist();
        result_ir_asOne.showMeanAveragePrecisionByQuery();


        TextDataset textDataset_singleMethod = new TextDataset("data/iTrust/code_changes_for_every_single_method",
                AppConfigure.Aqualush_CleanedRequirement, AppConfigure.AqualushOracle);

        Result result_ir_singleMethod = IR.compute(textDataset_singleMethod, IRModelConst.VSM, new IRValueVoting_CSTI(), "Aqualush");
//        result_ir_singleMethod.showMatrix();
        result_ir_singleMethod.showAveragePrecisionByRanklist();
        result_ir_singleMethod.showMeanAveragePrecisionByQuery();

        TextDataset textDataset_singleClass = new TextDataset("data/Aqualush/code_changes_for_every_single_class",
                AppConfigure.Aqualush_CleanedRequirement, AppConfigure.AqualushOracle);

        Result result_ir_singleClass = IR.compute(textDataset_singleClass, IRModelConst.VSM, new IRValueVoting_CSTI(), "Aqualush");
//        result_ir_singleClass.showMatrix();
        result_ir_singleClass.showAveragePrecisionByRanklist();
        result_ir_singleClass.showMeanAveragePrecisionByQuery();

        TextDataset textDataset_method = new TextDataset(AppConfigure.Aqualush_RegionGroupedByMethod,
                AppConfigure.Aqualush_CleanedRequirement, AppConfigure.AqualushOracle);

        Result result_ir_method = IR.compute(textDataset_method, IRModelConst.VSM, new IRValueVoting_CSTI(), "Aqualush");
//        result_ir_method.showMatrix();
        result_ir_method.showAveragePrecisionByRanklist();
        result_ir_method.showMeanAveragePrecisionByQuery();


        VisualCurve curve = new VisualCurve();
        curve.addLine(result_ir_asOne);
        curve.addLine(result_ir_singleMethod);
        curve.addLine(result_ir_singleClass);
        curve.addLine(result_ir_method);
        curve.showChart();
    }
}
