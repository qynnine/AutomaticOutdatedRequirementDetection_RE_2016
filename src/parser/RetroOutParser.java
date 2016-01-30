package parser;

import document.ArtifactsCollection;
import document.SimilarityMatrix;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;

/**
 * Created by niejia on 15/12/31.
 */
public class RetroOutParser {

    public RetroOutParser() {
    }

    public static SimilarityMatrix createSimilarity(String path, String version, int reqCount) {

        SimilarityMatrix result = new SimilarityMatrix();
        ArtifactsCollection ac = new ArtifactsCollection();
        SAXReader reader = new SAXReader();

        try {

            Document document = reader.read(new File(path));
            Element root = document.getRootElement();
            Iterator iter = root.elementIterator("high");

            while (iter.hasNext()) {
                Element e = (Element) iter.next();

                String[] tokens = (e.attribute("id")).getValue().split("-");
                String className = "";
                if (tokens.length == 2) {
                    className = tokens[1];
                } else {
                    className = tokens[0];
                }

                Iterator i = e.elementIterator("low");

                int currentScore = reqCount;
                while (i.hasNext()) {
                    Element itemEle = (Element) i.next();
                    String uc = (itemEle.attribute("id")).getValue();
                    result.addLink(className, uc, currentScore * 1.0);
                    currentScore--;
                }
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args) {
        String retro_out_path = "data/iTrust/retro_out/RetroDemo.txt";
        SimilarityMatrix sm = RetroOutParser.createSimilarity(retro_out_path, "change2", 39);
    }
}
