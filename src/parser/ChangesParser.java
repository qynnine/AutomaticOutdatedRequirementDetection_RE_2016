package parser;

import java.io.File;
import java.util.HashSet;

/**
 * Created by niejia on 15/11/4.
 */
public class ChangesParser {
    public static HashSet<String> fetchArtifacts(String changedClassPath) {
        HashSet<String> changedTarget = new HashSet<>();
        File file = new File(changedClassPath);
        for (File f : file.listFiles()) {
            if (!f.getName().startsWith(".")) {
                changedTarget.add(f.getName().split(".java")[0]);
            }
        }
        return changedTarget;
    }
}
