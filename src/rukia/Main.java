package rukia;

import rukia.type.CallRelationsStaticAnalyser;

/**
 * Created by niejia on 16/1/28.
 */
public class Main {

    public static void main(String[] args) {
        String path = "data/AccessDAO.java";
        CallRelationsStaticAnalyser callRelationsStaticAnalyser = new CallRelationsStaticAnalyser(path);
        System.out.println("Hello");
    }
}
