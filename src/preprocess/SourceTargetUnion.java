package preprocess;

import core.type.Granularity;
import document.ArtifactsCollection;
import document.SimilarityMatrix;
import io.ArtifactsReader;
import io.SqliteIO;
import parser.CodeParser;
import parser.RequirementParser;
import util._;

import java.io.File;
import java.util.Set;

/**
 * Created by niejia on 15/2/22.
 * Extract Dataset union (Requirements, code) and RTM.
 */
public class SourceTargetUnion {

    public SourceTargetUnion(String requirementPath, String codePath, String rtmDBFilePath, String reqExportPath,String codeExportPath
    , String rtmFileName) {
        SimilarityMatrix rtm_class = SqliteIO.readRTMFromDB(rtmDBFilePath, Granularity.CLASS, rtmFileName);

        int rtmUCNum = rtm_class.sourceArtifactsIds().size();
        int rtmClassNum = rtm_class.targetArtifactsIds().size();

        System.out.printf("RTM contains %d uc case.\n", rtmUCNum);
        System.out.printf("RTM contains %d classes.\n", rtmClassNum);

        CodeParser cp = new CodeParser(codePath,codeExportPath);
        cp.parse();

        RequirementParser rp = new RequirementParser(requirementPath, reqExportPath);
        rp.parser();

        // Delete Requirements and code which are not mentioned in RTM,
        deleteFilesNotInRTM(reqExportPath, rtm_class.sourceArtifactsIds(), "uc case",".txt");
        deleteFilesNotInRTM(codeExportPath, rtm_class.targetArtifactsIds(), "class",".java");

        // Delete trace links in rtm that source/target artifact is not in src/uc.
        File rtmDB = new File(rtmDBFilePath);
        String rtmClassPath = rtmDB.getParent() + "/" + rtmFileName + ".txt";
//
        ArtifactsCollection ucCollection = ArtifactsReader.getCollections(requirementPath, ".txt");
        ArtifactsCollection classCollection = ArtifactsReader.getCollections(codeExportPath, ".java");

        deleteTraceLinkNotFound(rtmClassPath, ucCollection.keySet(), classCollection.keySet(), Granularity.CLASS);
    }

    private void deleteTraceLinkNotFound(String rtmClassPath, Set<String> ucSet, Set<String> classSet, Granularity granularity) {
        String input = _.readFile(rtmClassPath);
        String[] lines = input.split("\n");

        StringBuilder sb = new StringBuilder();

        int deleteLinksNum = 0;
        int allLinksNum = 0;
        for (String line : lines) {
            allLinksNum++;

            String[] tokens = line.split(" ");
            String source = tokens[0];
            String target = tokens[1];

            if (ucSet.contains(source) && classSet.contains(target)) {
                sb.append(line);
                sb.append("\n");
            } else {
                deleteLinksNum++;
                System.out.println("Delete trace link " + line);
            }
        }

        System.out.printf("Delete %d of %d trace links in %s RTM.\n", deleteLinksNum, allLinksNum, granularity);
        _.writeFile(sb.toString(), rtmClassPath);
    }

    private void deleteFilesNotInRTM(String dirPath, Set<String> rtmSet, String fileType, String extendName) {
        File dir = new File(dirPath);
        int deleteFileNum = 0;
        int allFileNum = 0;
        for (File f : dir.listFiles()) {
            String id = f.getName().split(extendName)[0];
            // warning!! jsp文件名 存在“-”字符的编码问题
            id = id.replace("‐", "-");
            allFileNum++;
            if (!rtmSet.contains(id)) {
                deleteFileNum++;
                f.delete();
//                System.out.printf("Delete %s %s\n", fileType, f.getName());
            }
        }
        System.out.printf("Delete %d of %d %s files which are not in RTM.\n", deleteFileNum, allFileNum, fileType);
    }
}
