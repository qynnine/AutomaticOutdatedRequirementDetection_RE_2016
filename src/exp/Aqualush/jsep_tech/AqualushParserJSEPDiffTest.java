package exp.Aqualush.jsep_tech;

import exp.Aqualush.AqualushSetting;
import io.CorpusExtractor;
import parser.Hierarchy;
import parser.JSEPDiffParser;

/**
 * Created by niejia on 15/12/14.
 */
public class AqualushParserJSEPDiffTest {
    public static void main(String[] args) {
        CorpusExtractor corpus3 = new CorpusExtractor("Aqualush", "change3");
        CorpusExtractor corpus4 = new CorpusExtractor("Aqualush", "change4");

        JSEPDiffParser parser4 = new JSEPDiffParser("data/Aqualush/JSEP_Diff/Aqualush_change4.txt", corpus4, corpus3,
                "data/Aqualush/grouped_by_jsep_my_version", "Aqualush", "change4");

        parser4.callMethodGraphThreshold = 0.4;
//        parser4.callClassGraphThreshold = 0.2;

        parser4.process(AqualushSetting.Change4_JAR, AqualushSetting.Change3_JAR);
        String item = "ui.SetMaxLevelScrnState.acceptSettings";
        System.out.println(item + "'s answer ");
        System.out.println("---------haha-------");

        System.out.println(parser4.useCallHierarchy(item, parser4.newVersionMethodCallGraph, 2, Hierarchy.CallerAndCallee));
    }
}
