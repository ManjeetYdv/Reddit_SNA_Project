package OutputClasses;

import Nodes.User;

import java.util.HashMap;
import java.util.Map;

public class UserOutputs {
    public static void printAllUserDetails(HashMap<Long, User>users){
        for(long userid : users.keySet()){
            System.out.println("-----");
            User user=users.get(userid);
//            System.out.println("User id : "+user.uniqueID);
            System.out.println("User Username : "+user.username);
            System.out.println("User Name : "+user.name);
            System.out.println();
        }
    }
    public static void printUserDetailsById(long id, HashMap<Long, User> users) {
        User user = users.get(id);

        if (user != null) {
            System.out.println("-----");
            System.out.println("User ID : " + user.uniqueID);
            System.out.println("Username : " + user.username);
            System.out.println("Name : " + user.name);
            System.out.println();
        } else {
            System.out.println("User with ID " + id + " does not exist.");
        }
    }

    public static void printFollowersById(Long id, HashMap<Long, User> users){
        User user = users.get(id);
        if(user!=null){
            Map<Long, User> followers = user.followers;
            System.out.print("Followers of "+user.name+" : ");
            for(Long key : followers.keySet()){
                System.out.print(followers.get(key).name+" , ");
            }
            System.out.println();
        }
        else{
            System.out.println("User with ID " + id + " does not exist.");
        }
    }
    public static void printFollowingById(Long id, HashMap<Long, User>users){
        User user = users.get(id);
        if(user!=null){
            Map<Long, User> following = user.followings;
            System.out.print("Following of "+user.name+" : ");
            for(Long key : following.keySet()){
                System.out.print(following.get(key).name+" , ");
            }
            System.out.println();
        }
        else{
            System.out.println("User with ID " + id + " does not exist.");
        }
    }
}
