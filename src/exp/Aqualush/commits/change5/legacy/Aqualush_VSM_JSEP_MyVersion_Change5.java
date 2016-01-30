package exp.Aqualush.commits.change5.legacy;

import core.algo.JSS2015_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import exp.Aqualush.AqualushSetting;

/**
 * Created by niejia on 15/12/12.
 */
public class Aqualush_VSM_JSEP_MyVersion_Change5 {
    public static void main(String[] args) {

        TextDataset textDataset5 = new TextDataset(AqualushSetting.Aqualush_Change5_GroupedByJSEP_MyVersion,
                AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange5);

        Result result_ir5 = IR.compute(textDataset5, IRModelConst.VSM, new JSS2015_CSTI(), "Change5");
        System.out.println("Change5");
        result_ir5.showMatrix();
        result_ir5.showAveragePrecisionByRanklist();
        result_ir5.showMeanAveragePrecisionByQuery();
        System.out.println("---------------------- ");

    }
}
