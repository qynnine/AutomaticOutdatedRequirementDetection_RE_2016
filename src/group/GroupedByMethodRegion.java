package group;

import exp.iTrust.handle_group.DivideITrustGroupForEveryChange;
import io.ChangeRegionFetcher;
import io.CorpusExtractor;
import parser.JDiffChangesParser;
import preprocess.ArtifactPreprocessor;
import relation.CallRelationGraph;
import relation.graph.CodeVertex;
import util.JavaElement;
import util._;

import java.io.File;
import java.util.*;

/**
 * Created by niejia on 15/11/8.
 */
public class GroupedByMethodRegion {

    private final String exportDir;
    private CallRelationGraph completedCallGraph;

    private List<HashSet<String>> changeRegion;
    private HashSet<String> addedArtifactList;
    private HashSet<String> removedArtifactList;

    private CorpusExtractor newCorpus;
    private CorpusExtractor oldCorpus;
    private String commitVersion;

    private CallRelationGraph newCallGraph;
    private CallRelationGraph oldCallGraph;

    private String exportPath_release;

    private String methodForChangePath = "data/iTrust/divide_group_to_change/method_for_change.txt";
    Map<String, List<String>> methodForChange;

    private List<HashSet<String>> finalRegion;
    private JDiffChangesParser jDiffChangesParser;

    public GroupedByMethodRegion(CorpusExtractor newCorpus, CorpusExtractor oldCorpus, String exportDir, ChangeRegionFetcher coChangeRegionFetcher, String newVersionName, String oldVersionName, CallRelationGraph newCallGraph, CallRelationGraph oldCallGraph, CallRelationGraph completedCallGraph, String exportPath_release, JDiffChangesParser jDiffChangesParser) {

        this.newCorpus = newCorpus;
        this.oldCorpus = oldCorpus;
        this.newCallGraph = newCallGraph;
        this.oldCallGraph = oldCallGraph;
        this.completedCallGraph = completedCallGraph;
        this.exportDir = exportDir;

        this.changeRegion = coChangeRegionFetcher.getChangeRegion();
        this.addedArtifactList = coChangeRegionFetcher.getAddedArtifactList();
        this.removedArtifactList = coChangeRegionFetcher.getRemovedArtifactList();
        this.commitVersion = newVersionName;

        this.finalRegion = new ArrayList<>();
        this.exportPath_release = exportPath_release;

        this.methodForChange = (new DivideITrustGroupForEveryChange(methodForChangePath)).methodForChange;

        this.jDiffChangesParser = jDiffChangesParser;
        // Delete current groups
        File exportFile = new File(exportDir + "/" + commitVersion);
        if (exportFile.exists()) {
            for (File file : exportFile.listFiles()) file.delete();
        } else {
            exportFile.mkdir();
        }
    }

    public void export() {
        String dirPath = (new File(exportDir)).getParentFile().getPath();
        String singleMethodExportPath = dirPath + "/for_every_single_method/" + commitVersion;
        File singleMethodFile = new File(singleMethodExportPath);
        if (singleMethodFile.exists()) {
            for(File file: singleMethodFile.listFiles()) file.delete();
        } else {
            singleMethodFile.mkdir();
        }

        String allChangesExportPath = dirPath + "/as_a_whole/" + commitVersion;
        File allChangesExportFile = new File(allChangesExportPath);
        if (allChangesExportFile.exists()) {
            for(File file: allChangesExportFile.listFiles()) file.delete();
        } else {
            allChangesExportFile.mkdir();
        }

        String singleClassExportPath = dirPath + "/for_every_single_class/" + commitVersion;
        File singleClassFile = new File(singleClassExportPath);
        if (singleClassFile.exists()) {
            for(File file: singleClassFile.listFiles()) file.delete();
        } else {
            singleClassFile.mkdir();
        }

        String groupsFromReleasePath = dirPath + exportPath_release;
//        System.out.println(" groupsFromReleasePath = " + groupsFromReleasePath );
        File groupsFromRelease = new File(groupsFromReleasePath);
        if (groupsFromRelease.exists()) {
            for(File file: groupsFromRelease.listFiles()) {
                System.out.println(file.getName());
                if (file.listFiles() != null) {
                    for (File f : file.listFiles()) {
                        f.delete();
                    }
                }
            }

        } else {
            groupsFromRelease.mkdir();
            int[] versionArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
            for (int i : versionArray) {
                File f = new File(groupsFromRelease + "/change" + i);
                f.mkdir();
            }
        }

        int regionNum = 1;

        StringBuffer wholeChange = new StringBuffer();
        HashMap<String, List<String>> methodsInClass = new HashMap<>();

        for (HashSet<String> region : changeRegion) {
            if (region.size() >= 1 ) {
                StringBuffer sb = new StringBuffer();
                HashSet<String> methodInFinalRegion = new HashSet<>();

                Set<String> containedField = new LinkedHashSet<>();

                for (String s : region) {
                    if (addedArtifactList.contains(s) || removedArtifactList.contains(s)) {
                    methodInFinalRegion.add(s);
                    String content = fetchFileContent(s);
                        sb.append(s);
//                    sb.append(extractIdentifier(s));

                        Set<String> fieldInfo = new LinkedHashSet<>();
                        if (addedArtifactList.contains(s)) {
                            fieldInfo = jDiffChangesParser.involvedChangedFieldInMethod(s, "Added");
                        } else if (removedArtifactList.contains(s)) {
                            fieldInfo = jDiffChangesParser.involvedChangedFieldInMethod(s, "Removed");
                        }
                        for (String fieldName : fieldInfo) {
                            String fieldIdentifier = JavaElement.getIdentifier(fieldName);
                            containedField.add(fieldIdentifier);
                        }

                    sb.append(getCallHierarchy(s));
                    sb.append(" ");
                    sb.append(content);
                    sb.append(" ");

                    _.writeFile(ArtifactPreprocessor.handleJavaFile(s + " " + content + " "), singleMethodExportPath + "/" + s + ".txt");

                    String className = extractClassName(s);
                    if (methodsInClass.containsKey(className)) {
                        List<String> methods = methodsInClass.get(className);
                        methods.add(s);
                        methodsInClass.put(className, methods);
                    } else {
                        List<String> methods = new ArrayList<>();
                        methods.add(s);
                        methodsInClass.put(extractClassName(s), methods);
                    }
                    }
                }
                finalRegion.add(methodInFinalRegion);
//
//                HashSet<String> nbs = extractContext(region, 1);
//                for (String nb : nbs) {
//                    sb.append(nb);
//                    sb.append(" ");
//                }

                wholeChange.append(sb.toString());
                //Maybe we need add every method's class doc into method region
                Set<String> extractedDocClass = new HashSet<>();
                for (String s : region) {
                    if (addedArtifactList.contains(s) || removedArtifactList.contains(s)) {
                        String className = extractClassName(s);
                        if (!extractedDocClass.contains(className)) {
                            String classdoc = extractClassDoc(className);
                            extractedDocClass.add(className);
//                            System.out.println(" className = " + className );
//                            System.out.println(" classdoc = " + classdoc);
                            sb.append(classdoc);
                            sb.append("\n");
                        }
                    }
                }

                //Maybe we need add fields involved in method region
                for (String fieldIdentifier : containedField) {
                    sb.append(fieldIdentifier);
                    sb.append(" ");
                }

                System.out.println(" containedField = " + containedField );
                sb.append("\n");

                if (!sb.toString().equals("")) {
//                    _.writeFile(ArtifactPreprocessor.handleJavaFile(sb.toString()), exportDir + "/" + commitVersion + "/Group" + regionNum + ".txt");
                    _.writeFile(ArtifactPreprocessor.handleJavaFile(ArtifactPreprocessor.handleJSEPCodeChange(sb.toString())), exportDir + "/" + commitVersion + "/Group" + regionNum + ".txt");
                    _.writeFile(ArtifactPreprocessor.handleJSEPCodeChange(sb.toString()), exportDir + "_not_preprocessed/" + commitVersion + "/Group" + regionNum + ".txt");
                }

                for (String changeVersion : methodForChange.keySet()) {
                    List<String> correspondingMethod = methodForChange.get(changeVersion);
                    for (String method : finalRegion.get(regionNum-1)) {
                        if (correspondingMethod.contains(method)) {
                            _.writeFile(ArtifactPreprocessor.handleJavaFile(ArtifactPreprocessor.handleJSEPCodeChange(sb.toString())), groupsFromReleasePath+"/"+changeVersion+"/Group" + regionNum + ".txt");
                        }
                    }
                }
                regionNum++;
            } else {
                System.out.println("No method in this change region");
            }
        }

        // Export the whole change as a artifact.
        _.writeFile(ArtifactPreprocessor.handleJavaFile(wholeChange.toString()), allChangesExportPath + "/all_changes.txt");

        for (String className : methodsInClass.keySet()) {
            StringBuffer classContent = new StringBuffer();

            for (String m : methodsInClass.get(className)) {

                String content = fetchFileContent(m);
                classContent.append(m);
                classContent.append(" ");
                classContent.append(content);
                classContent.append(" ");
            }
            _.writeFile(ArtifactPreprocessor.handleJavaFile(classContent.toString()), singleClassExportPath + "/" + className + ".txt");
        }
    }

    private String extractClassDoc(String className) {
        String oldClassDoc = getClassDoc(className, oldCorpus);
        String newClassDoc = getClassDoc(className, newCorpus);

        if (newClassDoc != null) {
            return newClassDoc;
        } else if (oldClassDoc != null) {
            return oldClassDoc;
        } else {
            return "";
        }
    }

    private boolean isAddedOrRemovedTarget(HashSet<String> region) {
        if (region.size() == 1) {
            String target = null;
            for (String s : region) {
                target = s;
            }
            if (addedArtifactList.contains(target) || removedArtifactList.contains(target)) {
//                System.out.println(target);
                return true;
            }
            return false;
        }
        return false;
    }

    private String extractClassName(String name) {
        String[] tokens = name.split("\\.");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tokens.length; i++) {
            if (Character.isLowerCase(tokens[i].charAt(0))) {
                sb.append(tokens[i]);
                sb.append(".");
            } else {
                sb.append(tokens[i]);
                break;
            }
        }

        return sb.toString();
    }

    public String getMethodContent(String methodName, CorpusExtractor corpus) {
        String doc = _.readFile(corpus.extractedMethodIdentifierPath + "/" + methodName + ".txt");
        return doc;
    }

    private String fetchFileContent(String s) {
//        if (getMethodContent(s, newCorpus) != null) {
//            return getMethodContent(s, newCorpus);
//        } else {
//            return getMethodContent(s, oldCorpus);
//        }



        if (addedArtifactList.contains(s)) {
            return getMethodContent(s, newCorpus);
        } else if (removedArtifactList.contains(s)) {
            return getMethodContent(s, oldCorpus);
        } else {
          return getMethodContent(s, newCorpus);
        }
//        throw new IllegalArgumentException("Not a added or removed method");
    }

    public void showChangeRegion() {
        int totalRegion = 0;
        for (int i = 0; i < finalRegion.size(); i++) {
            if (finalRegion.get(i).size() >= 2 || isAddedOrRemovedTarget(finalRegion.get(i))) {
//            if (changeRegion.get(i).size() >= 2) {

                System.out.println("Final Region " + (totalRegion + 1) + ": ");
                int j = 1;
                for (String vn : finalRegion.get(i)) {
                    System.out.println(j + " " + vn);
                    j++;
                }
                System.out.println();
                totalRegion++;
            }
        }
    }


    private String extractIdentifier(String name) {
        String[] tokens = name.split("\\.");
        return tokens[tokens.length - 1];
    }

    public String getClassDoc(String className, CorpusExtractor corpus) {
        String doc = _.readFile(corpus.extractedClassCommentPath + "/" + className + ".txt");
        return doc;
    }

    public String getCallHierarchy(String methodName) {
        List<CodeVertex> nbs = new ArrayList<>();
        completedCallGraph.searhNeighbourConnectedGraphByCall(methodName, nbs);

        StringBuilder sb = new StringBuilder();
        if (nbs != null) {
            for (CodeVertex cv : nbs) {
//                System.out.println(methodName + " call hierarchy: " + cv.getName());
                sb.append(cv);
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
