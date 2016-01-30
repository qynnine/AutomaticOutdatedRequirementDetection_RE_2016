package io;

import preprocess.ArtifactPreprocessor;
import util._;

import java.io.File;

/**
 * Created by niejia on 15/12/2.
 */
public class CommitReader {

    public static void process(String commitDir, String cleanedCommitDir) {
        File dirFile = new File(commitDir);
        if (!dirFile.exists()) {
            _.abort("Directory doesn't exist");
        }

        if (!dirFile.isDirectory()) {
            _.abort("Path should be a directory");
        }

        File outDir = new File(cleanedCommitDir);
        if (outDir.exists()) {
            for (File f : outDir.listFiles()) {
                f.delete();
            }
        } else {
            outDir.mkdir();
        }

        for (File f : dirFile.listFiles()) {
            String fileName = f.getName();
            if (fileName.endsWith(".txt")) {
                _.writeFile(ArtifactPreprocessor.handlePureTextFile(_.readFile(f.getPath())), outDir.getPath() + "/" + fileName);
            }
        }
    }
}
