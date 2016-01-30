package exp.iTrust.all_changes;

import core.algo.IRValueVoting_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import exp.Aqualush.all_changes.MergeResult;
import exp.iTrust.ITrustSetting;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by niejia on 16/1/4.
 */
public class iTrust_VSM_ChangeRegion_Release_All_Changes {

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

        changeRegion(ITrustSetting.iTrust_Change1_GroupedByMethod_FromRelease_InitialRegion,
                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChange1, "Change1");

        changeRegion(ITrustSetting.iTrust_Change2_GroupedByMethod_FromRelease_InitialRegion,
                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChange2, "Change2");

//        changeRegion(ITrustSetting.iTrust_Change3_GroupedByMethod_FromRelease_InitialRegion,
//                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChange3, "Change3");
//
//        changeRegion(ITrustSetting.iTrust_Change4_GroupedByMethod_FromRelease_InitialRegion,
//                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChange4, "Change4");
//
//        changeRegion(ITrustSetting.iTrust_Change5_GroupedByMethod_FromRelease_InitialRegion,
//                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChange5, "Change5");
//
//        changeRegion(ITrustSetting.iTrust_Change6_GroupedByMethod_FromRelease_InitialRegion,
//                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChange6, "Change6");

        changeRegion(ITrustSetting.iTrust_Change7_GroupedByMethod_FromRelease_InitialRegion,
                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChange7, "Change7");

//        changeRegion(ITrustSetting.iTrust_Change8_GroupedByMethod_FromRelease_InitialRegion,
//                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChange8, "Change8");
//
        changeRegion(ITrustSetting.iTrust_Change9_GroupedByMethod_FromRelease_InitialRegion,
                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChange9, "Change9");

//        changeRegion(ITrustSetting.iTrust_Change10_GroupedByMethod_FromRelease_InitialRegion,
//                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChange10, "Change10");
//
//        changeRegion(ITrustSetting.iTrust_Change11_GroupedByMethod_FromRelease_InitialRegion,
//                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChangeV11, "iTrust");

        MergeResult mergeResult = new MergeResult(changesVersion, changesResult,changesTextDataset);

        Result mergedResult_change_region = mergeResult.getMergedResult();
        return mergedResult_change_region;
    }


    public static void changeRegion(String code, String req, String oracle, String change) {
        System.out.println("----------" + change + "----------");
        TextDataset textDataset = new TextDataset(code,
                req, oracle);
        Result result_ir = IR.compute(textDataset, IRModelConst.VSM_ALL, new IRValueVoting_CSTI(), change);
//        Result result_ir = IR.compute(textDataset, IRModelConst.VSM_Weighting, new JSS2015_CSTI(), change);
        result_ir.showMatrix();
        result_ir.showAveragePrecisionByRanklist();
        changesVersion.add(change);
        changesResult.put(change, result_ir);
        changesTextDataset.put(change, textDataset);
        System.out.println("--------------------");
    }
}
