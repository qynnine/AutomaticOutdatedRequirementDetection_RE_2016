package preprocess;

import parser.JavaStructParser;
import parser.ParserType;

import java.io.File;

/**
 * Created by niejia on 15/11/9.
 */
public class ExportMethodIdentifier {

    public ExportMethodIdentifier(String srcDir, String outputPath) {
        File srcFile = new File(srcDir);
        File outDir = new File(outputPath);
        if (outDir.exists()) {
            for (File f : outDir.listFiles()) {
                f.delete();
            }
        } else {
            outDir.mkdir();
        }

        System.out.println("Extracting method identifer...");
        if (srcFile.isDirectory()) {
            for (File f : srcFile.listFiles()) {
                if (f.getName().endsWith(".java")) {
//                    System.out.println(f.getName());
                    JavaStructParser structParser = new JavaStructParser(f.getPath(), outputPath, ParserType.MethodIdentifier);
                }
            }
        }
        System.out.println("Finished.");
    }

    public static void main(String[] args) {
        ExportMethodIdentifier process =
                new ExportMethodIdentifier("data/iTrust/v10", "data/iTrust/ExtractedCorpus/MethodIdentifier/v10");

    }
}
