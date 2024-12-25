package Utility;

import Nodes.User;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.HashMap;
import java.util.Map;

public class BuildVisualsGraphHEHE {
    public static Graph userConnectionGraph(HashMap<Long, User> users) {
        Graph graph = new MultiGraph("Tutorial 1");
        for (Long key : users.keySet())
        {
            graph.addNode(users.get(key).name).setAttribute("ui.label", users.get(key).name);
        }

        for(long key : users.keySet())
        {
            User user=users.get(key);
            Map<Long, User> following=user.followings;
            for(Long followingId : following.keySet()){
                String followName=following.get(followingId).name;
                if(graph.getNode(user.name)!=null && graph.getNode(followName)!=null)
                {
                    graph.addEdge("edge"+user.name+followName, user.name, followName, true);
                }
            }
        }

        return graph;
    }

}
