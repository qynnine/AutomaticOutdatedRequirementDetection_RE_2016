package exp.Aqualush.retro.my_keywords;

import core.algo.Retro_retrieval;
import core.dataset.TextDataset;
import core.metrics.Result;
import exp.Aqualush.AqualushSetting;

/**
 * Created by niejia on 16/1/5.
 */
public class Aqualush_JSEP_Retro_MyKeyWords_Change7 {
    public static void main(String[] args) {
        TextDataset textDataset = new TextDataset(AqualushSetting.Aqualush_Change7_GroupedByJSEP_MyVersion,
                AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange7);

        String retro_out_path = "data/Aqualush/retro/my_keywords/ch7.txt";
        Result result = Retro_retrieval.computeRetroResult(textDataset, retro_out_path, "Change7", 337);
        result.showMatrix();
        result.showAveragePrecisionByRanklist();
    }
}
