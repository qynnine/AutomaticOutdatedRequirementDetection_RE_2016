package exp.Aqualush.retro.cochange;

import core.algo.Retro_retrieval;
import core.dataset.TextDataset;
import core.metrics.Result;
import exp.Aqualush.AqualushSetting;

/**
 * Created by niejia on 16/1/8.
 */
public class Aqualush_CochangeRegion_Retro_Change457 {
    public static void main(String[] args) {
        String retro_out_path4 = "data/Aqualush/retro/cochange_region/ch4.txt";
        String retro_out_path5 = "data/Aqualush/retro/cochange_region/ch5.txt";
        String retro_out_path7 = "data/Aqualush/retro/cochange_region/ch7.txt";
        cochange_region_group(retro_out_path4, "Change4", AqualushSetting.Aqualush_Change4_GroupedByJSEP_MyVersion, AqualushSetting.AqualushOracleChange4);
        cochange_region_group(retro_out_path5, "Change5",AqualushSetting.Aqualush_Change5_GroupedByJSEP_MyVersion, AqualushSetting.AqualushOracleChange5);
        cochange_region_group(retro_out_path7, "Change7",AqualushSetting.Aqualush_Change7_GroupedByJSEP_MyVersion, AqualushSetting.AqualushOracleChange7);
    }

    private static void cochange_region_group(String retro_out, String version, String regionPath, String oracle) {
        TextDataset textDataset = new TextDataset(regionPath,
                AqualushSetting.Aqualush_CleanedRequirement, oracle);


        Result result = Retro_retrieval.computeRetroResult(textDataset, retro_out, version, 337);

        result.showMatrix();
        result.showAveragePrecisionByRanklist();
    }


}
