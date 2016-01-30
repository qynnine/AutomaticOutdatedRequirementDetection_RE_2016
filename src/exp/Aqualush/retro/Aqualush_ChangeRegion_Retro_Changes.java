package exp.Aqualush.retro;

import core.algo.Retro_retrieval;
import core.dataset.TextDataset;
import core.metrics.Result;
import exp.Aqualush.AqualushSetting;

/**
 * Created by niejia on 16/1/21.
 */
public class Aqualush_ChangeRegion_Retro_Changes {

    public static void main(String[] args) {

        String retro_out_path_4 = "data/Aqualush/retro/change_region/ch4.txt";
        String retro_out_path_5 = "data/Aqualush/retro/change_region/ch5.txt";
        String retro_out_path_7 = "data/Aqualush/retro/change_region/ch7.txt";

        change_region_retro(AqualushSetting.Aqualush_Change4_GroupedByJSEP,
                AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange4, retro_out_path_4, "Change4");
        change_region_retro(AqualushSetting.Aqualush_Change5_GroupedByJSEP,
                AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange5, retro_out_path_5, "Change5");
        change_region_retro(AqualushSetting.Aqualush_Change7_GroupedByJSEP,
                AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange7, retro_out_path_7, "Change7");
    }


    public static void change_region_retro(String code, String req, String oracle, String retro_out_path, String version) {
        System.out.println("----------" + version + "----------");
        TextDataset textDataset = new TextDataset(code,
                req, oracle);

        Result result = Retro_retrieval.computeRetroResult(textDataset, retro_out_path, version, 337);
        result.showMatrix();
        result.showAveragePrecisionByRanklist();
        System.out.println("--------------------");
    }
}
