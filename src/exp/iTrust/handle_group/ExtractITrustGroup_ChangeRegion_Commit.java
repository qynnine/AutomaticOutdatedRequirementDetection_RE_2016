package exp.iTrust.handle_group;

import core.type.Granularity;
import exp.iTrust.ITrustSetting;
import group.ChangeRegionsAlgorithm;
import io.CorpusExtractor;

/**
 * Created by niejia on 16/1/17.
 */
public class ExtractITrustGroup_ChangeRegion_Commit {
    public static void main(String[] args) {

        CorpusExtractor corpus0 = new CorpusExtractor("iTrust", "change0");
        CorpusExtractor corpus1 = new CorpusExtractor("iTrust", "change1");
        CorpusExtractor corpus2 = new CorpusExtractor("iTrust", "change2");
        CorpusExtractor corpus3 = new CorpusExtractor("iTrust", "change3");
        CorpusExtractor corpus4 = new CorpusExtractor("iTrust", "change4");
        CorpusExtractor corpus5 = new CorpusExtractor("iTrust", "change5");
        CorpusExtractor corpus6 = new CorpusExtractor("iTrust", "change6");
        CorpusExtractor corpus7 = new CorpusExtractor("iTrust", "change7");
        CorpusExtractor corpus8 = new CorpusExtractor("iTrust", "change8");
        CorpusExtractor corpus9 = new CorpusExtractor("iTrust", "change9");
        CorpusExtractor corpus10 = new CorpusExtractor("iTrust", "change10");

        CorpusExtractor corpusV10 = new CorpusExtractor("iTrust", "v10");
        CorpusExtractor corpusV11 = new CorpusExtractor("iTrust", "v11");

        group("v10", "v11", ITrustSetting.v10_JAR, ITrustSetting.v11_JAR,
                ITrustSetting.MethodChangesV10_V11, Granularity.METHOD,
                corpusV10, corpusV11, ITrustSetting.iTrust_ChangeRegionPath, ITrustSetting.iTrustChangesFromJDiff_v11_v10);
//
//        group("change0", "change1", ITrustSetting.Change0_JAR, ITrustSetting.Change1_JAR,
//                ITrustSetting.MethodChanges1, Granularity.METHOD,
//                corpus0, corpus1, ITrustSetting.GroupByMethodPath);
//
//        group("change1", "change2", ITrustSetting.Change1_JAR, ITrustSetting.Change2_JAR,
//                ITrustSetting.MethodChanges2, Granularity.METHOD,
//                corpus1, corpus2, ITrustSetting.GroupByMethodPath);
//
//        group("change2", "change3", ITrustSetting.Change2_JAR, ITrustSetting.Change3_JAR,
//                ITrustSetting.MethodChanges3, Granularity.METHOD,
//                corpus2, corpus3, ITrustSetting.GroupByMethodPath);
//
//        group("change3", "change4", ITrustSetting.Change3_JAR, ITrustSetting.Change4_JAR,
//                ITrustSetting.MethodChanges4, Granularity.METHOD,
//                corpus3, corpus4, ITrustSetting.GroupByMethodPath);
//
//        group("change4", "change5", ITrustSetting.Change4_JAR, ITrustSetting.Change5_JAR,
//                ITrustSetting.MethodChanges5, Granularity.METHOD,
//                corpus4, corpus5, ITrustSetting.GroupByMethodPath);
//
//
//        group("change5", "change6", ITrustSetting.Change5_JAR, ITrustSetting.Change6_JAR,
//                ITrustSetting.MethodChanges6, Granularity.METHOD,
//                corpus5, corpus6, ITrustSetting.GroupByMethodPath);
//
//        group("change6", "change7", ITrustSetting.Change6_JAR, ITrustSetting.Change7_JAR,
//                ITrustSetting.MethodChanges7, Granularity.METHOD,
//                corpus6, corpus7, ITrustSetting.GroupByMethodPath);
//
//        group("change7", "change8", ITrustSetting.Change7_JAR, ITrustSetting.Change8_JAR,
//                ITrustSetting.MethodChanges8, Granularity.METHOD,
//                corpus7, corpus8, ITrustSetting.GroupByMethodPath);
//
//        group("change8", "change9", ITrustSetting.Change8_JAR, ITrustSetting.Change9_JAR,
//                ITrustSetting.MethodChanges9, Granularity.METHOD,
//                corpus8, corpus9, ITrustSetting.GroupByMethodPath);
//
//        group("change9", "change10", ITrustSetting.Change9_JAR, ITrustSetting.Change10_JAR,
//                ITrustSetting.MethodChanges10, Granularity.METHOD,
//                corpus9, corpus10, ITrustSetting.GroupByMethodPath);
    }

    public static void group(String oldVersion, String newVersion, String oldJar, String newJar,
                             String methodChanges, Granularity granularity, CorpusExtractor oldCorpus, CorpusExtractor newCorpus, String groupByMethodPath, String changesFromJDiff) {
        System.out.println("------------------------");
        System.out.println("Change " + oldVersion + " to " + newVersion + ":");
        ChangeRegionsAlgorithm ge = new ChangeRegionsAlgorithm(oldVersion, newVersion, oldJar, newJar, methodChanges, granularity, oldCorpus, newCorpus, groupByMethodPath, changesFromJDiff);
        ge.process();
    }
}
