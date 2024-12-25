package Nodes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Post implements Serializable, Comparable<Post> {

    public long uniqueID;
    public User user;
    public String postText;

    @Override
    public String toString() {
        return "Post{" +
                "uniqueID=" + uniqueID +
                ", user=" + user +
                ", postText='" + postText + '\'' +
                ", subReddit=" + subReddit +
                ", postDate=" + postDate +
                ", upvotes=" + upvotes +
                ", downvotes=" + downvotes +
                ", engagementScore=" + engagementScore +
                '}';
    }

    public Post(long uniqueID, User user, String postText, SubReddit subReddit, LocalDateTime postDate, int upvotes, int downvotes, double engagementScore) {
        this.uniqueID = uniqueID;
        this.user = user;
        this.postText = postText;
        this.subReddit = subReddit;
        this.postDate = postDate;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.engagementScore = engagementScore;
    }

    public SubReddit subReddit;
    public LocalDateTime postDate;
    public int upvotes;
    public int downvotes;
   // public Comment comments;
    public  int totalComments;
    public double engagementScore;

    // Function to calculate engagement
    public void calculateEngagement() {
        // Time since the post was created
        long hoursSincePost = ChronoUnit.HOURS.between(postDate, LocalDateTime.now());

        // Safeguard against division by zero
        if (hoursSincePost == 0) {
            hoursSincePost = 1;
        }

        // Number of comments
        int numComments = totalComments;

        // Engagement formula

        this.engagementScore =  (upvotes - downvotes) + (numComments * 1.5) / Math.log(hoursSincePost + 2);
    }


    public void incrementComments() {
        this.totalComments++;
    }

    public void decrementComments(int count) {
        this.totalComments-=count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return uniqueID == post.uniqueID && upvotes == post.upvotes && downvotes == post.downvotes && Objects.equals(user, post.user) && Objects.equals(postText, post.postText) && Objects.equals(subReddit, post.subReddit) && Objects.equals(postDate, post.postDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueID, user, postText, subReddit, postDate, upvotes, downvotes);
    }

    @Override
    public int compareTo(Post post) {
        return 0;
    }
}
