package exp.Aqualush.legacy;

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
 * Created by niejia on 15/11/6.
 */
public class AqualushSimpleVSM_Standard_Region_Based {

    public static void main(String[] args) {
        String changedClassPath = "data/Aqualush/code_changes_grouped_by_class";

        JCallGraph oldCallGraph = new JCallGraph(AppConfigure.Aqualush_origin_jarFile);
        JCallGraph newCallGraph = new JCallGraph(AppConfigure.Aqualush_last_jarFile);

        StringBuffer sb = new StringBuffer();
        for (double threashold = 0.00; threashold < 1.01; threashold += 0.05) {
            RelationInfo relationInfo = new RelationInfo(AppConfigure.Aqualush_origin_jarFile, AppConfigure.Aqualush_last_jarFile, changedClassPath, Granularity.CLASS, false);
            relationInfo.setPruning(threashold);
            CallRelationGraph callGraph = new CallRelationGraph(relationInfo);
            System.out.println(" threashold = " + threashold );
            CoChangeRegionFetcher fetcher = new CoChangeRegionFetcher(changedClassPath, callGraph,callGraph);

            String importDir = "data/Aqualush/keywords";
            String exportDir = "data/Aqualush/code_changes_grouped_by_region";

            File dir = new File(exportDir);
            for (File file: dir.listFiles()) if (!file.isDirectory()) file.delete();
//
            GroupedByClassRegion.export(importDir, exportDir, fetcher.getChangeRegion());
//
            TextDataset textDataset = new TextDataset(AppConfigure.Aqualush_RegionGroupedByClass,
                    AppConfigure.Aqualush_CleanedRequirement, AppConfigure.AqualushOracle);
//
            Result result_ir = IR.compute(textDataset, IRModelConst.VSM, new IRValueVoting_CSTI(), "Aqualush");
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
