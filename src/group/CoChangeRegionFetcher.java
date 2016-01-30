package group;

import io.ChangeRegionFetcher;
import io.ChangedArtifacts;
import parser.JDiffChangesParser;
import relation.CallRelationGraph;
import relation.graph.CodeVertex;

import java.util.*;

/**
 * Created by niejia on 15/11/4.
 */
public class CoChangeRegionFetcher implements ChangeRegionFetcher {

    private CallRelationGraph changedCallGraph;
    private CallRelationGraph completedCallGraph;
    private HashSet<String> wholeChangedArtifactList;
    private List<HashSet<String>> changeRegion;

    private HashSet<String> addedArtifactList;
    private HashSet<String> removedArtifactList;
    private HashSet<String> modifiedArtifactList;
    private HashMap<String,Boolean> isMethodMerged;
    private JDiffChangesParser jDiffChangesParser;

    public CoChangeRegionFetcher(String changedClassPath, CallRelationGraph changedCallGraph, CallRelationGraph completedCallGraph) {
        ChangedArtifacts parser = new ChangedArtifacts();
        parser.parse(changedClassPath);
        this.wholeChangedArtifactList = parser.getWholeChangedArtifactList();

        this.addedArtifactList = parser.getAddedArtifactList();
        this.removedArtifactList = parser.getRemovedArtifactList();
        this.modifiedArtifactList = parser.getModifiedArtifactList();
//
//        this.wholeChangedArtifactList = ChangesParser.fetchArtifacts(changedClassPath);
        this.changedCallGraph = changedCallGraph;
        this.completedCallGraph = completedCallGraph;
        this.changeRegion = new ArrayList<>();
        this.isMethodMerged = new HashMap<>();

        collectChangeRegion();
    }

    private void collectChangeRegion() {
        HashSet<String> visitedVertexName = new HashSet<>();

        for (String vn : wholeChangedArtifactList) {
            if (!visitedVertexName.contains(vn)) {
                List<CodeVertex> vertexRegion = new ArrayList<>();

                // The artifact has no call relation with others
                if (changedCallGraph.getCodeVertexByName(vn) != null) {
                    changedCallGraph.searhNeighbourConnectedGraphByCall(vn, vertexRegion);
                    HashSet<String> region = new HashSet<>();
                    region.add(vn);
                    for (CodeVertex cv : vertexRegion) {
                        visitedVertexName.add(cv.getName());
                        region.add(cv.getName());
                    }
                    changeRegion.add(region);
                } else {
                    HashSet<String> region = new HashSet<>();
                    region.add(vn);
                    changeRegion.add(region);
                }
            }
        }
        System.out.println("ChangeRegion count: " + changeRegion.size());

        //remove all method which is just changed in method body
        for (int i = 0; i < changeRegion.size(); i++) {
            HashSet<String> region = changeRegion.get(i);

            for (String changedMethod : modifiedArtifactList) {
                region.remove(changedMethod);
            }
//            System.out.println(region);
        }

        cleanEmptyRegion();

        // remove java method like equals, finalize
        removeJavaSpecificMethod();

        // Merge left single method(added or removed) into the existed group (the class that method belongs to appears most times)
        for (int i = 0; i < changeRegion.size(); i++) {
            HashSet<String> region = changeRegion.get(i);
            if (region.size() == 1) {
                String singleMethod = null;
                for (String s : region) {
                    singleMethod = s;
                }

                if (addedArtifactList.contains(singleMethod) || removedArtifactList.contains(singleMethod)) {
                    mergeSingleMethodIntoOneExistedRegion(singleMethod, changeRegion);
                } else {
                    // ignore method that only changed in method body
                }
            }
        }

        // remove the single method which has already bean merged into other region
        removeMergedMethod();




        // remove the region that all method changes in it are modified
        removeRegionContainsOnlyChangedMethod();

        // merge the separated method which is added or removed
        mergeSeparatedMethod();

        // remove the region has only one method, ant it's a java specific method, like hasCode, equals, <init>
        removeRegionContainsOnlyOneJavaSpecificMethod();

        cleanEmptyRegion();
    }

    private void removeJavaSpecificMethod() {
        for (int i = 0; i < changeRegion.size(); i++) {
            HashSet<String> region = changeRegion.get(i);
            Iterator it = region.iterator();

            while (it.hasNext()) {
                String method = (String) it.next();
                String identifier = extractIdentifier(method);
                if (identifier.equals("toString") ||
                        identifier.equals("finalize") ||
                        identifier.equals("equals")) {
                    System.out.println("Remove method: " + method);
                    it.remove();
                }
            }
        }
    }

    private void removeRegionContainsOnlyOneJavaSpecificMethod() {
        Iterator it = changeRegion.iterator();
        while (it.hasNext()) {
            HashSet<String> region = (HashSet<String>) it.next();
            if (region.size() == 1) {
                String methodIdentifier = "";
                for (String s : region) {
                    methodIdentifier = extractIdentifier(s);
                }

                if (methodIdentifier.equals("hashCode") || methodIdentifier.equals("<init>")) {
                    it.remove();
                }
            }
        }
    }

    private void mergeSeparatedMethod() {
        Map<String, List<String>> separatedClassMethod = new HashMap<>();
        Iterator it = changeRegion.iterator();
        while (it.hasNext()) {
            HashSet<String> r = (HashSet<String>) it.next();
            if (r.size() == 1) {
                String m = null;
                for (String s : r) {
                    m = s;
                }

                String className = extractClassName(m);
                if (separatedClassMethod.get(className) == null) {
                    List<String> methods = new ArrayList<>();
                    methods.add(m);
                    separatedClassMethod.put(className, methods);
                } else {
                    List<String> methods = separatedClassMethod.get(className);
                    methods.add(m);
                    separatedClassMethod.put(className, methods);
                }

                it.remove();
            }
        }


        for (String className : separatedClassMethod.keySet()) {
            List<String> methods = separatedClassMethod.get(className);
            changeRegion.add(new HashSet<String>(methods));
        }
    }

    private void removeMergedMethod() {
        Iterator it = changeRegion.iterator();
        while (it.hasNext()) {
            HashSet<String> r = (HashSet<String>) it.next();
            if (r.size() == 1) {
                String m = null;
                for (String s : r) {
                    m = s;
                }

                if (isMethodMerged.get(m) != null && isMethodMerged.get(m) == true) {
                    it.remove();

                }
            }
        }
    }

    private void removeRegionContainsOnlyChangedMethod() {
        Iterator it = changeRegion.iterator();
        while (it.hasNext()) {
            HashSet<String> r = (HashSet<String>) it.next();
            boolean isAllMethodChangesAreModified = true;
            for (String m : r) {
                if (addedArtifactList.contains(m) || removedArtifactList.contains(m)) {
                    isAllMethodChangesAreModified = false;
                }
            }

            if (isAllMethodChangesAreModified) {
                it.remove();
            }
        }
    }

    private void removeMethodWithLowIDF() {
        for (int i = 0; i < changeRegion.size(); i++) {
            HashSet<String> region = changeRegion.get(i);
            Iterator it = region.iterator();
            while (it.hasNext()) {
                String method = (String) it.next();
                if (!completedCallGraph.isMethodAboveThreshold(method)) {
                    it.remove();
                    System.out.println("Remove " + method + " in region");
                }
            }
        }
    }

    private void cleanEmptyRegion() {
        Iterator it = changeRegion.iterator();
        while (it.hasNext()) {
            HashSet<String> r = (HashSet<String>) it.next();
            if (r.size() == 0) {
                it.remove();
            }
        }
    }


    // if method has not appears in any existing region, keep this single region.
    private void mergeSingleMethodIntoOneExistedRegion(String singleMethod, List<HashSet<String>> changeRegion) {

        String className = getClassName(singleMethod);
//        // if this class doesn't appears in another region, keep this region independent
        int num = 0;
        for (int i = 0; i < changeRegion.size(); i++) {
            if (getAppearTimesInRegion(className, changeRegion.get(i)) > 0) {
                num++;
            }
        }
        if (num <= 1) {
            isMethodMerged.put(singleMethod, false);
            return;
        }


        HashSet<String> appearClassNameMostTimesRegion = changeRegion.get(0);
        int mostTime = 0;
        for (int i = 0; i < changeRegion.size(); i++) {
            HashSet<String> region = changeRegion.get(i);
            if (region.size() > 1) {
                int appearTimes = getAppearTimesInRegion(className, region);
                if (appearTimes > mostTime) {
                    appearClassNameMostTimesRegion = region;
                    mostTime = appearTimes;
                }
            }
        }

        if (mostTime > 0) {
            appearClassNameMostTimesRegion.add(singleMethod);
            isMethodMerged.put(singleMethod, true);
        } else {
            isMethodMerged.put(singleMethod, false);
        }
    }

    private int getAppearTimesInRegion(String className, HashSet<String> region) {
        int appearTime = 0;
        for (String s : region) {
            if (getClassName(s).equals(className)) {
                appearTime++;
            }
        }
        return appearTime;
    }

    private String getClassName(String method) {
        String[] tokens = method.split("\\.");

        String className = null;
        for (String t : tokens) {
            if (Character.isUpperCase(t.charAt(0))) {
                className = t;
                break;
            }
        }

        return className;
    }

    public List<HashSet<String>> getChangeRegion() {
        return changeRegion;
    }

    public HashSet<String> getAddedArtifactList() {
        return addedArtifactList;
    }

    public HashSet<String> getRemovedArtifactList() {
        return removedArtifactList;
    }

    public void showChangeRegionWithoutModifiedPart() {
        int totalRegion = 0;
        for (int i = 0; i < changeRegion.size(); i++) {
            if (changeRegion.get(i).size() >= 2 || isAddedOrRemovedTarget(changeRegion.get(i))) {
//            if (changeRegion.get(i).size() >= 2) {

                System.out.println("Change Region " + (totalRegion + 1) + ": ");
                int j = 1;
                for (String vn : changeRegion.get(i)) {
                    if (addedArtifactList.contains(vn) || removedArtifactList.contains(vn)) {
                        System.out.println(j + " " + vn);
                        j++;
                    }
                }
                System.out.println();
                totalRegion++;
            }
        }
    }

    public void showChangeRegion() {
        int totalRegion = 0;
        for (int i = 0; i < changeRegion.size(); i++) {
            if (changeRegion.get(i).size() >= 2 || isAddedOrRemovedTarget(changeRegion.get(i))) {
//            if (changeRegion.get(i).size() >= 2) {

                System.out.println("Change Region " + (totalRegion + 1) + ": ");
                int j = 1;
                for (String vn : changeRegion.get(i)) {
                    System.out.println(j + " " + vn);
                    j++;
                }
                System.out.println();
                totalRegion++;
            }
        }
    }

    private boolean isAddedOrRemovedTarget(HashSet<String> region) {
        if (region.size() == 1) {
            String target = null;
            for (String s : region) {
                target = s;
            }
            if (addedArtifactList.contains(target) || removedArtifactList.contains(target)) {
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

    private String extractIdentifier(String name) {
        String[] tokens = name.split("\\.");
        return tokens[tokens.length - 1];
    }
}
