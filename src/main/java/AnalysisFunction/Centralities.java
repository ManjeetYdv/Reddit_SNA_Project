package AnalysisFunction;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.Node;
import java.util.*;
public class Centralities {
    // Degree Centrality
    public static double calculateDegreeCentrality(Node node, Graph graph) {
        Node maxDegree = findMaxDegreeNode(graph);
        return node.getDegree()/(double)maxDegree.getDegree();
    }
    public static Node findMaxDegreeNode(Graph graph) {
        Node maxNode = null;
        int maxDegree = -1;

        // Iterate through each node to find the maximum degree
        for (Node node : graph) {
            int degree = node.getDegree();
            if (degree > maxDegree) {
                maxDegree = degree;
                maxNode = node;
            }
        }

        return maxNode;
    }

    // Closeness Centrality
    public static double calculateClosenessCentrality(Node node, Graph graph) {
        double totalDistance = 0.0;
        int reachableNodes = 0;

        for (Node other : graph) {
            if (!node.equals(other)) {
                List<Node> path = ShortestPath.dijkstra(graph, node.getId(), other.getId());
                double distance = path.size() - 1; // path length
                if (distance >= 0) {
                    totalDistance += distance;
                    reachableNodes++;
                }
            }
        }

        return reachableNodes > 0 ? (double) reachableNodes / totalDistance : 0.0;
    }

    // Betweenness Centrality
    public static double calculateBetweennessCentrality(Node node, Graph graph) {
        double betweenness = 0.0;

        for (Node s : graph) {
            for (Node t : graph) {
                if (!s.equals(t) && !s.equals(node) && !t.equals(node)) {
                    List<Node> path = ShortestPath.dijkstra(graph, s.getId(), t.getId());
                    if (path != null && path.contains(node)) {
                        betweenness++;
                    }
                }
            }
        }

        return betweenness;
    }
}

// Shortest Path Implementation using Dijkstra's Algorithm
class ShortestPath {
    public static List<Node> dijkstra(Graph graph, String startId, String endId) {
        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Node> predecessors = new HashMap<>();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparing(distances::get));

        for (Node node : graph) {
            distances.put(node, Double.POSITIVE_INFINITY);
            predecessors.put(node, null);
            priorityQueue.add(node);
        }

        distances.put(graph.getNode(startId), 0.0);

        while (!priorityQueue.isEmpty()) {
            Node current = priorityQueue.poll();

            if (current.getId().equals(endId)) {
                break; // Shortest path found
            }

            for (Edge edge : current.getEachEdge()) {
                Node neighbor = edge.getOpposite(current);
                double newDist = distances.get(current) + 1; // Uniform weight of 1

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    predecessors.put(neighbor, current);
                    priorityQueue.remove(neighbor);
                    priorityQueue.add(neighbor);
                }
            }
        }

        // Build the path from endId to startId
        List<Node> path = new ArrayList<>();
        for (Node at = graph.getNode(endId); at != null; at = predecessors.get(at)) {
            path.add(at);
        }
        Collections.reverse(path); // Reverse the path to get it from start to end

        return path;
    }

}
