package parser;

import exp.Aqualush.AqualushSetting;
import io.CorpusExtractor;
import org.junit.Test;

public class JSEPDiffParserTest {


    @Test
    public void testUseCallHierarchy() throws Exception {
        CorpusExtractor oldCorpus = new CorpusExtractor("Aqualush", "change3");
        CorpusExtractor newCorpus = new CorpusExtractor("Aqualush", "change4");

        JSEPDiffParser parser = new JSEPDiffParser("data/Aqualush/JSEP_Diff/Aqualush_change4.txt", newCorpus, oldCorpus,
                "data/Aqualush/grouped_by_jsep_my_version", "Aqualush", "change4");


        parser.process(AqualushSetting.Change4_JAR, AqualushSetting.Change3_JAR);
        System.out.println(parser.useCallHierarchy("ui.SetLevelScrnState.getItems", parser.newVersionMethodCallGraph, 1, Hierarchy.Callee));
    }
}