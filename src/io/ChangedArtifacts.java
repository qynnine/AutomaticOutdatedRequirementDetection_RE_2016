package io;

import util._;

import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * Created by niejia on 15/11/8.
 */
public class ChangedArtifacts {

    private HashSet<String> addedArtifactList;
    private HashSet<String> removedArtifactList;
    private HashSet<String> modifiedArtifactList;

    private HashSet<String> wholeChangedArtifactList;

    public void parse(String path) {
        String input = _.readFile(path);
        String lines[] = input.split("\n");

        addedArtifactList = new LinkedHashSet<>();
        removedArtifactList = new LinkedHashSet<>();
        modifiedArtifactList = new LinkedHashSet<>();
        wholeChangedArtifactList = new LinkedHashSet<>();

        for (String line : lines) {
            if (line.startsWith("Added")) {
                getAddedArtifactList().add(line.split(" ")[1]);
            } else if (line.startsWith("Removed")) {
                getRemovedArtifactList().add(line.split(" ")[1]);
            } else if (line.startsWith("Changed")) {
                getModifiedArtifactList().add(line.split(" ")[1]);
            }
        }

        getWholeChangedArtifactList().addAll(getAddedArtifactList());
        getWholeChangedArtifactList().addAll(getRemovedArtifactList());
        getWholeChangedArtifactList().addAll(getModifiedArtifactList());
    }

    public boolean isAddedArtifact(String artifactName) {
        return getAddedArtifactList().contains(artifactName);
    }

    public boolean isRemovedArtifact(String artifactName) {
        return getRemovedArtifactList().contains(artifactName);
    }

    public boolean isModifiedArtifact(String artifactName) {
        return getModifiedArtifactList().contains(artifactName);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Added elements: " + getAddedArtifactList().size());
        for (String e : getAddedArtifactList()) {
            sb.append(e);
            sb.append("\n");
        }
        sb.append("\n");
        sb.append("Removed elements: " + getRemovedArtifactList().size());
        for (String e : getRemovedArtifactList()) {
            sb.append(e);
            sb.append("\n");
        }
        sb.append("\n");
        sb.append("changed elements: " + getModifiedArtifactList().size());
        for (String e : getModifiedArtifactList()) {
            sb.append(e);
            sb.append("\n");
        }
        return sb.toString();
    }

    public HashSet<String> getAddedArtifactList() {
        return addedArtifactList;
    }

    public HashSet<String> getRemovedArtifactList() {
        return removedArtifactList;
    }

    public HashSet<String> getModifiedArtifactList() {
        return modifiedArtifactList;
    }

    public HashSet<String> getWholeChangedArtifactList() {
        return wholeChangedArtifactList;
    }
}
