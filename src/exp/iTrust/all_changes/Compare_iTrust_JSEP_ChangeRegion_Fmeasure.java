package exp.iTrust.all_changes;

import core.metrics.Result;
import visual.VisualFmeasureCutCurve_Commit;
import visual.VisualPrecisionRecallCurve_AtCutN;

/**
 * Created by niejia on 16/1/18.
 */
public class Compare_iTrust_JSEP_ChangeRegion_Fmeasure {
    public static void main(String[] args) {


        Result result_jsep = iTrust_VSM_JSEP_All_Changes.getResult();
        Result result_changeRegion = iTrust_VSM_ChangeRegion_Release_All_Changes.getResult();

        result_jsep.showWilcoxonDataCol_fmeasure_atCutN("x",15);
        result_changeRegion.showWilcoxonDataCol_fmeasure_atCutN("y",15);
        result_jsep.showMeanAveragePrecisionByQuery();
        result_jsep.showAveragePrecisionByRanklist();


        result_changeRegion.showMeanAveragePrecisionByQuery();
        result_changeRegion.showAveragePrecisionByRanklist();



        VisualPrecisionRecallCurve_AtCutN precisionRecallCurve = new VisualPrecisionRecallCurve_AtCutN();
        precisionRecallCurve.addLine(result_jsep);
        precisionRecallCurve.addLine(result_changeRegion);
        precisionRecallCurve.showChart();



        VisualFmeasureCutCurve_Commit fmeasureCutCurve = new VisualFmeasureCutCurve_Commit();
        fmeasureCutCurve.addLine(result_jsep);
        fmeasureCutCurve.addLine(result_changeRegion);
        fmeasureCutCurve.showChart();
    }
}
