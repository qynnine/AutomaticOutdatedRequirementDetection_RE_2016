package preprocess;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by niejia on 15/12/21.
 */
public class SentenceSplitter {

    private SentenceSplitter() {}

    private static final String DELIM = " .,:;'\"[]{})(-_=+~!@#$%^&*<>\n\t\r1234567890";
    private static final String SPLIT_REGEX = "[A-Z][a-z]+|[a-z]+|[A-Z]+";

    /**
     * Sentence splitter process input in such a way, that any token with camel cases are
     * split to separate terms.
     * @param input text to process
     * @return
     */
    public static String process(String input) {
        StringTokenizer st = new StringTokenizer(input, DELIM);
        StringBuilder result = new StringBuilder();
        String space = " ";

        while (st.hasMoreTokens()) {
            String tok = st.nextToken();
            Pattern p = Pattern.compile(SPLIT_REGEX);
            Matcher m = p.matcher(tok);
            boolean found = m.find();
            while (found) {
                String subStringFound = m.group();
                if (1 < subStringFound.length()) {
                    result.append(subStringFound + space);
                }
                found = m.find();
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        String s = "/** \n" +
                " * Used for managing the Reason Codes.\n" +
                " * DAO stands for Database Access Object. All DAOs are intended to be reflections of the database, that is,\n" +
                " * one DAO per table in the database (most of the time). For more complex sets of queries, extra DAOs are\n" +
                " * added. DAOs can assume that all data has been validated and is correct.\n" +
                " * DAOs should never have setters or any other parameter to the constructor than a factory. All DAOs should be\n" +
                " * accessed by DAOFactory (@see {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC\n" +
                " * connections and/or accessing other DAOs.\n" +
                " * The Override Reason Code (ORC) is a universal product identifier used in the\n" +
                " * United States for drugs intended for human use.\n" +
                " * @see http://www.fda.gov/Drugs/InformationOnDrugs/ucm142438.htm\n" +
                " * @author Andy\n" +
                " */";


        System.out.println(process(s));
    }
}
