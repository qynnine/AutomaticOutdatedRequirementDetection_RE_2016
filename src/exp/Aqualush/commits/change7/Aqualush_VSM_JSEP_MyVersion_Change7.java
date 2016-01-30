package exp.Aqualush.commits.change7;

import core.algo.JSS2015_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import exp.Aqualush.AqualushSetting;

/**
 * Created by niejia on 15/12/12.
 */
public class Aqualush_VSM_JSEP_MyVersion_Change7 {
    public static void main(String[] args) {

        TextDataset textDataset7 = new TextDataset(AqualushSetting.Aqualush_Change7_GroupedByJSEP_MyVersion,
                AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange7);

        Result result_ir7 = IR.compute(textDataset7, IRModelConst.VSM_ALL, new JSS2015_CSTI(), "Change7");
        System.out.println("Change7");
        result_ir7.showMatrix();
        result_ir7.showAveragePrecisionByRanklist();
        result_ir7.showMeanAveragePrecisionByQuery();
        System.out.println("---------------------- ");

    }
}
