package io;

import document.KeywordsCollection;
import parser.KeywordsSourceInfo;

import java.io.File;

/**
 * Created by niejia on 16/1/19.
 */
public class KeywordsInfoReader {
    public static KeywordsCollection getCollections(String dirPath, String postfixName) {

        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
//            _.abort("Artifacts directory doesn't exist");
            return new KeywordsCollection();
        }

        if (!dirFile.isDirectory()) {
//            _.abort("Artifacts path should be a directory");
            return new KeywordsCollection();
        }

        KeywordsCollection collections = new KeywordsCollection();
        for (File f : dirFile.listFiles()) {
            if (f.getName().endsWith(postfixName)) {
                String id = f.getName().split(postfixName)[0];
                id = id.replace("‐", "-");

                KeywordsSourceInfo artifact = new KeywordsSourceInfo(f.getPath());
                collections.put(id, artifact);
            }
        }
        return collections;
    }
}
