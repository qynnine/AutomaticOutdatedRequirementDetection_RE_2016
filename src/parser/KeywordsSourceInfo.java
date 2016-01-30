package parser;

import util._;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by niejia on 16/1/18.
 */
public class KeywordsSourceInfo {

    private Set<String> addedKeywords;
    private Set<String> modifiedKeywords;
    private Set<String> removedKeywords;
    private Set<String> unchangedKeywords;

    private Set<String> classNameKeywords;
    private Set<String> methodNameKeywords;
    private Set<String> methodCommentsKeywords;
    private Set<String> classCommentsKeywords;
    private Set<String> packageName;
    private Set<String> methodParameters;
    private Set<String> fieldName;

    public KeywordsSourceInfo(String path) {
        String input = _.readFile(path);
        parser(input);
    }

    private void parser(String input) {
        addedKeywords = new LinkedHashSet<>();
        modifiedKeywords = new LinkedHashSet<>();
        removedKeywords = new LinkedHashSet<>();
        unchangedKeywords = new LinkedHashSet<>();

        classNameKeywords = new LinkedHashSet<>();
        methodNameKeywords = new LinkedHashSet<>();
        methodCommentsKeywords = new LinkedHashSet<>();
        classCommentsKeywords = new LinkedHashSet<>();
        packageName = new LinkedHashSet<>();
        methodParameters = new LinkedHashSet<>();
        fieldName = new LinkedHashSet<>();

        String[] lines = input.split("\n");
        for (String line : lines) {
            String[] tokens = line.split(" ");
            String text = tokens[0];
            String changedType = tokens[1];
            String sourceEntityType = tokens[2];

            if (changedType.equals("Added")) {
                addedKeywords.add(text);
            } else if (changedType.equals("Removed")) {
                removedKeywords.add(text);
            } else if (changedType.equals("Modified")) {
                modifiedKeywords.add(text);
            } else if (changedType.equals("Unchanged")) {
                unchangedKeywords.add(text);
            }

            if (sourceEntityType.equals("ClassName")) {
                classNameKeywords.add(text);
            } else if (sourceEntityType.equals("MethodName")) {
                methodNameKeywords.add(text);
            } else if (sourceEntityType.equals("ClassComments")) {
                classCommentsKeywords.add(text);
            } else if (sourceEntityType.equals("MethodComments")) {
                methodCommentsKeywords.add(text);
            } else if (sourceEntityType.equals("PackageName")) {
                packageName.add(text);
            } else if (sourceEntityType.equals("MethodParameters")) {
                methodParameters.add(text);
            } else if (sourceEntityType.equals("FieldName")) {
                fieldName.add(text);
            }
        }
    }


    public boolean isTermFromChangedArtifact(String term) {
        return addedKeywords.contains(term) || removedKeywords.contains(term) || modifiedKeywords.contains(term);
    }

    public boolean isTermFromAddedArtifact(String term) {
        return addedKeywords.contains(term);
    }

    public boolean isTermFromRemovedArtifact(String term) {
        return removedKeywords.contains(term);
    }

    public boolean isTermFromModifiedArtifact(String term) {
        return modifiedKeywords.contains(term);
    }

    public boolean isTermFromClassName(String term) {
        return classNameKeywords.contains(term);
    }

    public boolean isTermFromMethodName(String term) {
        return methodNameKeywords.contains(term);
    }

    public boolean isTermFromFieldName(String term) {
        return fieldName.contains(term);
    }

    public boolean isTermFromUnchangedArtifact(String term) {
        return unchangedKeywords.contains(term);
    }
}
