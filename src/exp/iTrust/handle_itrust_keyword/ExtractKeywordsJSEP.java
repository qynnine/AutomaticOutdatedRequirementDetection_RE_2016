package exp.iTrust.handle_itrust_keyword;

import io.KeyWordsReader;

/**
 * Created by niejia on 15/12/9.
 */
public class ExtractKeywordsJSEP {
    public static void main(String[] args) {
        String[] changes = {"v11"};

        for (String vNumber : changes) {
            KeyWordsReader.cleanFormat("iTrust", vNumber);
        }
    }
}
