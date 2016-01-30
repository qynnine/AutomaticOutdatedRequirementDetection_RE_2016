package parser;

import java.io.File;

/**
 * Created by niejia on 15/2/22.
 * Extract Class and method form Java/Jsp File
 */
public class CodeParser {

    private File srcDir;
    private String exportPath;

    public CodeParser(String srcDirPath, String exportPath) {
        this.srcDir = new File(srcDirPath);
        this.exportPath = exportPath;
    }

    public void parse() {
        if (srcDir.isDirectory()) {
            for (File f : srcDir.listFiles()) {
                if (f.getName().endsWith(".java")) {
                    JavaTextParser parser = new JavaTextParser(f.getPath(), exportPath);
//                    _.writeFile(_.readFile(f.getPath()), exportPath + f.getName());
                }
            }
        }
    }
}
