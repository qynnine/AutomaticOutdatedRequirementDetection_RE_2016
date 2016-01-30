package exp.Aqualush.retro.author_keywords;

import core.algo.Retro_retrieval;
import core.dataset.TextDataset;
import core.metrics.Result;
import exp.Aqualush.AqualushSetting;

/**
 * Created by niejia on 16/1/5.
 */
public class Aqualush_JSEP_Retro_Change4 {
    public static void main(String[] args) {
        TextDataset textDataset = new TextDataset(AqualushSetting.Aqualush_Change4_GroupedByJSEP_MyVersion,
                AqualushSetting.Aqualush_CleanedRequirement, AqualushSetting.AqualushOracleChange4);

        String retro_out_path = "data/Aqualush/retro/author_keywords/ch4.txt";
        Result result = Retro_retrieval.computeRetroResult(textDataset, retro_out_path, "Change4", 337);
        result.showMatrix();
        result.showAveragePrecisionByRanklist();
    }
}
