package group;

import core.type.ChangedType;
import core.type.SourceEntityType;
import exp.iTrust.handle_group.DivideITrustGroupForEveryChange;
import io.ChangedArtifacts;
import io.CorpusExtractor;
import parser.JDiffChangesParser;
import preprocess.ArtifactPreprocessor;
import util.JavaElement;
import util._;

import java.io.File;
import java.util.*;

/**
 * Created by niejia on 16/1/16.
 */
public class KeywordsExtracterForChangeRegion {

    private ChangedArtifacts changedArtifacts;
    private Map<String, HashSet<String>> changeRegions;
    private List<HashSet<String>> changedArtifactsGroup;
    private CorpusExtractor newCorpus;
    private CorpusExtractor oldCorpus;
    private String commitVersion;
    private Map<String, List<String>> methodChangeMapping;
    private String groupsFromReleasePath;

    private List<Set<String>> finalRegionsList;

    private String iTrustRegionForRelease = "/change_region_release";
//
    private String exportDirPath;
    private String methodChangeMappingPath = "data/iTrust/divide_group_to_change/method_for_change.txt";

    private JDiffChangesParser jDiffChangesParser;


    public KeywordsExtracterForChangeRegion(ChangedArtifacts changedArtifacts, Map<String, HashSet<String>> changeRegions, List<HashSet<String>> changedArtifactsGroup, String newVersionName, String oldVersionName, CorpusExtractor newCorpus, CorpusExtractor oldCorpus, String exportDirPath, JDiffChangesParser jDiffChangesParser) {
        this.changedArtifacts = changedArtifacts;
        this.changeRegions = changeRegions;
        this.changedArtifactsGroup = changedArtifactsGroup;
        this.newCorpus = newCorpus;
        this.oldCorpus = oldCorpus;

        this.exportDirPath = exportDirPath;

        this.finalRegionsList = new ArrayList<>();

        this.commitVersion = newVersionName;
        this.methodChangeMapping = (new DivideITrustGroupForEveryChange(methodChangeMappingPath)).methodForChange;

        this.jDiffChangesParser = jDiffChangesParser;

        mergeChangeRegion();
        cleanDir();
        extractKeywords();
    }


    private void mergeChangeRegion() {
        for (Set<String> changedGroup : changedArtifactsGroup) {
            Set<String> region = new LinkedHashSet<>();
            for (String changedArtifact : changedGroup) {
                Set<String> regionForArtifact = changeRegions.get(changedArtifact);
                for (String v : regionForArtifact) {
                    region.add(v);
                }
            }
            finalRegionsList.add(region);
        }
    }

    private void extractKeywords() {
        int regionNumber = 1;

        for (Set<String> region : finalRegionsList) {
            StringBuilder sb = new StringBuilder();
            StringBuilder sb_source = new StringBuilder();

            Set<String> containedFieldForRegion = new LinkedHashSet<>();

            for (String v : region) {
                String packageIdentifier = JavaElement.getPackageName(v);
                String classIdentifier = JavaElement.getIdentifier(JavaElement.getClassName(v));
                String methodIdentifier = JavaElement.getIdentifier(v);


                if (changedArtifacts.isAddedArtifact(v)) {
                      String methodParameters = getMethodParameters(v, newCorpus);
                    String methodDoc = getMethodDoc(v, newCorpus);
//                    sb.append(v);
//                    sb.append("\n");
//                    sb.append(fetchFileContent(v));
//                    sb.append("\n");
                    sb.append(packageIdentifier);
                    sb.append("\n");
                    sb.append(classIdentifier);
                    sb.append("\n");
                    sb.append(methodIdentifier);
                    sb.append("\n");
                    sb.append(methodParameters);
                    sb.append("\n");
                    sb.append(methodDoc);
                    sb.append("\n");
//
                    recordWeighting(sb_source, packageIdentifier, ChangedType.Added, SourceEntityType.PackageName);
                    recordWeighting(sb_source, classIdentifier, ChangedType.Added, SourceEntityType.ClassName);
                    recordWeighting(sb_source, methodIdentifier, ChangedType.Added, SourceEntityType.MethodName);
                    recordWeighting(sb_source, methodParameters, ChangedType.Added, SourceEntityType.MethodParameters);
                    recordWeighting(sb_source, methodDoc, ChangedType.Added, SourceEntityType.MethodComments);

                    AppendInvolvedFieldInRegion(containedFieldForRegion, v);

                } else if (changedArtifacts.isRemovedArtifact(v)) {
                    String methodParameters = getMethodParameters(v, oldCorpus);
                    String methodDoc = getMethodDoc(v, oldCorpus);
//                    sb.append(v);
//                    sb.append("\n");
//                    sb.append(fetchFileContent(v));
//                    sb.append("\n");
                    sb.append(packageIdentifier);
                    sb.append("\n");
                    sb.append(classIdentifier);
                    sb.append("\n");
                    sb.append(methodIdentifier);
                    sb.append("\n");
                    sb.append(methodParameters);
                    sb.append("\n");
                    sb.append(methodDoc);
                    sb.append("\n");

                    recordWeighting(sb_source, packageIdentifier, ChangedType.Removed, SourceEntityType.PackageName);
                    recordWeighting(sb_source, classIdentifier, ChangedType.Removed, SourceEntityType.ClassName);
                    recordWeighting(sb_source, methodIdentifier, ChangedType.Removed, SourceEntityType.MethodName);
                    recordWeighting(sb_source, methodParameters, ChangedType.Removed, SourceEntityType.MethodParameters);
                    recordWeighting(sb_source, methodDoc, ChangedType.Removed, SourceEntityType.MethodComments);


                    AppendInvolvedFieldInRegion(containedFieldForRegion, v);
                } else if (changedArtifacts.isModifiedArtifact(v)) {
                    String methodParameters = getMethodParameters(v, newCorpus);
//                    sb.append(v);
//                    sb.append("\n");
//                    sb.append(methodParameters);
//                    sb.append("\n");
                    sb.append(packageIdentifier);
                    sb.append("\n");
                    sb.append(classIdentifier);
                    sb.append("\n");
                    sb.append(methodIdentifier);
                    sb.append("\n");
                    sb.append(methodParameters);
                    sb.append("\n");

                    recordWeighting(sb_source, packageIdentifier, ChangedType.Modified, SourceEntityType.PackageName);
                    recordWeighting(sb_source, classIdentifier, ChangedType.Modified, SourceEntityType.ClassName);
                    recordWeighting(sb_source, methodIdentifier, ChangedType.Modified, SourceEntityType.MethodName);
                    recordWeighting(sb_source, methodParameters, ChangedType.Modified, SourceEntityType.MethodParameters);
                    AppendInvolvedFieldInRegion(containedFieldForRegion, v);
                } else {

                    String methodParameters = getMethodParameters(v, newCorpus);
//                    sb.append(v);
//                    sb.append("\n");
//                    sb.append(methodParameters);
//                    sb.append("\n");
                    sb.append(packageIdentifier);
                    sb.append("\n");
                    sb.append(classIdentifier);
                    sb.append("\n");
                    sb.append(methodIdentifier);
                    sb.append("\n");
                    sb.append(methodParameters);
                    sb.append("\n");

                    recordWeighting(sb_source, packageIdentifier, ChangedType.Unchanged, SourceEntityType.PackageName);
                    recordWeighting(sb_source, classIdentifier, ChangedType.Unchanged, SourceEntityType.ClassName);
                    recordWeighting(sb_source, methodIdentifier, ChangedType.Unchanged, SourceEntityType.MethodName);
                    recordWeighting(sb_source, methodParameters, ChangedType.Unchanged, SourceEntityType.MethodParameters);
                }
            }

            //Maybe we need add every method's class doc into method region
            Set<String> extractedDocClass = new HashSet<>();
            for (String s : region) {
                if (changedArtifacts.isAddedArtifact(s) || changedArtifacts.isRemovedArtifact(s)) {
                    String className = JavaElement.getClassName(s);
                    if (!extractedDocClass.contains(className)) {
                        String classdoc = extractClassDoc(className);
                        extractedDocClass.add(className);
                        sb.append(classdoc);
                        sb.append("\n");
                        if (changedArtifacts.isAddedArtifact(s)) {
                            recordWeighting(sb_source, classdoc, ChangedType.Added, SourceEntityType.ClassComments);
                        } else if (changedArtifacts.isRemovedArtifact(s)) {
                            recordWeighting(sb_source, classdoc, ChangedType.Removed, SourceEntityType.ClassComments);
                        }
                    }
                }
            }

            //Maybe we need add fields involved in method region
            for (String fieldName : containedFieldForRegion) {
                String fieldIdentifier = JavaElement.getIdentifier(fieldName);
                sb.append(fieldIdentifier);
                sb.append(" ");
                if (jDiffChangesParser.isAddedField(fieldName)) {
                    recordWeighting(sb_source, fieldIdentifier, ChangedType.Added, SourceEntityType.FieldName);
                } else if (jDiffChangesParser.isRemovedField(fieldName)) {
                    recordWeighting(sb_source, fieldIdentifier, ChangedType.Removed, SourceEntityType.FieldName);
                }
            }

            sb.append("\n");

            if (!sb.toString().equals("")) {
                _.writeFile(ArtifactPreprocessor.handleJavaFile(ArtifactPreprocessor.handleJSEPCodeChange(sb.toString())), exportDirPath + "/" + commitVersion + "/Group" + regionNumber + ".txt");
                _.writeFile(ArtifactPreprocessor.handleJSEPCodeChange(sb.toString()), exportDirPath + "_not_preprocessed/" + commitVersion + "/Group" + regionNumber + ".txt");
                _.writeFile(ArtifactPreprocessor.handleJavaFile(ArtifactPreprocessor.handleJSEPCodeChange(sb.toString())), groupsFromReleasePath + "/" + commitVersion + "/Group" + regionNumber + ".txt");
                _.writeFile(sb_source.toString(), exportDirPath + "_weighting/" + commitVersion + "/Group" + regionNumber + ".txt");
            }

            for (String changeVersion : methodChangeMapping.keySet()) {
                List<String> correspondingMethod = methodChangeMapping.get(changeVersion);
                for (String method : finalRegionsList.get(regionNumber-1)) {
                    if (correspondingMethod.contains(method)) {
                        _.writeFile(ArtifactPreprocessor.handleJavaFile(ArtifactPreprocessor.handleJSEPCodeChange(sb.toString())), groupsFromReleasePath+"/"+changeVersion+"/Group" + regionNumber + ".txt");
                        _.writeFile(ArtifactPreprocessor.handleJSEPCodeChange(sb.toString()), groupsFromReleasePath+"_not_preprocessed"+"/"+changeVersion+"/Group" + regionNumber + ".txt");
                        _.writeFile(sb_source.toString(), groupsFromReleasePath+"_weighting" + "/" + changeVersion + "/Group" + regionNumber + ".txt");

                    }
                }
            }

            regionNumber++;
        }
    }

    private void recordWeighting(StringBuilder sb, String str, ChangedType changedType, SourceEntityType sourceEntityType) {
        String preprocessed = ArtifactPreprocessor.handleJavaFile(ArtifactPreprocessor.handleJSEPCodeChange(str));
        String[] tokens = preprocessed.split(" ");
        for (String t : tokens) {
            if (!t.isEmpty()) {
                sb.append(t);
                sb.append(" ");
                sb.append(changedType);
                sb.append(" ");
                sb.append(sourceEntityType);
                sb.append("\n");
            }
        }
    }

    private void AppendInvolvedFieldInRegion(Set<String> containedFieldForRegion, String v) {
        Set<String> fieldInfo = new LinkedHashSet<>();
        if (changedArtifacts.isAddedArtifact(v)) {
            fieldInfo = jDiffChangesParser.involvedChangedFieldInMethod(v, "Added");
        } else if (changedArtifacts.isRemovedArtifact(v)) {
            fieldInfo = jDiffChangesParser.involvedChangedFieldInMethod(v, "Removed");
        }
        for (String fieldName : fieldInfo) {
            containedFieldForRegion.add(fieldName);
        }
    }

    private String getMethodParameters(String methodName, CorpusExtractor corpus) {
        String parameters = _.readFile(corpus.extractedMethodParameterPath + "/" + methodName + ".txt");
        return parameters == null ? "" : parameters;
    }

    public void export() {
        File exportFile = new File(exportDirPath + "/" + commitVersion);
        if (exportFile.exists()) {
            for (File file : exportFile.listFiles()) file.delete();
        } else {
            exportFile.mkdir();
        }
    }

    private String fetchFileContent(String s) {
        if (changedArtifacts.isAddedArtifact(s)) {
            return getMethodContent(s, newCorpus);
        } else if (changedArtifacts.isRemovedArtifact(s)) {
            return getMethodContent(s, oldCorpus);
        } else {
            return getMethodContent(s, newCorpus);
        }
    }

    public String getMethodContent(String methodName, CorpusExtractor corpus) {
        String doc = _.readFile(corpus.extractedMethodIdentifierPath + "/" + methodName + ".txt");
        return doc == null ? "" : doc;
    }


    public String getMethodDoc(String methodName, CorpusExtractor corpus) {
        String doc = _.readFile(corpus.extractedMethodCommentPath + "/" + methodName + ".txt");
        return doc == null ? "" : doc;
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

    public String getClassDoc(String className, CorpusExtractor corpus) {
        String doc = _.readFile(corpus.extractedClassCommentPath + "/" + className + ".txt");
        return doc == null ? "" : doc;
    }

    public void showFinalRegion() {
        int i = 1;
        for (Set<String> region : finalRegionsList) {
            System.out.println("Region " + i + ":");
            int j = 1;
            for (String s : region) {
                System.out.println(j+": "+s);
                j++;
            }
            i++;

        }
    }



    private void cleanDir() {
        File exportFile = new File(exportDirPath + "/" + commitVersion);
        if (exportFile.exists()) {
            for (File file : exportFile.listFiles()) file.delete();
        } else {
            exportFile.mkdir();
        }

        File exportNotPreprocessedFile = new File(exportDirPath+"_not_preprocessed" + "/" + commitVersion);
        if (exportNotPreprocessedFile.exists()) {
            for (File file : exportNotPreprocessedFile.listFiles()) file.delete();
        } else {
            exportNotPreprocessedFile.mkdir();
        }

        File exportWeightingFile = new File(exportDirPath+"_weighting" + "/" + commitVersion);
        if (exportWeightingFile.exists()) {
            for (File file : exportWeightingFile.listFiles()) file.delete();
        } else {
            exportWeightingFile.mkdir();
        }


        File divideGroupsToChangeFile = new File(exportFile.getParentFile().getParentFile().getPath() + iTrustRegionForRelease);
        groupsFromReleasePath = divideGroupsToChangeFile.getPath();
        if (divideGroupsToChangeFile.exists()) {
            for (File file : divideGroupsToChangeFile.listFiles()) file.delete();
        } else {
            divideGroupsToChangeFile.mkdir();
        }

        String[] versionArray = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        for (String i : versionArray) {
            File dir = new File(divideGroupsToChangeFile + "/change" + i);
            if (dir.exists()) {
                for (File file : dir.listFiles()) file.delete();
            } else {
                dir.mkdir();
            }
        }

        File releaseDir = new File(divideGroupsToChangeFile + "/" + commitVersion);
        if (releaseDir.exists()) {
            for (File file : releaseDir.listFiles()) file.delete();
        } else {
            releaseDir.mkdir();
        }

        for (String i : versionArray) {
            File dir = new File(divideGroupsToChangeFile+"_weighting" + "/change" + i);
            if (dir.exists()) {
                for (File file : dir.listFiles()) file.delete();
            } else {
                dir.mkdir();
            }
        }

        for (String i : versionArray) {
            File dir = new File(divideGroupsToChangeFile+"_not_preprocessed" + "/change" + i);
            if (dir.exists()) {
                for (File file : dir.listFiles()) file.delete();
            } else {
                dir.mkdir();
            }
        }
    }
}
