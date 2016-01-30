package core.algo;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import relation.graph.CodeEdge;
import relation.graph.CodeVertex;

import java.util.LinkedList;

/**
 * Created by niejia on 15/5/11.
 */
public class PathSearch {


    public static void main(String[] args) {

        Graph<CodeVertex, CodeEdge> graph = new UndirectedSparseGraph<CodeVertex, CodeEdge>();

        LinkedList<CodeVertex> visited = new LinkedList();
//        new PathSearch().breadthFirst(graph, visited, "H");
    }

    public void breadthFirst(Graph graph, LinkedList<CodeVertex> visited, CodeVertex end, LinkedList<LinkedList<CodeVertex>> allPath) {

        LinkedList<CodeVertex> nodes = new LinkedList<>();
        for (Object v : graph.getNeighbors(visited.getLast())) {
            nodes.add((CodeVertex) v);
        }

        // examine adjacent nodes
        for (CodeVertex node : nodes) {
            if (visited.contains(node)) {
                continue;
            }


            if (node.equals(end)) {
                visited.add(node);
//                if (visited.size() <= 5) {
//                    printPath(visited);
                LinkedList<CodeVertex> copy = new LinkedList<>(visited);
                allPath.add(copy);
//                }
                visited.removeLast();
                break;
            }
        }

        for (CodeVertex node : nodes) {
            if (visited.contains(node) || node.equals(end)) {
                continue;
            }

            visited.addLast(node);
            if (nodes.size() <= 5) {
                breadthFirst(graph, visited, end,allPath);
            }

            visited.removeLast();
        }
    }

    private void printPath(LinkedList<CodeVertex> visited) {
        for (CodeVertex node : visited) {
            System.out.print(node.getName());
            System.out.print(" ");
        }
        System.out.println();
    }
}
