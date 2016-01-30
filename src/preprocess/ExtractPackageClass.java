package preprocess;

import parser.JavaStructParser;
import parser.ParserType;

import java.io.File;

/**
 * Created by niejia on 15/12/2.
 */
public class ExtractPackageClass {
    public ExtractPackageClass(String srcDir, String outputPath) {
        File srcFile = new File(srcDir);
        File outDir = new File(outputPath);
        if (outDir.exists()) {
            for (File f : outDir.listFiles()) {
                f.delete();
            }
        } else {
            outDir.mkdir();
        }

        System.out.println("Extracting package class...");
        if (srcFile.isDirectory()) {
            for (File f : srcFile.listFiles()) {
                if (f.getName().endsWith(".java")) {
                    JavaStructParser structParser = new JavaStructParser(f.getPath(), outputPath, ParserType.PackageClass);
                }
            }
        }
        System.out.println("Finished.");
    }
}
