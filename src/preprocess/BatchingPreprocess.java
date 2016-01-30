package preprocess;

import util._;

import java.io.File;

/**
 * Created by niejia on 15/2/23.
 */
public class BatchingPreprocess {
    private File ucDirPath;
    private File classDirPath;

    public BatchingPreprocess(String ucDirPath, String classDirPath) {
        this.ucDirPath = new File(ucDirPath);
        this.classDirPath = new File(classDirPath);
    }

    public void doProcess() {
        preprocessReqFiles(ucDirPath);
        preprocessJavaFiles(classDirPath);
    }

    private void preprocessJavaFiles(File dirPath) {
        if (dirPath.isDirectory()) {
            for (File f : dirPath.listFiles()) {
                if (f.getName().endsWith(".java")) {
                    _.writeFile(ArtifactPreprocessor.handleJavaFile(_.readFile(f.getPath())), f.getPath());
                }
            }
        }
    }

    public void preprocessReqFiles(File ucDirPath) {
        if (ucDirPath.isDirectory()) {
            for (File f : ucDirPath.listFiles()) {
                if (f.getName().endsWith(".txt")) {
                    _.writeFile(ArtifactPreprocessor.handlePureTextFile(_.readFile(f.getPath())), f.getPath());
                }
            }
        }
    }
}
