package Nodes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class SubReddit implements Serializable, Comparable<SubReddit> {


    public long uniqueId;
    public String name;
    public String description;
    public String type;
    public HashMap<Integer, User> users;
    public HashMap<Integer, Post> posts;

    public SubReddit(long uniqueId, String name, String description, String type) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.description = description;
        this.type = type;
        this.users = new HashMap<>();
        this.posts = new HashMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubReddit subReddit = (SubReddit) o;
        return uniqueId == subReddit.uniqueId && Objects.equals(name, subReddit.name) && Objects.equals(description, subReddit.description) && Objects.equals(type, subReddit.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueId, name, description, type);
    }

    @Override
    public int compareTo(SubReddit subReddit) {
        return 0;
    }
}
