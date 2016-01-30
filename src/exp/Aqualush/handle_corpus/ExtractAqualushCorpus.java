package exp.Aqualush.handle_corpus;

import io.CorpusExtractor;

/**
 * Created by niejia on 15/11/29.
 */
public class ExtractAqualushCorpus {
    public static void main(String[] args) {
        int[] changes = {3, 4, 5, 6, 7,0, 99};
        for (int vNumber : changes) {
            String projectName = "Aqualush";
            String versionName = "change" + vNumber;
            CorpusExtractor ce = new CorpusExtractor(projectName, versionName);
            ce.process();
        }
    }
}
