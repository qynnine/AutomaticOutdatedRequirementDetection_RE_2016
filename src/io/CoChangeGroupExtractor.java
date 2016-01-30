package io;

import core.type.Granularity;
import group.CoChangeRegionFetcher;
import group.GroupedByMethodRegion;
import parser.JDiffChangesParser;
import relation.CallRelationGraph;
import relation.RelationInfo;

/**
 * Created by niejia on 15/11/29.
 */
public class CoChangeGroupExtractor {

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


    public CoChangeGroupExtractor(String oldVersionName, String newVersionName, String oldVersionJar, String newVersionJar, String changesPath, Granularity granularity, CorpusExtractor oldCorpus, CorpusExtractor newCorpus, String exportGroupPath, String changesFromJDiff) {
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
        RelationInfo relationInfo = new RelationInfo(oldVersionJar, newVersionJar,
                changesPath, granularity, false);
        relationInfo.setPruning(0.2);

        RelationInfo completedRelationInfo = new RelationInfo(oldVersionJar, newVersionJar,
                changesPath, granularity, true);
//        completedRelationInfo.setPruning(0.35);
        completedRelationInfo.setPruning(0.3);

        CallRelationGraph changedCallGraph = new CallRelationGraph(relationInfo);
        CallRelationGraph completedCallGraph = new CallRelationGraph(completedRelationInfo);

        RelationInfo oldRelationInfo = new RelationInfo(oldVersionJar,granularity,false);
        RelationInfo newRelationInfo = new RelationInfo(newVersionJar,granularity,false);

        CallRelationGraph oldCallGraph = new CallRelationGraph(oldRelationInfo);
        CallRelationGraph newCallGraph = new CallRelationGraph(newRelationInfo);

        ChangeRegionFetcher fetcher = new CoChangeRegionFetcher(changesPath, changedCallGraph,completedCallGraph);
//        fetcher.showChangeRegion();
//        fetcher.showChangeRegionWithoutModifiedPart();

        String exportPath = "/groups_from_release_cochange";
        GroupedByMethodRegion methodRegion = new GroupedByMethodRegion(newCorpus, oldCorpus, exportGroupPath, fetcher, newVersionName, oldVersionName, newCallGraph, oldCallGraph, completedCallGraph, exportPath, jDiffChangesParser);
        methodRegion.export();

        methodRegion.showChangeRegion();
    }
}
