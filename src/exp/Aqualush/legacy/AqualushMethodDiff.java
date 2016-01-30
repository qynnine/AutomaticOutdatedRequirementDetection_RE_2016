package exp.Aqualush.legacy;

import parser.MethodBodyDiff;

/**
 * Created by niejia on 15/11/9.
 */
public class AqualushMethodDiff {
    public static void main(String[] args) {
        String diffFile = "data/Aqualush/ExtractedCorpus/MethodBody/diff_code_origin_last.txt";
        MethodBodyDiff parser = new MethodBodyDiff(diffFile, "last", "origin", "java");
        parser.parser();

        System.out.println(parser);
//
//        String diffFile = "data/Aqualush/ExtractedCorpus/MethodBody/diff_code_version6_version2.txt";
//        MethodBodyDiff parser = new MethodBodyDiff(diffFile, "version2", "version6", "java");
//        parser.parser();
//
//        System.out.println(parser);
    }
}
