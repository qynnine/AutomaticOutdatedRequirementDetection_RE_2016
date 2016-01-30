package io;

import preprocess.ArtifactPreprocessor;
import util._;

import java.io.File;

/**
 * Created by niejia on 15/11/1.
 */
public class KeyWordsReader {

    public static void cleanFormat(String projectName, String versionName) {
        File dirFile = new File("data/" + projectName + "/keywords/" + versionName);
        if (!dirFile.exists()) {
            _.abort("Directory doesn't exist");
        }

        if (!dirFile.isDirectory()) {
            _.abort("Path should be a directory");
        }

        File outFile = new File("data/" + projectName + "/grouped/jsep/" + versionName);
        if (outFile.exists()) {
            for (File f : outFile.listFiles()) {
                f.delete();
            }
        } else {
            outFile.mkdir();
        }
        for (File f : dirFile.listFiles()) {
            String fileName = f.getName();
            if (fileName.startsWith("kW")) {
                String splitedFileName = fileName.split("-")[1];
                _.writeFile(ArtifactPreprocessor.handleJavaFile(_.readFile(f.getPath())), outFile.getPath() + "/" + splitedFileName + ".txt");
            }
        }
    }

    public static void main(String[] args) {
//        KeyWordsReader.cleanFormat(keywordsDis, );
    }
}
