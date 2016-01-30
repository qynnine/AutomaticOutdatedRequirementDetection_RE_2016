package preprocess;

import parser.JavaStructParser;
import parser.ParserType;

import java.io.File;

/**
 * Created by niejia on 15/11/8.
 */
public class ExportMethodBody {

    public ExportMethodBody(String srcDir, String outputPath) {
        File srcFile = new File(srcDir);
        File outDir = new File(outputPath);
        if (outDir.exists()) {
            for (File f : outDir.listFiles()) {
                f.delete();
            }
        } else {
            outDir.mkdir();
        }
        System.out.println("Extracting method body...");
        if (srcFile.isDirectory()) {
            for (File f : srcFile.listFiles()) {
                if (f.getName().endsWith(".java")) {
//                    System.out.println(f.getName());
                    JavaStructParser structParser = new JavaStructParser(f.getPath(), outputPath, ParserType.MethodBody);
                }
            }
        }
        System.out.println("Finished.");
    }

    public static void main(String[] args) {
        ExportMethodBody process =
                new ExportMethodBody("data/iTrust/v10", "data/iTrust/ExtractedCorpus/MethodBody/v10");

    }

}
