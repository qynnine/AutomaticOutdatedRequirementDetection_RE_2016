package exp.iTrust.commits.change7;

import core.algo.Retro_retrieval;
import core.dataset.TextDataset;
import core.metrics.Result;
import exp.iTrust.ITrustSetting;

/**
 * Created by niejia on 15/12/31.
 */
public class iTrust_JSEP_Retro_Change7 {
    public static void main(String[] args) {
        TextDataset textDataset = new TextDataset(ITrustSetting.iTrust_Change7_GroupedByJSEP_MyVersion,
                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChange7);

        String retro_out_path = "data/iTrust/retro_out/RetroDemo.txt";
        Result result = Retro_retrieval.computeRetroResult(textDataset, retro_out_path, "Change7", 39);
        result.showMatrix();
        result.showAveragePrecisionByRanklist();
//        VisualCurve curve = new VisualCurve();
//        curve.addLine(result);
//        curve.showChart();
    }
}
