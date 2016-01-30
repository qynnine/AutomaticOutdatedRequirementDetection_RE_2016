package exp.iTrust.handle_corpus;

import io.CorpusExtractor;

/**
 * Created by niejia on 15/12/9.
 */
public class ExtractITrustCorpus {
    public static void main(String[] args) {
        String[] version = {"v10", "v11",
                "change0","change1","change2","change3",
                "change4", "change5", "change6", "change7", "change8", "change9", "change10"};

        for (String vNumber : version) {
            String projectName = "iTrust";
            String versionName = vNumber;
            CorpusExtractor ce = new CorpusExtractor(projectName, versionName);
            ce.process();
        }
    }
}
