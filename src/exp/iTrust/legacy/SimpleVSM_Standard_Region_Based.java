package exp.iTrust.legacy;

import callGraph.JCallGraph;
import core.algo.IRValueVoting_CSTI;
import core.dataset.TextDataset;
import core.ir.IR;
import core.ir.IRModelConst;
import core.metrics.Result;
import core.type.Granularity;
import group.CoChangeRegionFetcher;
import group.GroupedByClassRegion;
import relation.CallRelationGraph;
import relation.RelationInfo;
import util.AppConfigure;

import java.io.File;

/**
 * Created by niejia on 15/11/4.
 */
public class SimpleVSM_Standard_Region_Based {

    public static void main(String[] args) {

        String changedClassPath = "data/iTrust/code_changes_grouped_by_class";

        JCallGraph oldCallGraph = new JCallGraph(AppConfigure.iTrust_v10_jarFile);
        JCallGraph newCallGraph = new JCallGraph(AppConfigure.iTrust_v11_jarFile);

        StringBuffer sb = new StringBuffer();

        for (double threashold = 0.00; threashold < 1.01; threashold += 0.05) {
            RelationInfo relationInfo = new RelationInfo(AppConfigure.iTrust_v10_jarFile, AppConfigure.iTrust_v11_jarFile, changedClassPath, Granularity.CLASS, false);
            relationInfo.setPruning(threashold);
            CallRelationGraph callGraph = new CallRelationGraph(relationInfo);

            System.out.println(" threashold = " + threashold );
            CoChangeRegionFetcher fetcher = new CoChangeRegionFetcher(changedClassPath, callGraph,callGraph);

            String importDir = "data/iTrust/keywords";
            String exportDir = "data/iTrust/code_changes_grouped_by_region";

            File dir = new File(exportDir);
            for (File file: dir.listFiles()) if (!file.isDirectory()) file.delete();
//
        GroupedByClassRegion.export(importDir, exportDir, fetcher.getChangeRegion());
//
        TextDataset textDataset = new TextDataset(AppConfigure.iTrust_RegionGroupedByClass,
                AppConfigure.iTrust_CleanedRequirement, AppConfigure.iTrustOracle);
//
        Result result_ir = IR.compute(textDataset, IRModelConst.VSM, new IRValueVoting_CSTI(), "iTrust");


//        result_ir.showMatrix();
        sb.append("-----------------------------");
            sb.append("\n");
        sb.append("Threashold @" + threashold);
            sb.append("\n");
        sb.append(result_ir.showAveragePrecisionByRanklist());
            sb.append("\n");
//            sb.append(result_ir.showMeanAveragePrecisionByQuery());
//            sb.append("\n");

//        VisualCurve curve = new VisualCurve();
//        curve.addLine(result_ir);
//        curve.showChart();
        }

        System.out.println(sb.toString());
    }
}
