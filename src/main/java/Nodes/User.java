package Nodes;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class User implements Serializable, Comparable<User> {
    public long uniqueID;
    public String username;
    public String name;
    public Map<Long, Post> posts = new HashMap<>();
    public Map<Long, User> followers = new HashMap<>();
    public Map<Long, User> followings = new HashMap<>();
    public Map<Long, SubReddit> subReddits = new HashMap<>();



    public User(long uniqueID, String username, String name) {
        this.uniqueID = uniqueID;
        this.username = username;
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return uniqueID == user.uniqueID && Objects.equals(username, user.username) && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueID, username, name);
    }

    @Override
    public int compareTo(User user) {
        return 0;
    }
}
