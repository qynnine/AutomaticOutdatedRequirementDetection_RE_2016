package exp.iTrust.retro;

import core.algo.Retro_retrieval;
import core.dataset.TextDataset;
import core.metrics.Result;
import exp.iTrust.ITrustSetting;

/**
 * Created by niejia on 16/1/6.
 */
public class ITrust_JSEP_Retro_AuthorKeyWords_Changes {
    public static void main(String[] args) {

        String retro_out_path_1 = "data/iTrust/retro/author_keywords/ch1.txt";
        String retro_out_path_2 = "data/iTrust/retro/author_keywords/ch2.txt";
        String retro_out_path_3 = "data/iTrust/retro/author_keywords/ch3.txt";
        String retro_out_path_4 = "data/iTrust/retro/author_keywords/ch4.txt";
        String retro_out_path_5 = "data/iTrust/retro/author_keywords/ch5.txt";
        String retro_out_path_6 = "data/iTrust/retro/author_keywords/ch6.txt";
        String retro_out_path_7 = "data/iTrust/retro/author_keywords/ch7.txt";
        String retro_out_path_10 = "data/iTrust/retro/author_keywords/ch10.txt";

        jsep_retro_myversion(ITrustSetting.iTrust_Change1_GroupedByJSEP,
                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChange1, retro_out_path_1, "Change1");

        jsep_retro_myversion(ITrustSetting.iTrust_Change2_GroupedByJSEP,
                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChange2, retro_out_path_2, "Change2");
        jsep_retro_myversion(ITrustSetting.iTrust_Change3_GroupedByJSEP,
                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChange3, retro_out_path_3, "Change3");
        jsep_retro_myversion(ITrustSetting.iTrust_Change4_GroupedByJSEP,
                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChange4, retro_out_path_4, "Change4");
        jsep_retro_myversion(ITrustSetting.iTrust_Change5_GroupedByJSEP,
                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChange5, retro_out_path_5, "Change5");
        jsep_retro_myversion(ITrustSetting.iTrust_Change6_GroupedByJSEP,
                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChange6, retro_out_path_6, "Change6");
        jsep_retro_myversion(ITrustSetting.iTrust_Change7_GroupedByJSEP,
                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChange7, retro_out_path_7, "Change7");
        jsep_retro_myversion(ITrustSetting.iTrust_Change10_GroupedByJSEP,
                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChange10, retro_out_path_10, "Change10");
    }


    public static void jsep_retro_myversion(String code, String req, String oracle, String retro_out_path, String version) {
        System.out.println("----------" + version + "----------");
        TextDataset textDataset = new TextDataset(code,
                req, oracle);

        Result result = Retro_retrieval.computeRetroResult(textDataset, retro_out_path, version, 39);
        result.showMatrix();
        result.showAveragePrecisionByRanklist();
        System.out.println("--------------------");
    }
}
