package OutputClasses;

import Nodes.Post;

import java.util.HashMap;

public class PostOutputs {
    public static void allPostBySubReddit(String subRedditName, HashMap<Long, Post> posts){
        for(Long key : posts.keySet()){
            Post post = posts.get(key);
            if(post.subReddit.name.equals(subRedditName)){
                System.out.println(post.toString());
            }
        }
    }
}
