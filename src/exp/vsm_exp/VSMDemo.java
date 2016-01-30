package exp.vsm_exp;

import core.algo.None_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import exp.iTrust.ITrustSetting;

/**
 * Created by niejia on 15/12/15.
 */
public class VSMDemo {
    public static void main(String[] args) {
        TextDataset textDataset = new TextDataset(ITrustSetting.iTrust_CleanedRequirement,ITrustSetting.iTrust_Change7_GroupedByJSEP,
                ITrustSetting.iTrustOracleChange8);

        Result result_ir = IR.compute(textDataset, IRModelConst.VSM, new None_CSTI(), "Change9");
        result_ir.showMatrix();

    }
}
