package exp.iTrust.jsep_tech;

import util._;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by niejia on 15/12/30.
 */
public class DivideITrustClassForEveryChange {
    // the classes belong to one change

    public Map<String, List<String>> classForChange;


    public DivideITrustClassForEveryChange(String oracle, String changes) {
        classForChange = new HashMap<>();

        String input = _.readFile(oracle);
        String lines[] = input.split("\n");

        for (int i = 0; i < lines.length; i++) {
            String tokens[] = lines[i].split(" ");

            List<String> classes = new ArrayList<>();

            for (int j = 1; j < tokens.length; j++) {
                classes.add(tokens[j]);
            }

            classForChange.put(tokens[0], classes);
        }

//        System.out.println(classForChange);

        File changesFile = new File(changes);

        for (int i = 1; i <= 10; i++) {
            File f = new File(changesFile.getParentFile().getPath() + "/" + "change" + i);
            if (f.exists()) {
                for (File subF:f.listFiles()) {
                    subF.delete();
                }
            } else {
                f.mkdir();
            }

            for (File classFile : changesFile.listFiles()) {
                String fileName = classFile.getName();
                String[] tokens = fileName.split("\\.");
                String className = tokens[tokens.length - 2];
                System.out.println(" className = " + className );

                    List<String> containedClass = classForChange.get("change" + i);

                    if (containedClass.contains(className)) {
                        _.writeFile(_.readFile(classFile.getPath()), f.getPath() + "/" + className+".txt");
                    }
            }
        }
    }


    public static void main(String[] args) {
        String filePath = "data/iTrust/divide_group_to_change/class_for_change.txt";
        String changesPath = "data/iTrust/grouped_by_jsep_my_version/changeV11_V10";
        String changesNotPreprocessedPath = "data/iTrust/grouped_by_jsep_my_version_not_preprocessed/changeV11_V10";
        DivideITrustClassForEveryChange dgfc = new DivideITrustClassForEveryChange(filePath, changesPath);
        DivideITrustClassForEveryChange dgfc1 = new DivideITrustClassForEveryChange(filePath, changesNotPreprocessedPath);
    }

}
