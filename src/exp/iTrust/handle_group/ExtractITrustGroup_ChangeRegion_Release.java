package exp.iTrust.handle_group;

import core.type.Granularity;
import exp.iTrust.ITrustSetting;
import io.CorpusExtractor;
import group.ChangeRegionsAlgorithm;

/**
 * Created by niejia on 16/1/4.
 */
public class ExtractITrustGroup_ChangeRegion_Release {
    public static void main(String[] args) {
        CorpusExtractor corpusV10 = new CorpusExtractor("iTrust", "v10");
        CorpusExtractor corpusV11 = new CorpusExtractor("iTrust", "v11");

        group("v10", "v11", ITrustSetting.v10_JAR, ITrustSetting.v11_JAR,
                ITrustSetting.MethodChangesV10_V11, Granularity.METHOD,
                corpusV10, corpusV11, ITrustSetting.iTrust_ChangeRegionPath, ITrustSetting.iTrustChangesFromJDiff_v11_v10);
    }

    public static void group(String oldVersion, String newVersion, String oldJar, String newJar,
                             String methodChanges, Granularity granularity, CorpusExtractor oldCorpus, CorpusExtractor newCorpus, String groupByMethodPath, String changesFromJDiff) {
        System.out.println("------------------------");
        System.out.println("Change " + oldVersion + " to " + newVersion + ":");
        ChangeRegionsAlgorithm ge = new ChangeRegionsAlgorithm(oldVersion, newVersion, oldJar, newJar, methodChanges, granularity, oldCorpus, newCorpus, groupByMethodPath, changesFromJDiff);
        ge.process();
    }
}
