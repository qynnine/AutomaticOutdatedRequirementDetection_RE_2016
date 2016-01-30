package exp.Aqualush.commits.change4;

import core.algo.JSS2015_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import exp.Aqualush.AqualushSetting;

/**
 * Created by niejia on 16/1/10.
 */
public class Aqualush_VSM_CochangeRegion_Change4 {

    public static void main(String[] args) {

        changeRegion(AqualushSetting.Aqualush_Change4_GroupedByMethod, AqualushSetting.Aqualush_CleanedRequirement,
                AqualushSetting.AqualushOracleChange4, "Change4");

    }

    public static void changeRegion(String code, String req, String oracle, String change) {
        System.out.println("----------" + change + "----------");
        TextDataset textDataset = new TextDataset(code,
                req, oracle);

        Result result_ir = IR.compute(textDataset, IRModelConst.VSM_ALL, new JSS2015_CSTI(), change);
        result_ir.showMatrix();
        result_ir.showAveragePrecisionByRanklist();
        System.out.println("--------------------");
    }

}
