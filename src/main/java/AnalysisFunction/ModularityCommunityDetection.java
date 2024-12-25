package AnalysisFunction;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import java.util.*;

public class ModularityCommunityDetection {

    public static void main(String[] args) {
        // Create the graph (replace this with your actual graph initialization)
        Graph graph = new SingleGraph("ExampleGraph");

        // Add nodes and edges to the graph (modify this as per your graph)
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addNode("E");

        graph.addEdge("AB", "A", "B");
        graph.addEdge("AC", "A", "C");
        graph.addEdge("BD", "B", "D");
        graph.addEdge("CD", "C", "D");
        graph.addEdge("DE", "D", "E");

        // Modularity-based Community Detection
        List<Set<Node>> communities = detectCommunitiesByModularity(graph);

        // Print detected communities
        System.out.println("Detected communities:");
        for (Set<Node> community : communities) {
            System.out.print("Community: ");
            for (Node node : community) {
                System.out.print(node.getId() + " ");
            }
            System.out.println();
        }
    }

    /**
     * Detect communities using Modularity.
     * @param graph - The graph to process.
     * @return List of detected communities.
     */
    public static List<Set<Node>> detectCommunitiesByModularity(Graph graph) {
        List<Set<Node>> communities = new ArrayList<>();
        int m = graph.getEdgeCount(); // Total number of edges
        Map<Node, Integer> nodeDegrees = new HashMap<>(); // Node degrees
        Map<String, Double> edgeWeights = new HashMap<>(); // Edge weights

        // Step 1: Calculate node degrees and store edge weights (for undirected graph, edge weight is always 1)
        for (Node node : graph) {
            nodeDegrees.put(node, node.getDegree());
            for (Edge edge : node.getEachEdge()) {
                String edgeKey = edge.getNode0().getId() + "-" + edge.getNode1().getId();
                edgeWeights.put(edgeKey, 1.0);
            }
        }

        // Step 2: Compute modularity score for different node communities
        // Create a map to track community of each node
        Map<Node, Set<Node>> nodeCommunityMap = new HashMap<>();
        for (Node node : graph) {
            Set<Node> singleNodeCommunity = new HashSet<>();
            singleNodeCommunity.add(node);
            nodeCommunityMap.put(node, singleNodeCommunity);
        }

        // Step 3: Merge communities iteratively based on modularity improvement
        // For simplicity, we just start by merging nodes that have edges between them and check the modularity improvement
        boolean changed;
        do {
            changed = false;
            for (Edge edge : graph.getEdgeSet()) {
                Node node1 = edge.getNode0();
                Node node2 = edge.getNode1();

                Set<Node> community1 = nodeCommunityMap.get(node1);
                Set<Node> community2 = nodeCommunityMap.get(node2);

                if (!community1.equals(community2)) {
                    // Calculate modularity before and after merging communities
                    double currentModularity = calculateModularity(graph, nodeCommunityMap, m, edgeWeights);
                    mergeCommunities(community1, community2, nodeCommunityMap);
                    double newModularity = calculateModularity(graph, nodeCommunityMap, m, edgeWeights);

                    // If modularity improves, accept the merge
                    if (newModularity > currentModularity) {
                        changed = true;
                    } else {
                        // If modularity decreases, revert the merge
                        splitCommunities(community1, community2, nodeCommunityMap);
                    }
                }
            }
        } while (changed);

        // Collect the final communities
        Set<Set<Node>> uniqueCommunities = new HashSet<>(nodeCommunityMap.values());
        communities.addAll(uniqueCommunities);

        return communities;
    }

    /**
     * Merge two communities.
     * @param community1 - The first community to merge.
     * @param community2 - The second community to merge.
     * @param nodeCommunityMap - The map of node to community.
     */
    public static void mergeCommunities(Set<Node> community1, Set<Node> community2, Map<Node, Set<Node>> nodeCommunityMap) {
        community1.addAll(community2);
        for (Node node : community2) {
            nodeCommunityMap.put(node, community1);
        }
    }

    /**
     * Split two merged communities.
     * @param community1 - The first community to split.
     * @param community2 - The second community to split.
     * @param nodeCommunityMap - The map of node to community.
     */
    public static void splitCommunities(Set<Node> community1, Set<Node> community2, Map<Node, Set<Node>> nodeCommunityMap) {
        for (Node node : community1) {
            nodeCommunityMap.put(node, new HashSet<>(Collections.singleton(node)));
        }
        for (Node node : community2) {
            nodeCommunityMap.put(node, new HashSet<>(Collections.singleton(node)));
        }
    }

    /**
     * Calculate modularity score for a given community division.
     * @param graph - The graph to process.
     * @param nodeCommunityMap - The current community mapping.
     * @param m - Total number of edges.
     * @param edgeWeights - The weights of edges.
     * @return The modularity score.
     */
    public static double calculateModularity(Graph graph, Map<Node, Set<Node>> nodeCommunityMap, int m, Map<String, Double> edgeWeights) {
        double modularity = 0.0;

        for (Edge edge : graph.getEdgeSet()) {
            Node node1 = edge.getNode0();
            Node node2 = edge.getNode1();
            double weight = edgeWeights.get(node1.getId() + "-" + node2.getId());

            // Check if the nodes belong to the same community
            if (nodeCommunityMap.get(node1).equals(nodeCommunityMap.get(node2))) {
                modularity += weight - (node1.getDegree() * node2.getDegree() / (2.0 * m));
            } else {
                modularity -= (node1.getDegree() * node2.getDegree() / (2.0 * m));
            }
        }

        return modularity / (2.0 * m);
    }
}