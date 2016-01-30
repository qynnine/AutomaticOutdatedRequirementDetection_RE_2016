package exp.iTrust.jsep_tech;

import exp.iTrust.ITrustSetting;
import io.CorpusExtractor;
import parser.JSEPDiffParser_AuthorVersion;

/**
 * Created by niejia on 15/12/15.
 */
public class iTrustParserJSEPDiff_AuthorVersion {
    public static void main(String[] args) {
        double callMethodGraphThreshold = -1.0;
        double callClassGraphThreshold = -1.0;

//        CorpusExtractor corpus0 = new CorpusExtractor("iTrust", "change0");
//        CorpusExtractor corpus1 = new CorpusExtractor("iTrust", "change1");
//        CorpusExtractor corpus2 = new CorpusExtractor("iTrust", "change2");
//        CorpusExtractor corpus3 = new CorpusExtractor("iTrust", "change3");
//        CorpusExtractor corpus4 = new CorpusExtractor("iTrust", "change4");
//        CorpusExtractor corpus5 = new CorpusExtractor("iTrust", "change5");
//        CorpusExtractor corpus6 = new CorpusExtractor("iTrust", "change6");
//        CorpusExtractor corpus7 = new CorpusExtractor("iTrust", "change7");
//        CorpusExtractor corpus8 = new CorpusExtractor("iTrust", "change8");
//        CorpusExtractor corpus9 = new CorpusExtractor("iTrust", "change9");
//        CorpusExtractor corpus10 = new CorpusExtractor("iTrust", "change10");

        CorpusExtractor corpusV10 = new CorpusExtractor("iTrust", "v10");
        CorpusExtractor corpusV11 = new CorpusExtractor("iTrust", "v11");

        jsepDiff("iTrust", "changeV11_V10", "data/iTrust/JSEP_Diff/iTrust_changeV11_V10.txt",
                corpusV11, corpusV10, ITrustSetting.v11_JAR, ITrustSetting.v10_JAR, callClassGraphThreshold, callMethodGraphThreshold);

//
//        jsepDiff("iTrust", "change1", "data/iTrust/JSEP_Diff/iTrust_change1.txt",
//                corpus1, corpus0, ITrustSetting.Change1_JAR, ITrustSetting.Change0_JAR, callClassGraphThreshold, callMethodGraphThreshold);
//
//        jsepDiff("iTrust", "change2", "data/iTrust/JSEP_Diff/iTrust_change2.txt",
//                corpus2, corpus1, ITrustSetting.Change2_JAR, ITrustSetting.Change1_JAR, callClassGraphThreshold, callMethodGraphThreshold);
//
//        jsepDiff("iTrust", "change3", "data/iTrust/JSEP_Diff/iTrust_change3.txt",
//                corpus3, corpus2, ITrustSetting.Change3_JAR, ITrustSetting.Change2_JAR, callClassGraphThreshold, callMethodGraphThreshold);
//
//        jsepDiff("iTrust", "change4", "data/iTrust/JSEP_Diff/iTrust_change4.txt",
//                corpus4, corpus3, ITrustSetting.Change4_JAR, ITrustSetting.Change3_JAR, callClassGraphThreshold, callMethodGraphThreshold);
//
//        jsepDiff("iTrust", "change5", "data/iTrust/JSEP_Diff/iTrust_change5.txt",
//                corpus5, corpus4, ITrustSetting.Change5_JAR, ITrustSetting.Change4_JAR, callClassGraphThreshold, callMethodGraphThreshold);
//
//        jsepDiff("iTrust", "change6", "data/iTrust/JSEP_Diff/iTrust_change6.txt",
//                corpus6, corpus5, ITrustSetting.Change6_JAR, ITrustSetting.Change5_JAR, callClassGraphThreshold, callMethodGraphThreshold);
//
//        jsepDiff("iTrust", "change7", "data/iTrust/JSEP_Diff/iTrust_change7.txt",
//                corpus7, corpus6, ITrustSetting.Change7_JAR, ITrustSetting.Change6_JAR, callClassGraphThreshold, callMethodGraphThreshold);
//
//        jsepDiff("iTrust", "change8", "data/iTrust/JSEP_Diff/iTrust_change8.txt",
//                corpus8, corpus7, ITrustSetting.Change8_JAR, ITrustSetting.Change7_JAR, callClassGraphThreshold, callMethodGraphThreshold);
//
//        jsepDiff("iTrust", "change9", "data/iTrust/JSEP_Diff/iTrust_change9.txt",
//                corpus9, corpus8, ITrustSetting.Change9_JAR, ITrustSetting.Change8_JAR, callClassGraphThreshold, callMethodGraphThreshold);
//
//        jsepDiff("iTrust", "change10", "data/iTrust/JSEP_Diff/iTrust_change10.txt",
//                corpus10, corpus9, ITrustSetting.Change10_JAR, ITrustSetting.Change9_JAR, callClassGraphThreshold, callMethodGraphThreshold);
    }

    public static void jsepDiff(String project, String change, String diffPath, CorpusExtractor newCorpus, CorpusExtractor oldCorpus, String newJar, String oldJar,
                                double callClassGraphThreshold, double callMethodGraphThreshold) {
        JSEPDiffParser_AuthorVersion parser = new JSEPDiffParser_AuthorVersion(diffPath, newCorpus, oldCorpus,
                "data/iTrust/grouped_by_jsep_author_version", project, change);

        parser.callClassGraphThreshold = callClassGraphThreshold;
        parser.callMethodGraphThreshold = callMethodGraphThreshold;
        parser.process(newJar, oldJar);
    }
}
