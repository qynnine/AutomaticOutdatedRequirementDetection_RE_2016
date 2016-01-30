package preprocess;

import org.junit.Test;
import util._;

public class CleanUpTest {

    private final String inputFile = "data/iTrust/code/v10/java/AccessDAO.java";

    @Test
    public void testChararctorClean() throws Exception {
        String input = _.readFile(inputFile);
        String afterClean = CleanUp.chararctorClean(input);
        System.out.println(" afterClean = " + afterClean );
    }

    @Test
    public void testLengthFilter() throws Exception {

    }

    @Test
    public void testTolowerCase() throws Exception {

    }
}