package Utility;

import Nodes.SubReddit;
import Nodes.User;

import java.util.HashMap;

public class FindNode {
    public static User findUserByName(String username, HashMap<Long, User> users){
        User user=null;
        for(Long key : users.keySet()){
            if(users.get(key).username.equals(username)){
                user=users.get(key);
                break;
            }
        }
        return user;
    }
    public static SubReddit findRedditByName(String redditname ,HashMap<Long, SubReddit> subReddit){
        SubReddit subRedditHehe=null;
        for(Long key : subReddit.keySet()){
            if(subReddit.get(key).name.equals(redditname)){
                subRedditHehe=subReddit.get(key);
                break;
            }
        }
        return subRedditHehe;
    }
}
