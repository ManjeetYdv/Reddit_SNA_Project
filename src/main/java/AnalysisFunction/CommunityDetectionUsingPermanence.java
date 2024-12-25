package AnalysisFunction;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.*;
import java.util.ArrayList;

public class CommunityDetectionUsingPermanence {
    public static List<Set<Node>> detectCommunitiesByPermanence(Graph graph) {
        List<Set<Node>> communities = new ArrayList<>();

        // Temporary structure to track nodes' memberships in communities
        Map<Node, Set<Node>> nodeCommunityMap = new HashMap<>();

        // Initialize node communities (every node starts in its own community)
        for (Node node : graph) {
            Set<Node> singleNodeCommunity = new HashSet<>();
            singleNodeCommunity.add(node);
            nodeCommunityMap.put(node, singleNodeCommunity);
        }

        // Track how many common neighbors each pair of nodes has
        Map<String, Integer> edgeStrength = new HashMap<>();

        // Iterate over edges and calculate edge strength (how many common neighbors)
        for (Edge edge : graph.getEdgeSet()) {
            Node node1 = edge.getNode0();
            Node node2 = edge.getNode1();

            // Get neighbors of node1 and node2
            Set<Node> neighbors1 = new HashSet<>();
            node1.getNeighborNodeIterator().forEachRemaining(neighbors1::add); // Add neighbors of node1
            Set<Node> neighbors2 = new HashSet<>();
            node2.getNeighborNodeIterator().forEachRemaining(neighbors2::add); // Add neighbors of node2

            // Calculate the number of common neighbors between node1 and node2
            neighbors1.retainAll(neighbors2); // Get common neighbors

            int commonNeighbors = neighbors1.size();

            // If there are common neighbors, this edge is stronger (higher permanence)
            if (commonNeighbors > 0) {
                edgeStrength.put(node1.getId() + "-" + node2.getId(), commonNeighbors);
            }
        }

        // Define a threshold for edge strength to decide whether to merge communities
        final int threshold = 1; // Change this to make merging stricter

        // Now, let's iterate over all edges and merge communities only if the connection is strong
        for (Edge edge : graph.getEdgeSet()) {
            Node node1 = edge.getNode0();
            Node node2 = edge.getNode1();

            // Get the "strength" of the edge (based on the common neighbors)
            int strength = edgeStrength.getOrDefault(node1.getId() + "-" + node2.getId(), 0);

            // If the edge strength (common neighbors) is above a threshold, merge communities
            if (strength >= threshold) {
                Set<Node> community1 = nodeCommunityMap.get(node1);
                Set<Node> community2 = nodeCommunityMap.get(node2);

                // Only merge communities if they are not already the same
                if (!community1.equals(community2)) {
                    // Merge smaller community into larger one to preserve structure
                    if (community1.size() < community2.size()) {
                        community1.addAll(community2);
                        // Reassign all nodes in community2 to community1
                        for (Node node : community2) {
                            nodeCommunityMap.put(node, community1);
                        }
                    } else {
                        community2.addAll(community1);
                        // Reassign all nodes in community1 to community2
                        for (Node node : community1) {
                            nodeCommunityMap.put(node, community2);
                        }
                    }
                }
            }
        }

        // Collect unique communities
        Set<Set<Node>> uniqueCommunities = new HashSet<>(nodeCommunityMap.values());

        // Add the communities to the result list
        communities.addAll(uniqueCommunities);

        return communities;
    }

}
