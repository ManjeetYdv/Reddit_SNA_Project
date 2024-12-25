package AnalysisFunction;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.HashSet;
import java.util.Set;

public class LinkPrediction {
    public static double jaccardCoefficient(Graph graph, Node u, Node v) {
        Set<Node> neighborsU = new HashSet<>();
        Set<Node> neighborsV = new HashSet<>();

        // Get neighbors of u
        for (Edge edge : u.getEdgeSet()) {
            neighborsU.add(edge.getOpposite(u));
        }

        // Get neighbors of v
        for (Edge edge : v.getEdgeSet()) {
            neighborsV.add(edge.getOpposite(v));
        }

        // Find the intersection and union of the neighbors
        Set<Node> intersection = new HashSet<>(neighborsU);
        intersection.retainAll(neighborsV);

        Set<Node> union = new HashSet<>(neighborsU);
        union.addAll(neighborsV);

        // Calculate Jaccard Coefficient
        return (double) intersection.size() / (double) union.size();
    }
}
