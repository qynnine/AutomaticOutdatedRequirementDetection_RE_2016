package exp.Aqualush.commits.change4.legacy;

import core.algo.IRValueVoting_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import exp.Aqualush.AqualushSetting;

/**
 * Created by niejia on 15/12/12.
 */
public class Aqualush_VSM_JSEP_MyVersion_Change4 {
    public static void main(String[] args) {
        TextDataset textDataset4 = new TextDataset(AqualushSetting.Aqualush_Change4_GroupedByJSEP_MyVersion,
                AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange4);

        Result result_ir4 = IR.compute(textDataset4, IRModelConst.VSM_ALL, new IRValueVoting_CSTI(), "Change4");
        System.out.println("Change4");
        result_ir4.showMatrix();
        result_ir4.showAveragePrecisionByRanklist();
        result_ir4.showMeanAveragePrecisionByQuery();
        System.out.println("---------------------- ");

    }
}
