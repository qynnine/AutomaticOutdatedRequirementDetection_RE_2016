package exp.Aqualush.all_changes;

import core.algo.JSS2015_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import exp.Aqualush.AqualushSetting;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by niejia on 16/1/4.
 */
public class Aqualush_VSM_ChangeRegion_All_Changes {

    private static Set<String> changesVersion;
    private static Map<String, Result> changesResult;
    private static Map<String, TextDataset> changesTextDataset;

    public static void main(String[] args) {
        getResult();
    }

    public static Result getResult() {

        changesVersion = new HashSet<>();
        changesResult = new LinkedHashMap<>();
        changesTextDataset = new LinkedHashMap<>();


        changeRegion(AqualushSetting.Aqualush_Change4_GroupByChangeRegionPath, AqualushSetting.Aqualush_CleanedRequirement,
                AqualushSetting.AqualushOracleChange4, "Change4");

        changeRegion(AqualushSetting.Aqualush_Change5_GroupByChangeRegionPath, AqualushSetting.Aqualush_CleanedRequirement,
                AqualushSetting.AqualushOracleChange5, "Change5");

        changeRegion(AqualushSetting.Aqualush_Change7_GroupByChangeRegionPath, AqualushSetting.Aqualush_CleanedRequirement,
                AqualushSetting.AqualushOracleChange7, "Change7");

//        changeRegion(AqualushSetting.Aqualush_Change99_GroupByChangeRegionPath, AqualushSetting.Aqualush_CleanedRequirement,
//                AqualushSetting.AqualushOracleChange99, "Change99");

        int n = 20;
//        double meanAveragePrecision = (result_ir4.getAveragePrecisionByRanklistAtCutN(n) + result_ir5.getAveragePrecisionByRanklistAtCutN(n) +
//                result_ir7.getAveragePrecisionByRanklistAtCutN(n)) / 3.0;
//        System.out.println("Final MeanAveragePrecision: " + meanAveragePrecision);

        MergeResult mergeResult = new MergeResult(changesVersion, changesResult,changesTextDataset);
        return mergeResult.getMergedResult();
    }


    public static Result changeRegion(String code, String req, String oracle, String change) {
        System.out.println("----------" + change + "----------");
        TextDataset textDataset = new TextDataset(code,
                req, oracle);

        Result result_ir = IR.compute(textDataset, IRModelConst.VSM_ALL, new JSS2015_CSTI(), change);
//        Result result_ir = IR.compute(textDataset, IRModelConst.VSM_Weighting, new IRValueVoting_CSTI(), change);
        result_ir.showMatrix();
        result_ir.showAveragePrecisionByRanklist();
        System.out.println("--------------------");
        changesVersion.add(change);
        changesResult.put(change, result_ir);
        changesTextDataset.put(change, textDataset);
        changesTextDataset.put(change, textDataset);
        return result_ir;
    }
}
