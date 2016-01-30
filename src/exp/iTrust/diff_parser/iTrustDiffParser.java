package exp.iTrust.diff_parser;

import exp.iTrust.ITrustSetting;
import parser.MethodBodyDiff;

/**
 * Created by niejia on 15/12/9.
 */
public class iTrustDiffParser {

    public static void main(String[] args) {
        String diffFile = ITrustSetting.Diff_ChangeV10_V11;
        MethodBodyDiff parser = new MethodBodyDiff(diffFile, "v11", "v10", "txt");
        System.out.println(parser);
        System.out.println("---------------------- ");

        String change = "change";
        String[] changes = new String[11];
        for (int i = 0; i <= 10; i++) {
            changes[i] = change + i;
        }

        for (int i = 0; i <= 9; i++) {
            diff(changes[i + 1], changes[i], ITrustSetting.Diff_Change + (i + 1) + ".txt");
        }
    }

    public static void diff(String newVersion, String oldVersion, String diffPath) {
        MethodBodyDiff parser = new MethodBodyDiff(diffPath, newVersion, oldVersion, "txt");
        System.out.println(parser);
        System.out.println("---------------------- ");
    }
}
