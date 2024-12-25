package org.example;

import AnalysisFunction.*;
import Nodes.Post;
import Nodes.SubReddit;
import Nodes.User;
import OutputClasses.PostOutputs;
import OutputClasses.SubRedditOutputs;
import OutputClasses.UserOutputs;
import Utility.BuildUserConnections;
import Utility.BuildVisualsGraphHEHE;
import Utility.ReadFile;
import Utility.FindNode;

import org.graphstream.graph.implementations.SingleGraph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.*;
import java.util.stream.Collectors;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.Camera;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;

public class App
{
    public static List<Map.Entry<Node, Double>> getTop3CentralityNodes(Map<Node, Double> centrality) {
        return centrality.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())) // Sort by value in descending order
                .limit(3) // Limit to the top 3 entries
                .collect(Collectors.toList()); // Collect as a list
    }

    public static HashMap<Long, User> allUsers ;
    public static HashMap<Long, SubReddit> allSubReddits ;
    public static HashMap<Long, Post> allPosts;
    public static void main( String[] args ) {
        allUsers = new HashMap<>();
        allSubReddits = new HashMap<>();
        allPosts = new HashMap<>();

        ReadFile.readUsers(allUsers);
//        UserOutputs.printAllUserDetails(allUsers);
//        UserOutputs.printUserDetailsById(12, allUsers);
        ReadFile.readSubReddits(allSubReddits);
//        for(long key: allSubReddits.keySet()) System.out.println(allSubReddits.get(key).name);
//        SubRedditOutputs.
//                printDetailsOfSubReddits(allSubReddits);
//        SubRedditOutputs.printSubRedditDetailsById(12L, allSubReddits);

        //building followers and following connections
        BuildUserConnections.buildConnections(allUsers);

//        for(long userId : allUsers.keySet()){
//            UserOutputs.printFollowersById(userId, allUsers);
//            UserOutputs.printFollowingById(userId, allUsers);
//            System.out.println();
//        }
        ReadFile.readPostsData(allPosts);
        //PostOutputs.allPostBySubReddit("ComicBookFans", allPosts);

      //  graph.display();

        Graph connections= BuildVisualsGraphHEHE.userConnectionGraph(allUsers);System.setProperty("org.graphstream.ui", "swing");
        connections.setStrict(false);
        connections.setAutoCreate( true );

        connections.setAttribute("ui.stylesheet", "url('graph.css')");
        connections.setAttribute("ui.quality");
        connections.setAttribute("ui.antialias");

        Viewer viewer = connections.display();
        // Access the camera
        Camera camera = viewer.getDefaultView().getCamera();

        // Create a JFrame for the UI
        JFrame frame = new JFrame("GraphStream Zoom Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        // Create a zoom in button
        JButton zoomInButton = new JButton("Zoom In");
        zoomInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double currentZoom = camera.getViewPercent();
                camera.setViewPercent(currentZoom * 0.9); // Zoom in by 10%
            }
        });

        // Create a zoom out button
        JButton zoomOutButton = new JButton("Zoom Out");
        zoomOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double currentZoom = camera.getViewPercent();
                camera.setViewPercent(currentZoom * 1.1); // Zoom out by 10%
            }
        });

        // Add buttons to the frame
        JPanel panel = new JPanel();
        panel.add(zoomInButton);
        panel.add(zoomOutButton);
        frame.add(panel);

        // Show the UI
        frame.setVisible(true);

        HashMap<String, Long> timeTaken = new HashMap<>();
        long startTime = System.currentTimeMillis();
        System.out.println("---GLOBAL CLUSTERING COEFFICIENT---");
        System.out.println("Global Clustering Coefficient: A high global clustering coefficient shows a cohesive network with overlapping social circles.");
        System.out.println("Global clustering coefficient of connections graphs is : "+ClusteringCoefficient.calculateGlobalClusteringCoefficient(connections));
        long endTime = System.currentTimeMillis();
        timeTaken.put("Global Clustering Coefficient", endTime-startTime);
        System.out.println();


        Map<Node, Double> pageRank = PageRank.calculatePageRank(connections, 0.85, 100);
        Map<Node, Double> degreeCentrality = new HashMap<>();
        Map<Node, Double> closenessCentrality = new HashMap<>();
        Map<Node, Double> betweennessCentrality = new HashMap<>();
        Map<Node, Double> localClusteringCoefficient = new HashMap<>();
        Map<Node, Double> eigenVectorCentrality = new HashMap<>();
        Map<Node, Double> kartzCentrality = new HashMap<>();

        //finding degree centrality
        startTime = System.currentTimeMillis();
        for (Node node : connections) {
            double degreeCentralityNode = Centralities.calculateDegreeCentrality(node, connections);
            degreeCentrality.put(node, degreeCentralityNode);
        }
        endTime = System.currentTimeMillis();
        timeTaken.put("Degree Centrality", endTime-startTime);

        //finding closeness centrality
        startTime = System.currentTimeMillis();
        for (Node node : connections) {
            double closenessCentralityNode = Centralities.calculateClosenessCentrality(node, connections);
            closenessCentrality.put(node, closenessCentralityNode);
        }
        endTime = System.currentTimeMillis();
        timeTaken.put("Closeness Centrality", endTime-startTime);

        //finding betweeness centrality
        startTime = System.currentTimeMillis();
        for (Node node : connections) {
            double betweennessCentralityNode = Centralities.calculateBetweennessCentrality(node, connections);
            betweennessCentrality.put(node, betweennessCentralityNode);
        }
        endTime = System.currentTimeMillis();
        timeTaken.put("Betweenness Centrality", endTime-startTime);

        //findinglocalClustering Coefficient
        startTime = System.currentTimeMillis();
        for (Node node : connections) {
            double localCLusteringCoefficientNode=  ClusteringCoefficient.calculateLocalClusteringCoefficient(node);
            localClusteringCoefficient.put(node, localCLusteringCoefficientNode);
        }
        endTime = System.currentTimeMillis();
        timeTaken.put("Local Clustering Coefficient", endTime-startTime);

        //finding eigen vector coefficent
        startTime = System.currentTimeMillis();
        for (Node node : connections) {
            double eigenVectorCentralityNode= EigenVectorCentrality.getEigenvectorCentrality(connections, node, 10);
            eigenVectorCentrality.put(node, eigenVectorCentralityNode);
        }
        endTime = System.currentTimeMillis();
        timeTaken.put("Eigen Vector Centrality", endTime-startTime);

        //finding kartz centrality
        startTime = System.currentTimeMillis();
        for (Node node : connections) {
            double kartzCentralityNode=KartzCentrality.calculateKatzCentralityForNode(connections, node, 0.2, 1.0, 10);
            kartzCentrality.put(node, kartzCentralityNode);
        }
        endTime = System.currentTimeMillis();
        timeTaken.put("Kartz Centrality" , endTime-startTime);

        //creating a set of nodes for further use
       List<Node> nodeExample = new ArrayList<>();
        int i=0;
        for(Node node : connections){
            nodeExample.add(node);
            i++;
            if(i==10) break;
        }

//        for(Node node : nodeExample){
//            System.out.println("Node " + node.getId() + ": ");
//            System.out.println("  Degree Centrality : " + degreeCentrality.get(node));
//            System.out.println("  Closeness Centrality: " + closenessCentrality.get(node));
//            System.out.println("  Betweeness Centrality: " + betweennessCentrality.get(node));
//            System.out.println("  Local Clustering Coefficent  : "+ localClusteringCoefficient.get(node));
//            System.out.println("  PageRank : " + pageRank.get(node));
//            System.out.println("  Eigen Vector Centrality : " + eigenVectorCentrality.get(node));
//            System.out.println("  Kartz Centrality Centrality : "+kartzCentrality.get(node));
//            System.out.println();
//        }
        Node nodeForCentrality= nodeExample.get(3);
        System.out.println("Node " + nodeForCentrality.getId() + ": ");
        System.out.println("  Degree Centrality : " + degreeCentrality.get(nodeForCentrality));
        System.out.println("  Closeness Centrality: " + closenessCentrality.get(nodeForCentrality));
        System.out.println("  Betweeness Centrality: " + betweennessCentrality.get(nodeForCentrality));
        System.out.println("  Local Clustering Coefficent  : "+ localClusteringCoefficient.get(nodeForCentrality));
        System.out.println("  PageRank : " + pageRank.get(nodeForCentrality));
        System.out.println("  Eigen Vector Centrality : " + eigenVectorCentrality.get(nodeForCentrality));
        System.out.println("  Kartz Centrality Centrality : "+kartzCentrality.get(nodeForCentrality));
        System.out.println();

        //link prediction
        startTime = System.currentTimeMillis();
        Node nodeForJaccard= nodeExample.get(3);
        for(Node node : nodeExample){
            double jaccardNumber= LinkPrediction.jaccardCoefficient(connections, nodeForJaccard, node);
            System.out.println("Jaccard Coefficient between " + nodeForJaccard.getId()+" and "+ node.getId() + " : " +jaccardNumber);
        }
        endTime = System.currentTimeMillis();
        timeTaken.put("Jaccard Number", endTime-startTime);
        System.out.println();

        startTime = System.currentTimeMillis();
        Node nodeForPathSimilarity= nodeExample.get(1);
        for(Node node : nodeExample){
            int pathCount = AllPathSimilarity.countAllPaths(connections, nodeForPathSimilarity, node, 3);
            System.out.println("All Paths Count Similarity between " +nodeForPathSimilarity.getId() + " and "+node.getId()+" : " +pathCount);
        }
        endTime = System.currentTimeMillis();
        timeTaken.put("Path Sim", endTime-startTime);

        //finding communities
        startTime = System.currentTimeMillis();
        List<Set<Node>> communities = CommunityDetectionUsingPermanence.detectCommunitiesByPermanence(connections);
        endTime = System.currentTimeMillis();
        timeTaken.put("Community Detection", endTime-startTime);
        for(Set<Node> community : communities){
            System.out.print("Community: ");
            for(Node node : community) {
                System.out.print(node.getId()+" ");
            }
            System.out.println();
        }


     //   List<Set<Node>> communities2 = ModularityCommunityDetection.detectCommunitiesByModularity(connections);
//        System.out.println(communities2.size());
//        for(Set<Node> community : communities2){
//            System.out.print("Community: ");
//            for(Node node : community) {
//                System.out.print(node.getId()+" ");
//            }
//            System.out.println();
//        }
        System.out.println();
        for(String key : timeTaken.keySet()){
            System.out.println("Time Taken for "+key+" : "+timeTaken.get(key)+" milliseconds");
        }
        System.out.println();
        System.out.println();

        List<Map.Entry<Node, Double>> top3Nodes = getTop3CentralityNodes(degreeCentrality);

        // Print the top 3 nodes and their centrality values
        System.out.println("Degree Centrality: A high degree centrality indicates a node that is highly connected and potentially influential within the network.");
        for (Map.Entry<Node, Double> entry : top3Nodes) {
            System.out.println("Node: " + entry.getKey().getId() + ", Degree Centrality: " + entry.getValue());
        }
        System.out.println();
        System.out.println("Closeness Centrality: A high closeness centrality indicates a person can reach others quickly, making them a good communicator.");
        List<Map.Entry<Node, Double>> top3Nodes2 = getTop3CentralityNodes(closenessCentrality);
        for (Map.Entry<Node, Double> entry : top3Nodes2) {
            System.out.println("Node: " + entry.getKey().getId() + ", Closeness Centrality: " + entry.getValue());
        }
        System.out.println();
        System.out.println("Betweenness Centrality: A high betweenness centrality indicates a person acts as a bridge, connecting different social groups.");
        List<Map.Entry<Node, Double>> top3Nodes3 = getTop3CentralityNodes(betweennessCentrality);
        for (Map.Entry<Node, Double> entry : top3Nodes3) {
            System.out.println("Node: " + entry.getKey().getId() + ", Betweeness Centrality: " + entry.getValue());
        }
        System.out.println();
        System.out.println("Katz Centrality: A high Katz centrality indicates influence through both direct and indirect connections, suggesting reputation across circles.");
        List<Map.Entry<Node, Double>> top3Nodes4 = getTop3CentralityNodes(kartzCentrality);
        for (Map.Entry<Node, Double> entry : top3Nodes4) {
            System.out.println("Node: " + entry.getKey().getId() + ", Kartz Centrality Centrality: " + entry.getValue());
        }

        System.out.println();
        System.out.println("Local Clustering Coefficient: A high local clustering coefficient shows a person belongs to a close-knit group where their friends know each other.");
        List<Map.Entry<Node, Double>> top3Nodes5 = getTop3CentralityNodes(localClusteringCoefficient);
        for (Map.Entry<Node, Double> entry : top3Nodes5) {
            System.out.println("Node: " + entry.getKey().getId() + ", Local Clustering Centrality Centrality: " + entry.getValue());
        }
    }
}
