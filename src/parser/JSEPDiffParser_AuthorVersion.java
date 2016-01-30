package parser;

import core.type.Granularity;
import io.CorpusExtractor;
import parser.type.ClassGroupArtifact;
import preprocess.ArtifactPreprocessor;
import relation.CallRelationGraph;
import relation.RelationInfo;
import relation.graph.CodeVertex;
import util._;

import java.io.File;
import java.util.*;

/**
 * Created by niejia on 15/12/21.
 */
public class JSEPDiffParser_AuthorVersion {


    List<String> addedClassList;
    List<String> modifiedClassList;
    List<String> removedClassList;

    List<String> addedMethodList;
    List<String> removedMethodList;

    List<String> addedFieldList;
    List<String> removedFieldList;

    HashMap<String, ClassGroupArtifact> classCorpus;

    CorpusExtractor newCorpus;
    CorpusExtractor oldCorpus;

    public CallRelationGraph newVersionMethodCallGraph;
    public CallRelationGraph oldVersionMethodCallGraph;

    public CallRelationGraph newVersionClassCallGraph;
    public CallRelationGraph oldVersionClassCallGraph;

    int callHierarchyLevel = 2;
    Hierarchy hierarchyType = Hierarchy.Caller;
    public double callClassGraphThreshold = -1;
    public double callMethodGraphThreshold = -1;

    private String changeOutDirPath;
    private String changeOutWhichIsNotPrepreocessed;

    public JSEPDiffParser_AuthorVersion(String diffFilePath, CorpusExtractor newCorpus, CorpusExtractor oldCorpus, String outDirPath, String projectName, String versionName) {
        addedClassList = new ArrayList<>();
        modifiedClassList = new ArrayList<>();
        removedClassList = new ArrayList<>();

        addedMethodList = new ArrayList<>();
        removedMethodList = new ArrayList<>();

        addedFieldList = new ArrayList<>();
        removedFieldList = new ArrayList<>();

        classCorpus = new HashMap<>();
        this.newCorpus = newCorpus;
        this.oldCorpus = oldCorpus;

        this.changeOutDirPath = outDirPath + "/" + versionName;
        this.changeOutWhichIsNotPrepreocessed = outDirPath + "_not_preprocessed/" + versionName;
        File outDir = new File(changeOutDirPath);
        if (outDir.exists()) {
            for (File f : outDir.listFiles()) {
                f.delete();
            }
        } else {
            outDir.mkdir();
        }

        File outDirNotPreprocessed = new File(changeOutWhichIsNotPrepreocessed);
        if (outDirNotPreprocessed.exists()) {
            for (File f : outDirNotPreprocessed.listFiles()) {
                f.delete();
            }
        } else {
            outDirNotPreprocessed.mkdir();
        }

        String input = _.readFile(diffFilePath);
        String[] lines = input.split("\n");
        for (String line : lines) {
            String[] tokens = line.split(" ");
            String changeState = tokens[0];
            String changeType = tokens[1];
            String artifact = tokens[2];

            if (changeState.equals("Added")) {
                if (changeType.equals("Class")) {
                    if (!addedClassList.contains(artifact)) {
                        addedClassList.add(artifact);
                    }
                } else if (changeType.equals("Method")) {
                    if (!addedMethodList.contains(artifact)) {
                        addedMethodList.add(artifact);
                    }
                } else if (changeType.equals("Field")) {
                    if (!addedFieldList.contains(artifact)) {
                        addedFieldList.add(artifact);
                    }
                }
            } else if (changeState.equals("Removed")) {
                if (changeType.equals("Class")) {
                    if (!removedClassList.contains(artifact)) {
                        removedClassList.add(artifact);
                    }
                } else if (changeType.equals("Method")) {
                    if (!removedMethodList.contains(artifact)) {
                        removedMethodList.add(artifact);
                    }
                } else if (changeType.equals("Field")) {
                    if (!removedFieldList.contains(artifact)) {
                        removedFieldList.add(artifact);
                    }
                }
            } else if (changeState.equals("Changed")) {
                if (changeType.equals("Class")) {
                    if (!modifiedClassList.contains(artifact)) {
                        modifiedClassList.add(artifact);
                    }
                }
            }
        }
    }

    public void process(String newVersionJar, String oldVersionJar) {

        RelationInfo relationInfo_newMethod = new RelationInfo(newVersionJar, Granularity.METHOD, true);
        relationInfo_newMethod.setPruning(callMethodGraphThreshold);
        this.newVersionMethodCallGraph = new CallRelationGraph(relationInfo_newMethod);


        RelationInfo relationInfo_oldMethod = new RelationInfo(oldVersionJar, Granularity.METHOD, true);
        relationInfo_oldMethod.setPruning(callMethodGraphThreshold);
        this.oldVersionMethodCallGraph = new CallRelationGraph(relationInfo_oldMethod);

        RelationInfo relationInfo_newClass = new RelationInfo(newVersionJar, Granularity.CLASS, true);
        relationInfo_newClass.setPruning(callClassGraphThreshold);
        this.newVersionClassCallGraph = new CallRelationGraph(relationInfo_newClass);


        RelationInfo relationInfo_oldClass = new RelationInfo(oldVersionJar, Granularity.CLASS, true);
        relationInfo_oldClass.setPruning(callClassGraphThreshold);
        this.oldVersionClassCallGraph = new CallRelationGraph(relationInfo_oldClass);

        extractFieldCorpus(addedFieldList, newCorpus, "Added");
        extractFieldCorpus(removedFieldList, oldCorpus, "Removed");

        extractMethodCorpus(addedMethodList, newCorpus, "Added");
        extractMethodCorpus(removedMethodList, oldCorpus, "Removed");

        extractClassCorpus(addedClassList, newCorpus, "Added");
        extractClassCorpus(removedClassList, oldCorpus, "Removed");
        extractClassCorpus(modifiedClassList, oldCorpus, "Modified");

        for (String className : classCorpus.keySet()) {
            _.writeFile(ArtifactPreprocessor.handleJavaFile(classCorpus.get(className).toString()), changeOutDirPath + "/" + className + ".txt");
        }

        for (String className : classCorpus.keySet()) {
            _.writeFile(classCorpus.get(className).toString(), changeOutWhichIsNotPrepreocessed + "/" + className + ".txt");
        }
    }

    private void extractClassCorpus(List<String> classList, CorpusExtractor corpus, String changeType) {
        if (changeType.equals("Modified")) return;
        for (String className : classList) {
            ClassGroupArtifact ca;

            if (classCorpus.get(className) == null) {
                ca = new ClassGroupArtifact(className);
            } else {
                ca = classCorpus.get(className);
            }

            addClassToCroup(ca, className, corpus, changeType);
            System.out.println(changeType + " Class: " + className);
            System.out.println("------------------");
            classCorpus.put(className, ca);
        }
    }

    private void addClassToCroup(ClassGroupArtifact ca, String className, CorpusExtractor corpus, String changeType) {
        ca.setClassDoc(getClassDoc(className, corpus));
        for (String field : getSubFieldInClass(className, corpus)) {
            ca.addClassField(className + "." + field);
        }

        if (changeType.equals("Added")) {
            ca.addClassHierarchy(useCallHierarchy(className, newVersionClassCallGraph, callHierarchyLevel, hierarchyType));

            for (String method : getFormattedSubMethodInClass(className, corpus)) {
                ca.addMethod(method);
                ca.addMethodDoc(method, getMethodDoc(method, corpus));
//                ca.addMethodCallHierarchy(method, useCallHierarchy(method, newVersionMethodCallGraph, callHierarchyLevel, hierarchyType));
            }

        } else if (changeType.equals("Removed")) {
            ca.addClassHierarchy(useCallHierarchy(className, oldVersionClassCallGraph, callHierarchyLevel, hierarchyType));

            for (String method : getFormattedSubMethodInClass(className, corpus)) {
                ca.addMethod(method);
                ca.addMethodDoc(method, getMethodDoc(method, corpus));
//                ca.addMethodCallHierarchy(method, useCallHierarchy(method, oldVersionMethodCallGraph, callHierarchyLevel, hierarchyType));
            }

        }
    }

    private void extractMethodCorpus(List<String> methodList, CorpusExtractor corpus, String changeType) {
        for (String method : methodList) {
            String className = extractClassName(method);

            ClassGroupArtifact ca;
            if (classCorpus.get(className) == null) {
                ca = new ClassGroupArtifact(className);
            } else {
                ca = classCorpus.get(className);

            }
            addMethodToGroup(ca, className, method, corpus, changeType);
            System.out.println(changeType + " Method: " + method);
            System.out.println("------------------");
            classCorpus.put(className, ca);
        }
    }

    private void addMethodToGroup(ClassGroupArtifact ca, String className, String methodName, CorpusExtractor corpus, String changeType) {
        ca.addMethod(methodName);
        ca.addMethodDoc(methodName, getMethodDoc(methodName, corpus));
        ca.setClassDoc(getClassDoc(className, corpus));

        if (changeType.equals("Added")) {
            ca.addMethodCallHierarchy(methodName, useCallHierarchy(methodName, newVersionMethodCallGraph, callHierarchyLevel, hierarchyType));
        } else if (changeType.equals("Removed")) {
            ca.addMethodCallHierarchy(methodName, useCallHierarchy(methodName, oldVersionMethodCallGraph, callHierarchyLevel, hierarchyType));
        }
    }



    private void extractFieldCorpus(List<String> fieldList, CorpusExtractor corpus, String changeType) {
        for (String field : fieldList) {
            String className = extractClassName(field);

            ClassGroupArtifact ca;
            if (classCorpus.get(className) == null) {
                ca = new ClassGroupArtifact(className);
            } else {
                ca = classCorpus.get(className);
            }

            addFiledToGroup(ca, className, field, corpus);
            System.out.println(changeType + " field: " + field);
            System.out.println("------------------");
            classCorpus.put(className, ca);
        }
    }

    private void addFiledToGroup(ClassGroupArtifact ca, String className, String fieldName, CorpusExtractor corpus) {
        ca.addClassField(fieldName);
        ca.setClassDoc(getClassDoc(className, corpus));
    }

    public String getMethodDoc(String methodName, CorpusExtractor corpus) {
        String doc = _.readFile(corpus.extractedMethodCommentPath + "/" + methodName + ".txt");
        return doc;
    }

    public String getClassDoc(String className, CorpusExtractor corpus) {
        String doc = _.readFile(corpus.extractedClassCommentPath + "/" + className + ".txt");
        return doc;
    }

    public String getSubMethodInClass(String className, CorpusExtractor corpus) {
        String subMethods = _.readFile(corpus.extractedClassMethodPath + "/" + className + ".txt");
        return subMethods;
    }

    public String getSubMethodFullNameInClass(String className, CorpusExtractor corpus) {
        String subMethods = _.readFile(corpus.extractedClassMethodFullNamePath + "/" + className + ".txt");
        return subMethods;
    }

    public List<String> getFormattedSubMethodInClass(String className, CorpusExtractor corpus) {
        List<String> result = new ArrayList<>();
        String methodsText = getSubMethodFullNameInClass(className, corpus);
        String[] tokens = methodsText.split("\n");
        for (String s : tokens) {
            if (s != null && !s.equals("")) {
                result.add(s);
            }
        }
        System.out.println(result);
        return result;
    }

    public String[] getSubFieldInClass(String className, CorpusExtractor corpus) {
        String subFields = _.readFile(corpus.extractedClassFieldPath + "/" + className + ".txt");
        return subFields.split("\n");
    }

    private String extractIdentifier(String name) {
        String[] tokens = name.split("\\.");
        return tokens[tokens.length - 1];
    }

    private String extractClassIdentifier(String name) {
        String[] tokens = name.split("\\.");
        return tokens[tokens.length - 2];
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

    // use method and method Call Graph or use class and Class Call Graph
    public String useCallHierarchy(String element,CallRelationGraph callGraph, int level, Hierarchy type) {

        if (element.equals("edu.ncsu.csc.itrust.beans.forms.EditOfficeVisitForm.getOverrideCodes")) {
            System.out.println("haha");
        }
        String packageName = extractPackageName(element);
        int currentlevel = 1;
        List<String> currentFather = new ArrayList<>();
        List<String> currentChildren = new ArrayList<>();
        int lastFatherNumb = 0;
        int lastChildrenNumb = 0;
        currentFather.add(element);
        currentChildren.add(element);

//        while (currentlevel <= level ) {
        while (currentlevel <= level && (lastChildrenNumb < currentChildren.size() || lastFatherNumb < currentFather.size())) {
            lastFatherNumb = currentFather.size();
            lastChildrenNumb = currentChildren.size();

            List<String> tmpForFather = new ArrayList<>();
            for (String center : currentFather) {
                List<CodeVertex> father = callGraph.getFathersByCall(center);

                for (CodeVertex s : father) {
                    if (!tmpForFather.contains(s.getName())) {
                        tmpForFather.add(s.getName());
                    }
                }
            }
            for (String s : tmpForFather) {
                if (!currentFather.contains(s)) {
                    currentFather.add(s);
                }
            }

            List<String> tmpForChild = new ArrayList<>();
            for (String center : currentChildren) {
                List<CodeVertex> children = callGraph.getChildrenByCall(center);

                for (CodeVertex s : children) {
                    if (!tmpForChild.contains(s.getName())) {
                        tmpForChild.add(s.getName());
                    }
                }
            }
            for (String s : tmpForChild) {
                if (!currentChildren.contains(s)) {
                    currentChildren.add(s);
                }
            }


            currentlevel++;
        }

        currentChildren.remove(element);
        currentFather.remove(element);

        StringBuilder result = new StringBuilder();
        if (type.equals(Hierarchy.Callee)) {
            Set<String> context = new HashSet<>();
            for (String s : currentChildren) {
                context.add(s);
            }

            for (String s : context) {
//                if (extractPackageName(s).equals(packageName)) {
                if (isClassName(s)) {
                    result.append(extractIdentifier(s));
                } else {
                    result.append(extractClassIdentifier(s) + " " + extractIdentifier(s));
                }
                result.append("\n");
//                }
            }

        } else if (type.equals(Hierarchy.Caller)) {
            Set<String> context = new HashSet<>();
            for (String s : currentFather) {
                context.add(s);
            }

            for (String s : context) {
//                if (extractPackageName(s).equals(packageName)) {
                if (isClassName(s)) {
                    result.append(extractIdentifier(s));
                } else {
                    result.append(extractClassIdentifier(s) + " " + extractIdentifier(s));
                }
                result.append("\n");
//                }
            }
        } else if (type.equals(Hierarchy.CallerAndCallee)) {
            Set<String> context = new HashSet<>();
            for (String s : currentFather) {
                context.add(s);
            }

            for (String s : currentChildren) {
                context.add(s);
            }

            for (String s : context) {
//                if (extractPackageName(s).equals(packageName)) {
                if (isClassName(s)) {
                    result.append(extractIdentifier(s));
                } else {
                    result.append(extractClassIdentifier(s) + " " + extractIdentifier(s));
                }
                result.append("\n");
//                }
            }
        }

        return result.toString();
    }

    private String extractPackageName(String classOrMethodName) {
        String[] tokens = classOrMethodName.split("\\.");
        StringBuilder sb = new StringBuilder();

        for (String s : tokens) {
            if (Character.isLowerCase(s.charAt(0))) {
                sb.append(s);
                sb.append(".");
            } else {
                break;
            }
        }
        return sb.toString();
    }

    private boolean isClassName(String element) {
        String[] tokens = element.split("\\.");
        return Character.isUpperCase(tokens[tokens.length - 1].charAt(0));
    }

    public static void main(String[] args) {
//        CorpusExtractor oldCorpus = new CorpusExtractor("Aqualush", "change3");
//        CorpusExtractor newCorpus = new CorpusExtractor("Aqualush", "change4");
//
//        JSEPDiffParser_Version2 parser = new JSEPDiffParser_Version2("data/Aqualush/JSEP_Diff/Aqualush_change4.txt", newCorpus, oldCorpus,
//                "data/Aqualush/grouped_by_jsep_my_version", "Aqualush", "change4");
//
//        parser.process(AqualushSetting.Change4_JAR, AqualushSetting.Change3_JAR);
    }
}
