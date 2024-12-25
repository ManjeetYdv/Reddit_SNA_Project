package AnalysisFunction;

import Utility.BuildVisualsGraphHEHE;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;

public class KartzCentrality {
    // Function to calculate Katz centrality for a specific node in the graph
    public static double calculateKatzCentralityForNode(Graph graph, Node node, double alpha, double beta, int maxIterations) {
        if (node == null) {
            System.out.println("Node not found in the graph.");
            return 0.0;
        }

        // Initialize centrality for the specific node
        double centrality = beta;

        // Iterate to calculate centrality score
        for (int i = 0; i < maxIterations; i++) {
            double updatedCentrality = beta;

            // Sum the contributions from neighbors (incoming edges)
            for (Edge edge : node.getEachEnteringEdge()) {
                Node neighbor = edge.getSourceNode();
                updatedCentrality += alpha * centrality; // Contribution from each neighbor
            }

            // Check if centrality has converged (optional)
            if (Math.abs(centrality - updatedCentrality) < 1e-6) {
                break; // Stop iterating if the change is very small
            }

            centrality = updatedCentrality;
        }

        return centrality;
    }

}
