package group;

import callGraph.JCallGraph;
import core.type.Granularity;
import preprocess.ArtifactPreprocessor;
import relation.CallRelationGraph;
import relation.RelationInfo;
import util.AppConfigure;
import util._;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by niejia on 15/11/4.
 */
public class GroupedByClassRegion {

    public GroupedByClassRegion() {

    }

    public static void export(String keywordsDir, String srcDir, List<HashSet<String>> changeRegion) {
        File keywordsDirFile = new File(keywordsDir);
        File srcDirFile = new File(srcDir);
        if (!keywordsDirFile.exists()) {
            _.abort("Directory doesn't exist");
        }

        if (!keywordsDirFile.isDirectory()) {
            _.abort("Path should be a directory");
        }

        HashMap<String, File> fileRouter = new HashMap<>();
        for (File f : keywordsDirFile.listFiles()) {
            String fileName = f.getName();
            if (fileName.startsWith("kW")) {
                String splitedFileName = fileName.split("-")[1];
                fileRouter.put(splitedFileName, f);
            }
        }

        System.out.println(" fileRouter = " + fileRouter );

        int i = 1;
        for (HashSet<String> region : changeRegion) {
            StringBuffer sb = new StringBuffer();
            System.out.println(region.size());
            for (String v : region) {
                String content = _.readFile(fileRouter.get(v).getPath().split("$")[0]);
                sb.append(content);
                sb.append(" ");
                System.out.println(v + " " + i);
            }
            _.writeFile(ArtifactPreprocessor.handlePureTextFile(sb.toString()), srcDir + "/Group" + i + ".java");
            i++;
        }
    }

    public static void main(String[] args) {

        String changedClassPath = "data/iTrust/code_changes_grouped_by_class";

        JCallGraph oldCallGraph = new JCallGraph(AppConfigure.iTrust_v10_jarFile);
        JCallGraph newCallGraph = new JCallGraph(AppConfigure.iTrust_v11_jarFile);

        RelationInfo relationInfo = new RelationInfo(AppConfigure.iTrust_v10_jarFile, AppConfigure.iTrust_v11_jarFile, changedClassPath, Granularity.CLASS, false);
        relationInfo.setPruning(0.2);
        CallRelationGraph callGraph = new CallRelationGraph(relationInfo);

        CoChangeRegionFetcher fetcher = new CoChangeRegionFetcher(changedClassPath, callGraph, callGraph);

        String importDir = "data/iTrust/keywords";
        String exportDir = "data/iTrust/code_changes_grouped_by_region";

        GroupedByClassRegion.export(importDir, exportDir, fetcher.getChangeRegion());
    }

}
