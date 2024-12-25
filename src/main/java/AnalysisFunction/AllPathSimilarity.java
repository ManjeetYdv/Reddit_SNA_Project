package AnalysisFunction;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import java.util.HashSet;
import java.util.Set;

public class AllPathSimilarity {


    public static int countAllPaths(Graph graph, Node startNode, Node endNode, int maxLength) {
        Set<Node> visited = new HashSet<>();

        return countPathsRecursive(startNode, endNode, maxLength, visited);
    }

    /**
     * Helper recursive function to count paths.
     * @param current - Current node in the path.
     * @param target - Target node.
     * @param remainingLength - Remaining length allowed in the path.
     * @param visited - Set of visited nodes to avoid cycles.
     * @return Number of paths from current to target within remainingLength.
     */
    private static int countPathsRecursive(Node current, Node target, int remainingLength, Set<Node> visited) {
        if (remainingLength < 0) return 0;
        if (current.equals(target) && remainingLength >= 0) return 1;

        visited.add(current);
        int pathCount = 0;

        for (Edge edge : current.getEachEdge()) {
            Node neighbor = edge.getOpposite(current);
            if (!visited.contains(neighbor)) {
                pathCount += countPathsRecursive(neighbor, target, remainingLength - 1, visited);
            }
        }

        visited.remove(current);
        return pathCount;
    }
}
