package io;

import document.SimilarityMatrix;
import document.SingleLink;
import util._;

/**
 * Created by niejia on 15/2/23.
 */
public class RTMReader {

    public static SimilarityMatrix createSimilarityMatrix(String path) {
        SimilarityMatrix sims = new SimilarityMatrix();

        if (!path.endsWith(".txt")) throw new IllegalArgumentException("not a txt file");

        String contents = _.readFile(path);
        String[] lines = contents.split("\n");
        int traceLinkNum = 0;
        for (int i = 0; i < lines.length; i++) {
            String[] tokens = lines[i].split(" ");
            SingleLink link = new SingleLink(tokens[0].trim(), tokens[1].trim(), Double.valueOf(tokens[2].trim()));
            sims.addLink(link.getSourceArtifactId(), link.getTargetArtifactId(), link.getScore());
            traceLinkNum++;
        }
        System.out.printf("Read %d trace links from rtm.\n", traceLinkNum);

        return sims;
    }
}
