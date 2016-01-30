package group;

import io.ChangedArtifacts;
import relation.CallRelationGraph;
import relation.graph.CodeVertex;
import util.JavaElement;

import java.util.*;

/**
 * Created by niejia on 16/1/22.
 */
public class ChangedArtifactsGrouper_II {

    private ChangedArtifacts changedArtifacts;
    private CallRelationGraph callGraphForChangedPart;

    private List<HashSet<String>> changedArtifactsGroup;
    private HashMap<String,Boolean> isMethodMerged;
    private CallRelationGraph newCallGraph;
    private CallRelationGraph oldCallGraph;
    private Map<String, HashSet<String>> initialChangeRegion;


    public ChangedArtifactsGrouper_II(ChangedArtifacts changedArtifacts, CallRelationGraph callGraphForChangedPart, Map<String, HashSet<String>> initialChangeRegion, CallRelationGraph newCallGraph, CallRelationGraph oldnewCallGraph) {
        this.callGraphForChangedPart = callGraphForChangedPart;
        this.changedArtifacts = changedArtifacts;
        this.changedArtifactsGroup = new ArrayList<>();
        this.newCallGraph = newCallGraph;
        this.oldCallGraph = oldnewCallGraph;
        this.initialChangeRegion = initialChangeRegion;

        groupChangedArtifact();
    }

    private Set<String> findVertexNeighboursInBothInitialRegionAndChangedPart(String target) {
        Set<String> nbs = new LinkedHashSet<>();

        List<CodeVertex> vertexRegion = new ArrayList<>();
        if (callGraphForChangedPart.getCodeVertexByName(target) != null) {
            callGraphForChangedPart.searhNeighbourConnectedGraphByCall(target, vertexRegion);
            for (CodeVertex codeVertex : vertexRegion) {
                String vName = codeVertex.getName();
                if (changedArtifacts.isAddedArtifact(vName) || changedArtifacts.isRemovedArtifact(vName)) {
                    nbs.add(vName);
                }
            }
        }

        HashSet<String> initialRegion = initialChangeRegion.get(target);
        if (initialRegion != null) {
            for (String v : initialRegion) {
                if (changedArtifacts.isAddedArtifact(v) || changedArtifacts.isRemovedArtifact(v)) {
                    nbs.add(v);
                }
            }
        }
        nbs.remove(target);
        return nbs;
    }

    // concerned added, removed, modified artifacts
    private void groupChangedArtifact() {
        isMethodMerged = new LinkedHashMap<>();
        HashSet<String> visitedVertexName = new HashSet<>();

        for (String vn : changedArtifacts.getWholeChangedArtifactList()) {

            if (!visitedVertexName.contains(vn)) {
                Set<String> nbs = findVertexNeighboursInBothInitialRegionAndChangedPart(vn);

                if (nbs.size() > 0) {
                    HashSet<String> subGraph = new HashSet<>();
                    subGraph.add(vn);
                    for (String cv : nbs) {
                        visitedVertexName.add(cv);
                        subGraph.add(cv);
                    }

                    changedArtifactsGroup.add(subGraph);
                } else {
                    HashSet<String> region = new HashSet<>();
                    region.add(vn);
                    changedArtifactsGroup.add(region);
                }
            }
        }

        System.out.println("Say you love me");

                //remove all method which is just changed in method body
                removeModifiedArtifacts();

        // remove java method like equals, finalize
        removeJavaSpecificMethod();

        cleanEmptyRegion();

        // Merge left single method(added or removed) into the existed group (the class that method belongs to appears most times)
        mergeSingleMethodToExistedGroup();

        // merge the separated method which is added or removed
        mergeSeparatedMethod();

        // remove the region has only one method, ant it's a java specific method, like hasCode, equals, <init>
        // should <init> to be removed
        removeRegionContainsOnlyOneJavaSpecificMethod();

        cleanEmptyRegion();
    }

    private void mergeSingleMethodToExistedGroup() {
        for (int i = 0; i < changedArtifactsGroup.size(); i++) {
            HashSet<String> region = changedArtifactsGroup.get(i);
            if (region.size() == 1) {
                String singleMethod = null;
                for (String s : region) {
                    singleMethod = s;
                }

                if (changedArtifacts.getAddedArtifactList().contains(singleMethod) || changedArtifacts.getRemovedArtifactList().contains(singleMethod)) {
                    mergeSingleMethodIntoOneExistedRegion(singleMethod, changedArtifactsGroup);
                } else {
                    // ignore method that only changed in method body
                }
            }
        }

        // remove the single method which has already bean merged into other region
        removeMergedMethod();
    }

    private void removeModifiedArtifacts() {
        for (int i = 0; i < changedArtifactsGroup.size(); i++) {
            HashSet<String> region = changedArtifactsGroup.get(i);

            for (String changedMethod : changedArtifacts.getModifiedArtifactList()) {
                region.remove(changedMethod);
            }
        }
    }

    private void removeJavaSpecificMethod() {
        for (int i = 0; i < changedArtifactsGroup.size(); i++) {
            HashSet<String> region = changedArtifactsGroup.get(i);
            Iterator it = region.iterator();

            while (it.hasNext()) {
                String method = (String) it.next();
                String identifier = JavaElement.getIdentifier(method);
                if (identifier.equals("toString") ||
                        identifier.equals("finalize") ||
                        identifier.equals("equals")) {
                    System.out.println("Remove method: " + method);
                    it.remove();
                }
            }
        }
    }

    private void cleanEmptyRegion() {
        Iterator it = changedArtifactsGroup.iterator();
        while (it.hasNext()) {
            HashSet<String> r = (HashSet<String>) it.next();
            if (r.size() == 0) {
                it.remove();
            }
        }
    }

    private void removeMergedMethod() {
        Iterator it = changedArtifactsGroup.iterator();
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
        Iterator it = changedArtifactsGroup.iterator();
        while (it.hasNext()) {
            HashSet<String> r = (HashSet<String>) it.next();
            boolean isAllMethodChangesAreModified = true;
            for (String m : r) {
                if (changedArtifacts.getAddedArtifactList().contains(m) || changedArtifacts.getRemovedArtifactList().contains(m)) {
                    isAllMethodChangesAreModified = false;
                }
            }

            if (isAllMethodChangesAreModified) {
                it.remove();
            }
        }
    }

    private void mergeSeparatedMethod() {
        Map<String, List<String>> separatedClassMethod = new HashMap<>();
        Iterator it = changedArtifactsGroup.iterator();
        while (it.hasNext()) {
            HashSet<String> r = (HashSet<String>) it.next();
            if (r.size() == 1) {
                String m = null;
                for (String s : r) {
                    m = s;
                }

                String className = JavaElement.getClassName(m);
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
            changedArtifactsGroup.add(new HashSet<String>(methods));
        }
    }

    private void removeRegionContainsOnlyOneJavaSpecificMethod() {
        Iterator it = changedArtifactsGroup.iterator();
        while (it.hasNext()) {
            HashSet<String> region = (HashSet<String>) it.next();
            if (region.size() == 1) {
                String methodIdentifier = "";
                for (String s : region) {
                    methodIdentifier = JavaElement.getIdentifier(s);
                }

                if (methodIdentifier.equals("hashCode") || methodIdentifier.equals("<init>")) {
                    it.remove();
                }
            }
        }
    }

    private void mergeSingleMethodIntoOneExistedRegion(String singleMethod, List<HashSet<String>> changeRegion) {

        String className = JavaElement.getClassName(singleMethod);
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
            if (JavaElement.getClassName(s).equals(className)) {
                appearTime++;
            }
        }
        return appearTime;
    }

    public List<HashSet<String>> getChangedArtifactsGroup() {
        return changedArtifactsGroup;
    }
}
