package group;

import core.type.Granularity;
import io.ChangedArtifacts;
import io.CorpusExtractor;
import parser.JDiffChangesParser;
import relation.CallRelationGraph;
import relation.RelationInfo;

/**
 * Created by niejia on 16/1/3.
 */
public class ChangeRegionsAlgorithm {
    private String oldVersionName;
    private String newVersionName;
    private String oldVersionJar;
    private String newVersionJar;
    private String changesPath;
    private Granularity granularity;
    private CorpusExtractor oldCorpus;
    private CorpusExtractor newCorpus;
    private String exportGroupPath;
    private JDiffChangesParser jDiffChangesParser;

    public ChangeRegionsAlgorithm(String oldVersionName, String newVersionName, String oldVersionJar, String newVersionJar, String changesPath, Granularity granularity, CorpusExtractor oldCorpus, CorpusExtractor newCorpus, String exportGroupPath, String changesFromJDiff) {
        this.oldVersionName = oldVersionName;
        this.newVersionName = newVersionName;
        this.oldVersionJar = oldVersionJar;
        this.newVersionJar = newVersionJar;
        this.changesPath = changesPath;
        this.granularity = granularity;
        this.oldCorpus = oldCorpus;
        this.newCorpus= newCorpus;
        this.exportGroupPath = exportGroupPath;
        this.jDiffChangesParser = new JDiffChangesParser(changesFromJDiff, newCorpus, oldCorpus);
    }

    public void process() {

        // Use changed call graph without modified artifacts
        RelationInfo relationInfoForChangedPart = new RelationInfo(false, oldVersionJar, newVersionJar,
                changesPath, granularity);

        // Use changed call graph with modified artifacts
//        RelationInfo relationInfoForChangedPart = new RelationInfo(oldVersionJar, newVersionJar,
//                changesPath, granularity, false);
//        relationInfoForInitialRegion.setPruning(0.3);

        CallRelationGraph callGraphForChangedPart = new CallRelationGraph(relationInfoForChangedPart);
        double thresholdForInitialRegion = 0.3;
        RelationInfo oldRelationInfo = new RelationInfo(oldVersionJar, granularity, false);
        oldRelationInfo.setPruning(thresholdForInitialRegion);
        RelationInfo newRelationInfo = new RelationInfo(newVersionJar, granularity, false);
        newRelationInfo.setPruning(thresholdForInitialRegion);

        CallRelationGraph oldCallGraph = new CallRelationGraph(oldRelationInfo);
        CallRelationGraph newCallGraph = new CallRelationGraph(newRelationInfo);

        ChangedArtifacts changedArtifacts = new ChangedArtifacts();
        changedArtifacts.parse(changesPath);

        InitialRegionFetcher fetcher = new InitialRegionFetcher(changedArtifacts, newCallGraph, oldCallGraph);

//        ChangedArtifactsGrouper_II grouper = new ChangedArtifactsGrouper_II(changedArtifacts, callGraphForChangedPart, fetcher.getChangeRegion(), newCallGraph, oldCallGraph);
        ChangedArtifactsGrouper grouper = new ChangedArtifactsGrouper(changedArtifacts, callGraphForChangedPart, fetcher.getChangeRegion());

        KeywordsExtracterForChangeRegion keywordsExtracter = new KeywordsExtracterForChangeRegion(changedArtifacts, fetcher.getChangeRegion(), grouper.getChangedArtifactsGroup(), newVersionName, oldVersionName, newCorpus, oldCorpus, exportGroupPath, jDiffChangesParser);
        keywordsExtracter.showFinalRegion();
    }
}
