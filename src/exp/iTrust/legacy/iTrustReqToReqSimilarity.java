package exp.iTrust.legacy;

import core.algo.None_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import util.AppConfigure;

/**
 * Created by niejia on 15/11/21.
 */
public class iTrustReqToReqSimilarity {
    public static void main(String[] args) {
        TextDataset textDataset_asOne = new TextDataset(AppConfigure.iTrust_CleanedRequirement,
                AppConfigure.iTrust_CleanedRequirement, AppConfigure.iTrustOracle);

        Result irResult = IR.compute(textDataset_asOne, IRModelConst.VSM, new None_CSTI(), "iTrust");

        irResult.showMatrix();
    }
}
