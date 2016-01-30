package exp.iTrust.handle_group;

import util._;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by niejia on 15/12/29.
 */
public class DivideITrustGroupForEveryChange {

    // the methods belong to one change

   public Map<String, List<String>> methodForChange;


    public DivideITrustGroupForEveryChange(String classForChangePath) {
        methodForChange = new HashMap<>();

        String input = _.readFile(classForChangePath);
        String lines[] = input.split("END\n");

        for (int i = 0; i < lines.length; i++) {
            String tokens[] = lines[i].split("\n");

            List<String> methods = new ArrayList<>();

            for (int j = 1; j < tokens.length; j++) {
                methods.add(tokens[j]);
            }

            methodForChange.put(tokens[0], methods);
        }

    }


    public static void main(String[] args) {
        String filePath = "data/iTrust/divide_group_to_change/method_for_change.txt";
        DivideITrustGroupForEveryChange dgfc = new DivideITrustGroupForEveryChange(filePath);
        
    }


}
