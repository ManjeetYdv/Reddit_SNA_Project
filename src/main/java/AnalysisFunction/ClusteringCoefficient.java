package AnalysisFunction;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;

import java.util.HashSet;
import java.util.Set;
public class ClusteringCoefficient {

    public static double calculateLocalClusteringCoefficient(Node node) {
        Set<Node> neighbors = new HashSet<>();
        for (Edge edge : node.getEachEdge()) {
            neighbors.add(edge.getOpposite(node));
        }

        int k = neighbors.size();
        if (k < 2) {
            return 0; // No neighbors or only one neighbor
        }

        // Count all edges between neighbors
        int edgesBetweenNeighbors = 0;
        for (Node n1 : neighbors) {
            for (Node n2 : neighbors) {
                if (!n1.equals(n2)) {
                    // Count the number of edges between n1 and n2
                    edgesBetweenNeighbors += countEdgesBetween(n1, n2);
                }
            }
        }

        // Use the formula for local clustering coefficient
        return (double) edgesBetweenNeighbors / (k * (k - 1));
    }

    public static int countEdgesBetween(Node n1, Node n2) {
        int count = 0;
        for (Edge edge : n1.getEachEdge()) {
            if (edge.getOpposite(n1).equals(n2)) {
                count++;
            }
        }
        return count;
    }

    public static double calculateGlobalClusteringCoefficient(Graph graph) {
        int closedTriplets = 0;
        int totalTriplets = 0;

        for (Node node : graph) {
            Set<Node> neighbors = new HashSet<>();
            for (Edge edge : node.getEachEdge()) {
                neighbors.add(edge.getOpposite(node));
            }

            int k = neighbors.size();
            if (k < 2) continue; // Not enough neighbors to form triplets

            // Count closed triplets (triangles)
            for (Node n1 : neighbors) {
                for (Node n2 : neighbors) {
                    if (!n1.equals(n2)) {
                        // Count the number of edges between n1 and n2
                        closedTriplets += countEdgesBetween(n1, n2);
                    }
                }
            }

            // Count total triplets
            totalTriplets += (k * (k - 1)) / 2; // Choose 2 neighbors from k
        }

        // Each triangle is counted multiple times, adjust accordingly
        return (double) closedTriplets / totalTriplets;
    }
}
