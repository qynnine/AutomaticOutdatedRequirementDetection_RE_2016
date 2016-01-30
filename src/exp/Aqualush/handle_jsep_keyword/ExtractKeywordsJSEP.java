package exp.Aqualush.handle_jsep_keyword;

import io.KeyWordsReader;

/**
 * Created by niejia on 15/11/30.
 */
public class ExtractKeywordsJSEP {
    public static void main(String[] args) {
        String[] changes = {"change4", "change5", "change7", "change99"};

        for (String vNumber : changes) {
            KeyWordsReader.cleanFormat("Aqualush", vNumber);
        }
    }
}
