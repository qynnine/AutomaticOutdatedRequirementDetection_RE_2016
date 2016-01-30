package core.dataset;

import document.ArtifactsCollection;
import document.KeywordsCollection;
import document.SimilarityMatrix;
import io.ArtifactsReader;
import io.KeywordsInfoReader;
import io.RTMReader;

import java.io.File;

/**
 * Created by niejia on 15/2/23.
 */
public class TextDataset {

    private ArtifactsCollection sourceCollection;
    private ArtifactsCollection targetCollection;
    private KeywordsCollection keywordsCollection;
    private SimilarityMatrix rtm;

    public TextDataset(String sourceDirPath, String targetDirPath, String rtmPath) {
        this.setSourceCollection(ArtifactsReader.getCollections(sourceDirPath, ".txt"));
        this.setTargetCollection(ArtifactsReader.getCollections(targetDirPath, ".txt"));
        this.setRtm(RTMReader.createSimilarityMatrix(rtmPath));

        File sourceDirFile = new File(sourceDirPath);
        String keywordsSourceInfoPath = sourceDirFile.getParentFile().getPath() + "_weighting/"+sourceDirFile.getName();
        this.keywordsCollection = KeywordsInfoReader.getCollections(keywordsSourceInfoPath, ".txt");
    }

    public ArtifactsCollection getSourceCollection() {
        return sourceCollection;
    }

    public void setSourceCollection(ArtifactsCollection sourceCollection) {
        this.sourceCollection = sourceCollection;
    }

    public ArtifactsCollection getTargetCollection() {
        return targetCollection;
    }

    public void setTargetCollection(ArtifactsCollection targetCollection) {
        this.targetCollection = targetCollection;
    }

    public SimilarityMatrix getRtm() {
        return rtm;
    }

    public void setRtm(SimilarityMatrix rtm) {
        this.rtm = rtm;
    }

    public KeywordsCollection getKeywordsCollection() {
        return keywordsCollection;
    }
}
