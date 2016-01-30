package exp.iTrust;

import util._;

import java.io.File;

/**
 * Created by niejia on 15/12/19.
 */
public class RemoveRequirementsTXT {
    public static void main(String[] args) {
        File dir = new File(ITrustSetting.iTrust_CleanedRequirement);
        for (File f : dir.listFiles()) {
            System.out.println(f.getName());
            _.writeFile(_.readFile(f.getPath()), "data/cleaned_requirements/" + f.getName().split("\\.")[0]);
        }
    }
}
