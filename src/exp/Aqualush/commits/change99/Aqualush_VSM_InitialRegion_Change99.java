package exp.Aqualush.commits.change99;

import core.algo.IRValueVoting_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import exp.Aqualush.AqualushSetting;

/**
 * Created by niejia on 16/1/9.
 */
public class Aqualush_VSM_InitialRegion_Change99 {
    public static void main(String[] args) {
        changeRegion(AqualushSetting.Aqualush_Change99_GroupByInitialRegionPath, AqualushSetting.Aqualush_CleanedRequirement,
                AqualushSetting.AqualushOracleChange99, "Change99");

    }

    public static void changeRegion(String code, String req, String oracle, String change) {
        System.out.println("----------" + change + "----------");
        TextDataset textDataset = new TextDataset(code,
                req, oracle);

        Result result_ir = IR.compute(textDataset, IRModelConst.VSM_ALL, new IRValueVoting_CSTI(), change);
//        result_ir.showMatrix();
        result_ir.showAveragePrecisionByRanklist();
        System.out.println("--------------------");
    }
}
