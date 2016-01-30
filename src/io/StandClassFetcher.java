package io;

import parser.CodeParser;
import preprocess.ArtifactPreprocessor;
import util._;

import java.io.File;
import java.util.HashMap;

/**
 * Created by niejia on 15/11/2.
 */
public class StandClassFetcher {

    private static void process(String keywordsDir, String srcDir) {
        File keywordsDirFile = new File(keywordsDir);
        File srcDirFile = new File(srcDir);
        if (!keywordsDirFile.exists()) {
            _.abort("Directory doesn't exist");
        }

        if (!keywordsDirFile.isDirectory()) {
            _.abort("Path should be a directory");
        }

        HashMap<String, String> targetClasses = new HashMap<>();
        for (File f : keywordsDirFile.listFiles()) {
            String fileName = f.getName();
            if (fileName.startsWith("kW")) {
                String splitedFileName = fileName.split("-")[1];
                String[] tokens = splitedFileName.split("\\.");
                targetClasses.put(tokens[tokens.length - 1] + ".java", fileName);
            }
        }

        for (File f : srcDirFile.listFiles()) {
            String fileName = f.getName();
            if (targetClasses.keySet().contains(fileName)) {
                String outputFileName = targetClasses.get(fileName).split("-")[1];
                _.writeFile(ArtifactPreprocessor.handleJavaFile(_.readFile(keywordsDir + "/" + targetClasses.get(fileName))), "data/iTrust/standard_class/" + outputFileName + ".java");
            }
        }
    }

    public static void main(String[] args) {
        String keywordsDir = "data/iTrust/keywords";
        String sourceDir = "data/iTrust/v11";

        CodeParser cp = new CodeParser(sourceDir, "data/iTrust/cleaned_v11");

        cp.parse();

        StandClassFetcher.process(keywordsDir, sourceDir);
    }
}
