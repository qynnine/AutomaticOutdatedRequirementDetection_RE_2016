package exp.Aqualush.handle_context;

import context.ExtractContext;
import exp.Aqualush.AqualushSetting;

/**
 * Created by niejia on 15/12/2.
 */
public class ExtractAqualushCorpusContext {
    public static void main(String[] args) {
        ExtractContext ec3 = new
                ExtractContext(AqualushSetting.Change3_JAR, AqualushSetting.MethodIdentifierCorpusPath, AqualushSetting.MethodCorpusWithContext, "Aqualush", "change3", -1);
        ec3.process(2);

        ExtractContext ec4 = new
                ExtractContext(AqualushSetting.Change4_JAR, AqualushSetting.MethodIdentifierCorpusPath, AqualushSetting.MethodCorpusWithContext, "Aqualush", "change4", -1);
        ec4.process(2);

        ExtractContext ec5 = new
                ExtractContext(AqualushSetting.Change5_JAR, AqualushSetting.MethodIdentifierCorpusPath, AqualushSetting.MethodCorpusWithContext, "Aqualush", "change5", -1);
        ec5.process(2);

        ExtractContext ec6 = new
                ExtractContext(AqualushSetting.Change6_JAR, AqualushSetting.MethodIdentifierCorpusPath, AqualushSetting.MethodCorpusWithContext, "Aqualush", "change6", -1);
        ec6.process(2);

        ExtractContext ec7 = new
                ExtractContext(AqualushSetting.Change7_JAR, AqualushSetting.MethodIdentifierCorpusPath, AqualushSetting.MethodCorpusWithContext, "Aqualush", "change7", -1);
        ec7.process(2);
    }
}
