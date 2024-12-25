package AnalysisFunction;
import java.util.*;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;
public class PageRank {
    public static Map<Node, Double> calculatePageRank(Graph graph, double dampingFactor, int iterations) {
        Map<Node, Double> pageRank = new HashMap<>();
        int totalNodes = graph.getNodeCount();
        double initialRank = 1.0 / totalNodes;

        // Initialize PageRank values
        for (Node node : graph) {
            pageRank.put(node, initialRank);
        }

        // Iterate to calculate PageRank
        for (int i = 0; i < iterations; i++) {
            Map<Node, Double> newPageRank = new HashMap<>();

            for (Node node : graph) {
                double rankSum = 0.0;

                // Sum the ranks from incoming edges
                for (Edge edge : graph.getEdgeSet()) {
                    if (edge.getTargetNode().equals(node)) {
                        Node source = edge.getSourceNode();
                        rankSum += pageRank.get(source) / source.getDegree();
                    }
                }

                // Calculate new PageRank value
                newPageRank.put(node, (1 - dampingFactor) / totalNodes + dampingFactor * rankSum);
            }

            pageRank = newPageRank; // Update PageRank for next iteration
        }

        return pageRank;
    }
}
