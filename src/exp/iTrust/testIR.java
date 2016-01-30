package exp.iTrust;

import core.algo.JSS2015_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;

/**
 * Created by niejia on 15/12/19.
 */
public class testIR {
    public static void main(String[] args) {
        TextDataset textDataset = new TextDataset("data_compare_vsm_retro/test_code",
                "data_compare_vsm_retro/test_req", ITrustSetting.iTrustOracleChange4);

        Result result_ir = IR.compute(textDataset, IRModelConst.VSM_ALL, new JSS2015_CSTI(), "Change4");
        result_ir.showMatrix();
//        result_ir.showAveragePrecisionByRanklist();
//        result_ir.showMeanAveragePrecisionByQuery();
    }
}
