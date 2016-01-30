package exp.iTrust.legacy;

import preprocess.ExportClassIdentifier;
import preprocess.ExportMethodBody;
import preprocess.ExportMethodIdentifier;

/**
 * Created by niejia on 15/11/10.
 */
public class iTrustCorpusExtractor {

    public static void main(String[] args) {
        String version = "v11";
        ExportClassIdentifier classIdentifier =
                new ExportClassIdentifier("data/iTrust/code/" + version, "data/iTrust/ExtractedCorpus/ClassIdentifier/" + version);

        ExportMethodIdentifier methodIdentifier =
                new ExportMethodIdentifier("data/iTrust/code/" + version, "data/iTrust/ExtractedCorpus/MethodIdentifier/" + version);


        ExportMethodBody methodBody =
                new ExportMethodBody("data/iTrust/code/" + version, "data/iTrust/ExtractedCorpus/MethodBody/" + version);
    }
}
