package Utility;

import Nodes.User;

import java.util.HashMap;
import java.util.Random;

public class BuildUserConnections {
    public static void buildConnections(HashMap<Long, User>allUsers){
        Random random = new Random();
        for(long key : allUsers.keySet()){
            int count = random.nextInt(21) ;// generates random number between 0 to 20
            for(int friend=0;friend<count;friend++){
                Long friendId = (long)random.nextInt(allUsers.size()) + 1;
                if(friendId==key || !allUsers.containsKey(friendId)) continue;
                User newFollowingFriend = allUsers.get(friendId);
                allUsers.get(key).followings.put(friendId, newFollowingFriend);
                newFollowingFriend.followers.put(key, allUsers.get(key));
            }
        }

    }
}
