package exp.Aqualush.legacy;

import core.algo.None_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import util.AppConfigure;

/**
 * Created by niejia on 15/11/21.
 */
public class AqualushReqToReqSimilarity {
    public static void main(String[] args) {
        TextDataset textDataset = new TextDataset(AppConfigure.Aqualush_CleanedRequirement,
                AppConfigure.Aqualush_CleanedRequirement, AppConfigure.AqualushOracle);

        Result result_ir = IR.compute(textDataset, IRModelConst.VSM, new None_CSTI(), "Aqualush");
        result_ir.showMatrix();
    }
}
