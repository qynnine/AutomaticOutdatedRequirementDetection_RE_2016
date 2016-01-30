package io;

import preprocess.ArtifactPreprocessor;
import util._;

import java.io.File;

/**
 * Created by niejia on 15/11/1.
 */

/**
 * UC1 -> UC1.txt
 */
public class RequirementDealWithPostfix {

    public static void process(String requirementDir) {
        File dirFile = new File(requirementDir);
        if (!dirFile.exists()) {
            _.abort("Directory doesn't exist");
        }

        if (!dirFile.isDirectory()) {
            _.abort("Path should be a directory");
        }

        for (File f : dirFile.listFiles()) {
            String fileName = f.getName();
            if (fileName.startsWith("SRS")) {
                _.writeFile(ArtifactPreprocessor.handlePureTextFile(_.readFile(f.getPath())), "data/Aqualush/requirements" + "/" + fileName + ".txt");
            }
        }
    }

    public static void main(String[] args) {
//        String requirementDir = "data/iTrust/uc";
//        RequirementDealWithPostfix.process(requirementDir);

        String requirementDir = "data/Aqualush/uc";
        RequirementDealWithPostfix.process(requirementDir);
    }
}
