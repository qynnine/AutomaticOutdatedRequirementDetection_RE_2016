package relation;

import callGraph.JCallGraph;
import core.type.Granularity;
import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.algorithms.scoring.PageRankWithPriors;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import io.ChangedArtifacts;
import org.apache.commons.collections15.functors.MapTransformer;
import relation.info.CallRelation;
import relation.info.CallRelationList;
import relation.info.RelationPair;

import java.io.Serializable;
import java.util.*;

/**
 * Created by niejia on 15/2/26.
 */
public class RelationInfo implements Serializable {
    private List<RelationPair> callRelationPairList;
    private Map<RelationPair, CallRelationList> pairCallRelationListMap;

    private Map<Integer, String> vertexIdNameMap;
    private Map<String, Integer> vertexNameIdMap;

    private HashSet<String> artifactNames;
    private HashSet<String> changedArtifactList;


    private ChangedArtifacts changedArtifacts;

    private Granularity granularity;

    public static String[] internalPackageName = {"edu.ncsu.csc.itrust","ui","device","irrigation","simulation",
    "startup","util","gov"};
    private double callEdgeScoreThreshold;
    private boolean isPruning;

    private boolean isExternalPackageEnable;
    private boolean isCompletedGraph;

    public RelationInfo(String jarPath,  Granularity granularity, boolean isExternalPackageEnable) {
        this.granularity = granularity;
        artifactNames = new LinkedHashSet<>();
        this.isExternalPackageEnable = isExternalPackageEnable;

        JCallGraph callGraph = new JCallGraph(jarPath);
        Hashtable<String, Vector<String>> callGraphMap = JCallGraph.callGraphMap;
        Hashtable<String, String> superClassesTable = callGraph.superClasses;


        for (String s : superClassesTable.keySet()) {
            if (callGraphMap.containsKey(s)) {
                Vector<String> v = callGraphMap.get(s);
                String father = superClassesTable.get(s);
                if (!v.contains(father)) {
                    v.add(father);
                }
                callGraphMap.put(s, v);
            } else {
                Vector<String> v = new Vector<>();
                v.add(superClassesTable.get(s));
                callGraphMap.put(s, v);
            }
        }

        for (String name : callGraphMap.keySet()) {
            String artName = null;
            if (granularity.equals(Granularity.CLASS)) {
                artName = extractClassName(name);
            } else if (granularity.equals(Granularity.METHOD)) {
                artName = name;
            }

            if (artName.equals("edu.ncsu.csc.itrust.action.UpdateReasonCodeListAction.<init>")) {
                System.out.println("haha");
            }

            if (isInternalPackage(artName) ) {
                artifactNames.add(artName);
            }
            Vector<String> callees = callGraphMap.get(name);
            for (String callee : callees) {
                if (granularity.equals(Granularity.CLASS)) {
                    artName = extractClassName(callee);
                } else if (granularity.equals(Granularity.METHOD)) {
                    artName = callee;
                }

                if (isInternalPackage(artName) ) {
                    artifactNames.add(artName);
                }
            }
        }

        vertexIdNameMap = new LinkedHashMap<>();
        vertexNameIdMap = new LinkedHashMap<>();

        int id = 1;
        for (String name : artifactNames) {
            vertexIdNameMap.put(id, name);
            vertexNameIdMap.put(name, id);
            id++;
        }

        CallRelationList callRelationList = new CallRelationList();

        for (String caller : callGraphMap.keySet()) {
            Vector<String> callees = callGraphMap.get(caller);

            String callerName = null;
            if (granularity.equals(Granularity.CLASS)) {
                callerName = extractClassName(caller);
            } else if (granularity.equals(Granularity.METHOD)) {
                callerName = caller;
            }

            for (String callee : callees) {
                String calleeName = null;
                if (granularity.equals(Granularity.CLASS)) {
                    calleeName = extractClassName(callee);
                } else if (granularity.equals(Granularity.METHOD)) {
                    calleeName = callee;
                }

                if ((isInternalPackage(callerName) && isInternalPackage(calleeName)) ) {
                    CallRelation cr = new CallRelation(callerName, calleeName, caller, callee);
                    if (!callRelationList.contains(cr)) {
                        callRelationList.add(cr);
                    }
                }
            }
        }

        pairCallRelationListMap = new LinkedHashMap<>();
        callRelationPairList = new ArrayList<>();
        List<String> callRelationById = new ArrayList<>();

        for (CallRelation cr : callRelationList) {
            String caller;
            String callee;

            caller = cr.getCallerClass();
            callee = cr.getCalleeClass();

            Integer callerId = vertexNameIdMap.get(caller);
            Integer calleeId = vertexNameIdMap.get(callee);

            String relationIdFormat = callerId + " " + calleeId;

            RelationPair rp = new RelationPair(callerId, calleeId);

            if (callerId != calleeId) {
                if (pairCallRelationListMap.containsKey(rp)) {
                    CallRelationList callRelationListForPair = pairCallRelationListMap.get(rp);
                    callRelationListForPair.add(cr);
                    pairCallRelationListMap.put(rp, callRelationListForPair);
                } else {
                    CallRelationList callRelationListForPair = new CallRelationList();
                    callRelationListForPair.add(cr);
                    pairCallRelationListMap.put(rp, callRelationListForPair);
                }
            }

            if (!callRelationById.contains(relationIdFormat) && callerId != calleeId) {

                callRelationById.add(relationIdFormat);
                getCallRelationPairList().add(rp);
            } else {
//                    System.out.println(relationIdFormat + " class call relation is duplicated.");
            }
        }
    }

    // not concerned modified vertexes
    public RelationInfo(Boolean concernedModifiedArtifact, String oldVersionPath, String newVersionPath, String changedClassPath, Granularity granularity) {
        this.granularity = granularity;
        artifactNames = new LinkedHashSet<>();

        ChangedArtifacts parser = new ChangedArtifacts();
        parser.parse(changedClassPath);
        this.changedArtifactList = parser.getWholeChangedArtifactList();

        this.changedArtifacts = parser;

        JCallGraph oldCallGraph = new JCallGraph(oldVersionPath);
        Hashtable<String, Vector<String>> oldCallGraphMap = JCallGraph.callGraphMap;

        JCallGraph newCallGraph = new JCallGraph(newVersionPath);
        Hashtable<String, Vector<String>> newCallGraphMap = JCallGraph.callGraphMap;

        if (!concernedModifiedArtifact) {
            removeModifiedArtifactInMap(oldCallGraphMap);
            removeModifiedArtifactInMap(newCallGraphMap);
        }

        for (String name : oldCallGraphMap.keySet()) {
            String artName = null;
            if (granularity.equals(Granularity.CLASS)) {
                artName = extractClassName(name);
            } else if (granularity.equals(Granularity.METHOD)) {
                artName = name;
            }

            if (isInternalPackageAndTarget(artName)) {
                    artifactNames.add(artName);
            }
            Vector<String> callees = oldCallGraphMap.get(name);
            for (String callee : callees) {
                if (granularity.equals(Granularity.CLASS)) {
                    artName = extractClassName(callee);
                } else if (granularity.equals(Granularity.METHOD)) {
                    artName = callee;
                }

                if (isInternalPackageAndTarget(artName) ) {
                    artifactNames.add(artName);
                }
            }
        }

        for (String name : newCallGraphMap.keySet()) {
            String artName = null;
            if (granularity.equals(Granularity.CLASS)) {
                artName = extractClassName(name);
            } else if (granularity.equals(Granularity.METHOD)) {
                artName = name;
            }

            if (artName.equals("ui.SetMaxLevelScrnState.setCurrentItem")) {
                System.out.println("hahaa");
            }
            if (isInternalPackageAndTarget(artName) ) {
                artifactNames.add(artName);
            }
            Vector<String> callees = newCallGraphMap.get(name);
            for (String callee : callees) {
                if (granularity.equals(Granularity.CLASS)) {
                    artName = extractClassName(callee);
                } else if (granularity.equals(Granularity.METHOD)) {
                    artName = callee;
                }

                if (isInternalPackageAndTarget(artName)) {
                    artifactNames.add(artName);
                }
            }
        }

        vertexIdNameMap = new LinkedHashMap<>();
        vertexNameIdMap = new LinkedHashMap<>();

        int id = 1;
        for (String name : artifactNames) {
            vertexIdNameMap.put(id, name);
            vertexNameIdMap.put(name, id);
            id++;
        }

        CallRelationList callRelationList = new CallRelationList();

        for (String caller : oldCallGraphMap.keySet()) {
            Vector<String> callees = oldCallGraphMap.get(caller);

            String callerName = null;
            if (granularity.equals(Granularity.CLASS)) {
                callerName = extractClassName(caller);
            } else if (granularity.equals(Granularity.METHOD)) {
                callerName = caller;
            }

            for (String callee : callees) {
                String calleeName = null;
                if (granularity.equals(Granularity.CLASS)) {
                    calleeName = extractClassName(callee);
                } else if (granularity.equals(Granularity.METHOD)) {
                    calleeName = callee;
                }

                if ((isInternalPackageAndTarget(callerName) && isInternalPackageAndTarget(calleeName)) ) {
                    CallRelation cr = new CallRelation(callerName, calleeName, caller, callee);
                    if (!callRelationList.contains(cr)) {
                        callRelationList.add(cr);
                    }
                }
            }
        }

        for (String caller : newCallGraphMap.keySet()) {
            Vector<String> callees = newCallGraphMap.get(caller);

            String callerName = null;
            if (granularity.equals(Granularity.CLASS)) {
                callerName = extractClassName(caller);
            } else if (granularity.equals(Granularity.METHOD)) {
                callerName = caller;
            }

            for (String callee : callees) {

                String calleeName = null;
                if (granularity.equals(Granularity.CLASS)) {
                    calleeName = extractClassName(callee);
                } else if (granularity.equals(Granularity.METHOD)) {
                    calleeName = callee;
                }

                if ((isInternalPackageAndTarget(callerName) && isInternalPackageAndTarget(calleeName)) ) {
                    CallRelation cr = new CallRelation(callerName, calleeName, caller, callee);
                    if (!callRelationList.contains(cr)) {
                        callRelationList.add(cr);
                    }
                }
            }
        }

        pairCallRelationListMap = new LinkedHashMap<>();
        callRelationPairList = new ArrayList<>();
        List<String> callRelationById = new ArrayList<>();

        for (CallRelation cr : callRelationList) {
            String caller;
            String callee;

            caller = cr.getCallerClass();
            callee = cr.getCalleeClass();

            Integer callerId = vertexNameIdMap.get(caller);
            Integer calleeId = vertexNameIdMap.get(callee);

            String relationIdFormat = callerId + " " + calleeId;

            RelationPair rp = new RelationPair(callerId, calleeId);

            if (callerId != calleeId) {
                if (pairCallRelationListMap.containsKey(rp)) {
                    CallRelationList callRelationListForPair = pairCallRelationListMap.get(rp);
                    callRelationListForPair.add(cr);
                    pairCallRelationListMap.put(rp, callRelationListForPair);
                } else {
                    CallRelationList callRelationListForPair = new CallRelationList();
                    callRelationListForPair.add(cr);
                    pairCallRelationListMap.put(rp, callRelationListForPair);
                }
            }

            if (!callRelationById.contains(relationIdFormat) && callerId != calleeId) {

                callRelationById.add(relationIdFormat);
                getCallRelationPairList().add(rp);
            } else {
//                    System.out.println(relationIdFormat + " class call relation is duplicated.");
            }
        }

        // check if there are some vertexes has no call neighbours, so they are not added into the graph
        for (String artifact : changedArtifacts.getWholeChangedArtifactList()) {
            if (changedArtifacts.isAddedArtifact(artifact) || changedArtifacts.isRemovedArtifact(artifact)) {
                if (!vertexNameIdMap.containsKey(artifact)) {
                    System.out.println(("Not considering such element " + artifact));
                    vertexIdNameMap.put(id, artifact);
                    vertexNameIdMap.put(artifact, id);
                    id++;
                }
            }

        }
    }

    private void removeModifiedArtifactInMap(Hashtable<String, Vector<String>> graphMap) {
        Iterator it_key = graphMap.keySet().iterator();
        while (it_key.hasNext()) {
            String r = (String) it_key.next();
            if (changedArtifacts.isModifiedArtifact(r)) {
               it_key.remove();
            }
        }

        Iterator it_value = graphMap.values().iterator();
        while (it_value.hasNext()) {
            Vector<String> r = (Vector<String>) it_value.next();

            Iterator i = r.iterator();
            while (i.hasNext()) {
                String t = (String) i.next();
                if (changedArtifacts.isModifiedArtifact(t)) {
                    i.remove();
                }
            }
        }
    }

    // concerned added, removed, modified vertexes
    public RelationInfo(String oldVersionPath, String newVersionPath, String changedClassPath, Granularity granularity, boolean isCompletedGraph) {
        this.granularity = granularity;
        artifactNames = new LinkedHashSet<>();
        this.isCompletedGraph = isCompletedGraph;
//        this.modifiedArtifactList = ChangesParser.fetchArtifacts(changedClassPath);

        ChangedArtifacts parser = new ChangedArtifacts();
        parser.parse(changedClassPath);
        this.changedArtifactList = parser.getWholeChangedArtifactList();
        this.changedArtifacts = parser;

        JCallGraph oldCallGraph = new JCallGraph(oldVersionPath);
        Hashtable<String, Vector<String>> oldCallGraphMap = JCallGraph.callGraphMap;

        JCallGraph newCallGraph = new JCallGraph(newVersionPath);
        Hashtable<String, Vector<String>> newCallGraphMap = JCallGraph.callGraphMap;

        for (String name : oldCallGraphMap.keySet()) {
            String artName = null;
            if (granularity.equals(Granularity.CLASS)) {
                artName = extractClassName(name);
            } else if (granularity.equals(Granularity.METHOD)) {
                artName = name;
            }

            if (isInternalPackageAndTarget(artName) ) {
                artifactNames.add(artName);
            }
            Vector<String> callees = oldCallGraphMap.get(name);
            for (String callee : callees) {
                if (granularity.equals(Granularity.CLASS)) {
                    artName = extractClassName(callee);
                } else if (granularity.equals(Granularity.METHOD)) {
                    artName = callee;
                }

                if (isInternalPackageAndTarget(artName) ) {
                    artifactNames.add(artName);
                }
            }
        }

        for (String name : newCallGraphMap.keySet()) {
            String artName = null;
            if (granularity.equals(Granularity.CLASS)) {
                artName = extractClassName(name);
            } else if (granularity.equals(Granularity.METHOD)) {
                artName = name;
            }

            if (artName.equals("ui.SetMaxLevelScrnState.setCurrentItem")) {
                System.out.println("hahaa");
            }
            if (isInternalPackageAndTarget(artName) ) {
                artifactNames.add(artName);
            }
            Vector<String> callees = newCallGraphMap.get(name);
            for (String callee : callees) {
                if (granularity.equals(Granularity.CLASS)) {
                    artName = extractClassName(callee);
                } else if (granularity.equals(Granularity.METHOD)) {
                    artName = callee;
                }

                if (isInternalPackageAndTarget(artName)) {
                    artifactNames.add(artName);
                }
            }
        }

        vertexIdNameMap = new LinkedHashMap<>();
        vertexNameIdMap = new LinkedHashMap<>();

        int id = 1;
        for (String name : artifactNames) {
            vertexIdNameMap.put(id, name);
            vertexNameIdMap.put(name, id);
            id++;
        }

        CallRelationList callRelationList = new CallRelationList();

        for (String caller : oldCallGraphMap.keySet()) {
            Vector<String> callees = oldCallGraphMap.get(caller);

            String callerName = null;
            if (granularity.equals(Granularity.CLASS)) {
                callerName = extractClassName(caller);
            } else if (granularity.equals(Granularity.METHOD)) {
                callerName = caller;
            }

            for (String callee : callees) {
                String calleeName = null;
                if (granularity.equals(Granularity.CLASS)) {
                    calleeName = extractClassName(callee);
                } else if (granularity.equals(Granularity.METHOD)) {
                    calleeName = callee;
                }

                if ((isInternalPackageAndTarget(callerName) && isInternalPackageAndTarget(calleeName)) ) {
                    CallRelation cr = new CallRelation(callerName, calleeName, caller, callee);
                    if (!callRelationList.contains(cr)) {
                        callRelationList.add(cr);
                    }
                }
            }
        }

        for (String caller : newCallGraphMap.keySet()) {
            Vector<String> callees = newCallGraphMap.get(caller);

            String callerName = null;
            if (granularity.equals(Granularity.CLASS)) {
                callerName = extractClassName(caller);
            } else if (granularity.equals(Granularity.METHOD)) {
                callerName = caller;
            }

            for (String callee : callees) {

                String calleeName = null;
                if (granularity.equals(Granularity.CLASS)) {
                    calleeName = extractClassName(callee);
                } else if (granularity.equals(Granularity.METHOD)) {
                    calleeName = callee;
                }

                if ((isInternalPackageAndTarget(callerName) && isInternalPackageAndTarget(calleeName)) ) {
                    CallRelation cr = new CallRelation(callerName, calleeName, caller, callee);
                    if (!callRelationList.contains(cr)) {
                        callRelationList.add(cr);
                    }
                }
            }
        }

        pairCallRelationListMap = new LinkedHashMap<>();
        callRelationPairList = new ArrayList<>();
        List<String> callRelationById = new ArrayList<>();

        for (CallRelation cr : callRelationList) {
            String caller;
            String callee;

            caller = cr.getCallerClass();
            callee = cr.getCalleeClass();

            Integer callerId = vertexNameIdMap.get(caller);
            Integer calleeId = vertexNameIdMap.get(callee);

            String relationIdFormat = callerId + " " + calleeId;

            RelationPair rp = new RelationPair(callerId, calleeId);

            if (callerId != calleeId) {
                if (pairCallRelationListMap.containsKey(rp)) {
                    CallRelationList callRelationListForPair = pairCallRelationListMap.get(rp);
                    callRelationListForPair.add(cr);
                    pairCallRelationListMap.put(rp, callRelationListForPair);
                } else {
                    CallRelationList callRelationListForPair = new CallRelationList();
                    callRelationListForPair.add(cr);
                    pairCallRelationListMap.put(rp, callRelationListForPair);
                }
            }

            if (!callRelationById.contains(relationIdFormat) && callerId != calleeId) {

                callRelationById.add(relationIdFormat);
                getCallRelationPairList().add(rp);
            } else {
//                    System.out.println(relationIdFormat + " class call relation is duplicated.");
            }
        }

        // check if there are some vertexes has no call neighbours, so they are not added into the graph
        for (String artifact : changedArtifacts.getWholeChangedArtifactList()) {
            if (changedArtifacts.isAddedArtifact(artifact) || changedArtifacts.isRemovedArtifact(artifact)) {
                if (!vertexNameIdMap.containsKey(artifact)) {
                    System.out.println(("Not considering such element " + artifact));
                    vertexIdNameMap.put(id, artifact);
                    vertexNameIdMap.put(artifact, id);
                    id++;
                }
            }

        }
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

    public Map<Integer, String> getVertexes() {
        return vertexIdNameMap;
    }

    public Integer getVertexIdByName(String vertexName) {
        return vertexNameIdMap.get(vertexName);
    }

    public String getVertexNameById(Integer id) {
        return vertexIdNameMap.get(id);
    }

    public void showMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(getCallRelationPairList().size() + " call relation pairs, ");
        System.out.println(sb.toString());
    }

    public List<RelationPair> getCallRelationPairList() {
        return callRelationPairList;
    }

    public CallRelationList getCallRelationListForRelationPair(RelationPair pair) {
        return pairCallRelationListMap.get(pair);
    }

    public Map<String, Number> getPageRank() {
        Graph<Integer, Integer> graph = new DirectedSparseGraph();

        for (int i = 1; i < vertexIdNameMap.size(); i++) {
            graph.addVertex(i);
        }

        Map<Integer, Integer> outCount = new HashMap<>();
        for (int i = 0; i < callRelationPairList.size(); i++) {
            RelationPair pair = callRelationPairList.get(i);
            if (!outCount.containsKey(pair.getKey())) {
                outCount.put(pair.getKey(), 1);
            } else {
                outCount.put(pair.getKey(), outCount.get(pair.getKey()) + 1);
            }
        }

        Map<Integer, Number> outPossible = new HashMap<>();
        for (Integer i : outCount.keySet()) {
            outPossible.put(i, 1.0 / outCount.get(i));
        }

        Map<Integer, Number> edgeWeights = new HashMap<>();
        int edgeN = 1;
        for (int i = 0; i < callRelationPairList.size(); i++) {
            graph.addEdge(edgeN, callRelationPairList.get(i).getKey(), callRelationPairList.get(i).getValue());
            edgeWeights.put(edgeN, outPossible.get(callRelationPairList.get(i).getKey()));
            edgeN++;
        }

        PageRankWithPriors<Integer, Integer> pr = new PageRank<Integer, Integer>(graph, MapTransformer.getInstance(edgeWeights), 0);
        pr.evaluate();

        Map<String, Number> vertexWeights = new HashMap<>();

        for (Integer v : graph.getVertices()) {

            vertexWeights.put(vertexIdNameMap.get(v), pr.getVertexScore(v));
        }
        return vertexWeights;
    }

    public boolean isInternalPackage(String target) {
        if (granularity.equals(Granularity.CLASS)) {
            for (String p : internalPackageName) {
                if (target.startsWith(p)) {
                    return true;
                }
            }
            return false;
        }
        // method
        else {
            for (String p : internalPackageName) {
                if (target.startsWith(p)) {
                    return true;
                }
            }
            return false;
        }
    }

    // isCompletedGraph is false, then the graph vertex only contains the changed part;
    // isCompletedGraph is true, then the graph contains all the vertex in two version;
    public boolean isInternalPackageAndTarget(String target) {

        if (granularity.equals(Granularity.CLASS)) {
            for (String p : internalPackageName) {
                if (target.startsWith(p)) {
                    if (isCompletedGraph) {
                        return true;
                    } else {
                        if (changedArtifactList.contains(target)) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
            return false;
        }
        // method
        else {
//            if (isExternalPackageEnable) {
//                if (!isClassArtifact(target)) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }

            for (String p : internalPackageName) {
                if (target.startsWith(p) && !isClassArtifact(target)) {
                    if (isCompletedGraph) {
                        return true;
                    } else {
                        if (changedArtifactList.contains(target)) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
            return false;
        }
    }

    private boolean isClassArtifact(String artifact) {
        String[] tokens = artifact.split("\\.");
        String lastToken = tokens[tokens.length - 1];
        return Character.isUpperCase(lastToken.charAt(0));
    }

    public boolean isContainedInChangeList(String target) {
        for (String str : changedArtifactList) {
            if (target.startsWith(str)) {
                return true;
            }
        }
        return false;
    }

    public double getCallEdgeScoreThreshold() {
        return callEdgeScoreThreshold;
    }

    public void setPruning(double callEdgeScoreThreshold) {
        this.isPruning = true;
        this.callEdgeScoreThreshold = callEdgeScoreThreshold;
    }

    public void setEnableExternalPackage() {
        isExternalPackageEnable = true;
    }

    public boolean isPruning() {
        return isPruning;
    }

//    public void setCompletedGraph(boolean isCompletedGraph) {
//        this.isCompletedGraph = isCompletedGraph;
//    }

//    public static void main(String[] args) {
//        String classDirPath = "data/exp/iTrust/class/code";
//        String relationDirPath = "data/exp/iTrust/relation";
//        RelationInfo rg = new RelationInfo(classDirPath, relationDirPath);
//        rg.showMessage();
//    }
}
