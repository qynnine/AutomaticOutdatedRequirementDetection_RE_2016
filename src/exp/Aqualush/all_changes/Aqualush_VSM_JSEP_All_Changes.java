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
 * Created by niejia on 15/12/2.
 */
public class Aqualush_VSM_JSEP_All_Changes {
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

        jsep(AqualushSetting.Aqualush_Change4_GroupedByJSEP,
                AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange4, "Change4");


        jsep(AqualushSetting.Aqualush_Change5_GroupedByJSEP,
                AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange5, "Change5");
//
        jsep(AqualushSetting.Aqualush_Change7_GroupedByJSEP,
                AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange7, "Change7");
//
//        jsep(AqualushSetting.Aqualush_Change99_GroupedByJSEP,
//                AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange99, "Change99");

//        int n = 20;
//        double meanAveragePrecision = (result_ir4.getAveragePrecisionByRanklistAtCutN(n) + result_ir5.getAveragePrecisionByRanklistAtCutN(n) +
//                result_ir7.getAveragePrecisionByRanklistAtCutN(n)) / 3.0;
//        System.out.println("Final MeanAveragePrecision: " + meanAveragePrecision);

        MergeResult mergeResult = new MergeResult(changesVersion, changesResult,changesTextDataset);
        Result result_jsep_merged = mergeResult.getMergedResult();

        return result_jsep_merged;
    }

    public static Result jsep(String code, String req, String oracle, String change) {
        System.out.println("----------" + change + "----------");
        TextDataset textDataset = new TextDataset(code,
                req, oracle);

        Result result_ir = IR.compute(textDataset, IRModelConst.VSM_ALL, new JSS2015_CSTI(), change);
//        result_ir.showMatrix();
        result_ir.showAveragePrecisionByRanklist();
        changesVersion.add(change);
        changesResult.put(change, result_ir);
        changesTextDataset.put(change, textDataset);
        System.out.println("--------------------");
        return result_ir;
    }
}
