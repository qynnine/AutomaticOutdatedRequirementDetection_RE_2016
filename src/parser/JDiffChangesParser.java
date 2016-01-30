package parser;

import io.CorpusExtractor;
import util.JavaElement;
import util._;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by niejia on 16/1/10.
 */
public class JDiffChangesParser {
    List<String> addedClassList;
    List<String> modifiedClassList;
    List<String> removedClassList;

    List<String> addedMethodList;
    List<String> removedMethodList;

    List<String> addedFieldList;
    List<String> removedFieldList;

    CorpusExtractor newCorpus;
    CorpusExtractor oldCorpus;


    Map<String, Set<String>> fieldAppearsInMethod;
    Set<String> totalAddedFields;
    Set<String> totalRemovedFields;

    Set<String> totalAddedMethods;
    Set<String> totalRemovedMethods;

    public JDiffChangesParser(String diffFilePath, CorpusExtractor newCorpus, CorpusExtractor oldCorpus) {
        addedClassList = new ArrayList<>();
        modifiedClassList = new ArrayList<>();
        removedClassList = new ArrayList<>();

        addedMethodList = new ArrayList<>();
        removedMethodList = new ArrayList<>();

        addedFieldList = new ArrayList<>();
        removedFieldList = new ArrayList<>();

        this.newCorpus = newCorpus;
        this.oldCorpus = oldCorpus;


        fieldAppearsInMethod = new HashMap<>();

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

        constructTotalChangedFields();
        constructTotalChangedMethods();

        findMethodContainsField();
    }

    private void findMethodContainsField() {
        fetchMethodHasField(totalAddedFields, newCorpus);
        System.out.println("Say hi");
    }

    public Set<String> involvedChangedFieldInMethod(String methodName, String type) {
        CorpusExtractor corpus = new CorpusExtractor();
        Set<String> modifiedFields = new LinkedHashSet<>();
        if (type.equals("Added")) {
            corpus = newCorpus;
            modifiedFields = totalAddedFields;
        } else if (type.equals("Removed")) {
            corpus = oldCorpus;
            modifiedFields = totalRemovedFields;
        }

        String className = JavaElement.getClassName(methodName);
        Set<String> changedFieldsInClass = getFieldsBelongToClass(className, modifiedFields);

        Set<String> involvedField = new LinkedHashSet<>();
        for (String fieldName : changedFieldsInClass) {
            Set<String> fieldUsages = fetchFieldUseagesInMethod(methodName, fieldName, corpus);
            if (fieldUsages.size() != 0) {
                involvedField.add(fieldName);
            }
        }

//        System.out.println(" changedFieldsInClass = " + changedFieldsInClass );
//        System.out.println(" involvedField = " + involvedField );

        return involvedField;
    }

    private Set<String> getFieldsBelongToClass(String className, Set<String> fields) {
        Set<String> result = new LinkedHashSet<>();

        for (String s : fields) {
            String s_className = JavaElement.getClassName(s);
            if (s_className.equals(className)) {
                result.add(s);
            }
        }

        return result;
    }

    private void fetchMethodHasField(Set<String> changedFields, CorpusExtractor corpus) {

    }

    public Set<String> fetchFieldUseagesInMethod(String methodName, String filedName, CorpusExtractor corpus) {
        Set<String> usages = new LinkedHashSet<>();
        String methodBody = corpus.getMethodBody(methodName);
        String fieldID = JavaElement.getIdentifier(filedName);
        for (String line : methodBody.split("\n")) {
            if (fetchWordInLine(line, fieldID)) {
//                System.out.println(line);
                usages.add(line);
            }
        }
        return usages;
    }


    public boolean fetchWordInLine(String line, String word) {
        Pattern pat = Pattern.compile(word);
        Matcher m = pat.matcher(line);
        return m.find();
    }

    private void constructTotalChangedMethods() {
        totalAddedMethods = new LinkedHashSet<>();
        totalRemovedMethods = new LinkedHashSet<>();

        for (String className : addedClassList) {
            Set<String> fields = newCorpus.getMethodsInClass(className);
            for (String s : fields) {
                totalAddedMethods.add(s);
            }
        }

        for (String s : addedMethodList) {
            totalAddedMethods.add(s);
        }

        for (String className : removedClassList) {
            Set<String> fields = oldCorpus.getFieldsInClass(className);
            for (String s : fields) {
                totalRemovedMethods.add(s);
            }
        }

        for (String s : removedMethodList) {
            totalRemovedMethods.add(s);
        }
    }

    private void constructTotalChangedFields() {
        totalAddedFields = new LinkedHashSet<>();
        totalRemovedFields = new LinkedHashSet<>();

        for (String className : addedClassList) {
            Set<String> fields = newCorpus.getFieldsInClass(className);
            for (String s : fields) {
                totalAddedFields.add(s);
            }
        }

        for (String s : addedFieldList) {
            totalAddedFields.add(s);
        }

        for (String className : removedClassList) {
            Set<String> fields = oldCorpus.getFieldsInClass(className);
            for (String s : fields) {
                totalRemovedFields.add(s);
            }
        }

        for (String s : removedFieldList) {
            totalRemovedFields.add(s);
        }
    }

    public static void main(String[] args) {

        CorpusExtractor corpus3 = new CorpusExtractor("Aqualush", "change3");
        CorpusExtractor corpus4 = new CorpusExtractor("Aqualush", "change4");
        CorpusExtractor corpus5 = new CorpusExtractor("Aqualush", "change5");

        CorpusExtractor corpus0 = new CorpusExtractor("Aqualush", "change0");
        CorpusExtractor corpus99 = new CorpusExtractor("Aqualush", "change99");

//        System.out.println(corpus4.getMethodsInClass("ui.SetMaxLevelScrnState"));
//        System.out.println(corpus4.getMethodBody("ui.SetMaxLevelScrnState.keyPress"));
//        System.out.println(corpus4.getMethodParameters("ui.SetMaxLevelScrnState.keyPress"));

        JDiffChangesParser parser5 = new JDiffChangesParser("data/Aqualush/JSEP_Diff/Aqualush_change5.txt", corpus5, corpus4);

//
//        parser4.fetchFieldUseagesInMethod("ui.SetMaxLevelScrnState.keyPress", "ui.SetMaxLevelScrnState.items", corpus4);
        parser5.involvedChangedFieldInMethod("ui.UIController.buildSetWaterAllocationScrn", "Added");

    }

    public boolean isAddedField(String str) {
        return addedFieldList.contains(str);
    }

    public boolean isRemovedField(String str) {
        return removedFieldList.contains(str);
    }
}
