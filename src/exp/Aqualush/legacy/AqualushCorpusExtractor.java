package exp.Aqualush.legacy;

import preprocess.ExportClassIdentifier;
import preprocess.ExportMethodBody;
import preprocess.ExportMethodIdentifier;

/**
 * Created by niejia on 15/11/9.
 */
public class AqualushCorpusExtractor {

    public static void main(String[] args) {

        String version = "last";
        ExportClassIdentifier classIdentifier =
                new ExportClassIdentifier("data/Aqualush/code/" + version, "data/Aqualush/ExtractedCorpus/ClassIdentifier/" + version);

        ExportMethodIdentifier methodIdentifier =
                new ExportMethodIdentifier("data/Aqualush/code/" + version, "data/Aqualush/ExtractedCorpus/MethodIdentifier/" + version);


        ExportMethodBody methodBody =
                new ExportMethodBody("data/Aqualush/code/" + version, "data/Aqualush/ExtractedCorpus/MethodBody/" + version);

    }

}
