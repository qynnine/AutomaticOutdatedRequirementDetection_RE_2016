package exp.iTrust.commits.change7;

import core.algo.JSS2015_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import exp.iTrust.ITrustSetting;

/**
 * Created by niejia on 16/1/19.
 */
public class iTrust_VSM_ChangeRegion_Change7 {
    public static void main(String[] args) {
        TextDataset textDataset = new TextDataset(ITrustSetting.iTrust_Change7_GroupedByMethod_FromRelease_InitialRegion,
                ITrustSetting.iTrust_CleanedRequirement, ITrustSetting.iTrustOracleChange7);

        Result result_ir = IR.compute(textDataset, IRModelConst.VSM_ALL, new JSS2015_CSTI(), "Change7");
        result_ir.showMatrix();
        result_ir.showAveragePrecisionByRanklist();
        result_ir.showMeanAveragePrecisionByQuery();
    }
}
