package AnalysisFunction;

import Jama.EigenvalueDecomposition;
import com.lowagie.text.pdf.parser.Matrix;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class EigenVectorCentrality {
    public static double getEigenvectorCentrality(Graph graph, Node targetNode, int iterations) {
        // Get the number of nodes
        int n = graph.getNodeCount();

        // Create adjacency matrix
        double[][] adjMatrix = new double[n][n];
        for (Node node : graph) {
            for (Edge edge : node.getEdgeSet()) {
                int sourceIndex = node.getIndex();
                int targetIndex = edge.getOpposite(node).getIndex();
                adjMatrix[sourceIndex][targetIndex] = 1;  // Assume undirected graph
                adjMatrix[targetIndex][sourceIndex] = 1;  // Symmetric
            }
        }

        // Initialize the eigenvector (start with a random vector)
        double[] eigenvector = new double[n];
        for (int i = 0; i < n; i++) {
            eigenvector[i] = 1.0;  // You can start with any initial vector
        }

        // Power iteration
        for (int iter = 0; iter < iterations; iter++) {
            // Multiply the adjacency matrix by the current eigenvector approximation
            double[] newEigenvector = new double[n];
            for (int i = 0; i < n; i++) {
                newEigenvector[i] = 0;
                for (int j = 0; j < n; j++) {
                    newEigenvector[i] += adjMatrix[i][j] * eigenvector[j];
                }
            }

            // Normalize the new eigenvector
            double norm = 0;
            for (double value : newEigenvector) {
                norm += value * value;
            }
            norm = Math.sqrt(norm);

            for (int i = 0; i < n; i++) {
                newEigenvector[i] /= norm;
            }

            // Update eigenvector approximation
            eigenvector = newEigenvector;
        }

        // Find the index of the target node
        int targetIndex = targetNode.getIndex();

        // Return the eigenvector centrality for the target node
        return eigenvector[targetIndex];
    }
}
