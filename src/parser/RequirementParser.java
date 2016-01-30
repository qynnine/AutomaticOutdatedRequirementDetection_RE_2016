package parser;

import util._;

import java.io.File;

/**
 * Created by niejia on 15/10/19.
 */
public class RequirementParser {

    private File reqDir;
    private String exportPath;

    public RequirementParser(String importPath, String exportPath) {
        this.reqDir = new File(importPath);
        this.exportPath = exportPath;
    }

    public void parser() {
        if (reqDir.isDirectory()) {
            for (File f : reqDir.listFiles()) {
                if (f.getName().endsWith(".txt")) {
//                    System.out.println(exportPath + f.getName());
                    _.writeFile(_.readFile(f.getPath()), exportPath + f.getName());
                }
            }
        }
    }
}
