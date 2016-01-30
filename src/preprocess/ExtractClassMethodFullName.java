package preprocess;

import parser.JavaStructParser;
import parser.ParserType;

import java.io.File;

/**
 * Created by niejia on 15/12/3.
 */
public class ExtractClassMethodFullName {
    public ExtractClassMethodFullName(String srcDir, String outputPath) {
        File srcFile = new File(srcDir);
        File outDir = new File(outputPath);
        if (outDir.exists()) {
            for (File f : outDir.listFiles()) {
                f.delete();
            }
        } else {
            outDir.mkdir();
        }

        System.out.println("Extracting class Methods FullName...");
        if (srcFile.isDirectory()) {
            for (File f : srcFile.listFiles()) {
                if (f.getName().endsWith(".java")) {
                    JavaStructParser structParser = new JavaStructParser(f.getPath(), outputPath, ParserType.ClassMethodFullName);
                }
            }
        }
        System.out.println("Finished.");
    }
}
