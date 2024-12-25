package Nodes;

//import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Comment implements Serializable, Comparable<Comment> {

    private static final long serialVersionUID = 1L;

    private final String content;
    private final String author;
    private final LocalDateTime date;
    private Comment prev;
    private Comment next;
    private Comment reply;
    private Comment lastReply;
    private Comment parent;
    private Post mainPost;
    private int repliesCount;
    /**
     * @param author userid of person who created the comment
     * @param content string storing comment data (text commented by user)
     * @param date datetime of comment created
     * @param parent if parent passed is null it states the comment is direct comment to the post otherwise it is a reply to another comment.
     * */
    public Comment(String content, String author, LocalDateTime date, Comment parent , Post mainPost) {
        this.content = content;
        this.author = author;
        this.date = date;
        this.parent=parent;
        prev=null;
        next=null;
        reply=null;
        lastReply=null;
        this.mainPost = mainPost;
        this.repliesCount=0;
    }

    void incrementRepliesCount() {
        repliesCount++;
        if(parent!=null) {
            parent.incrementRepliesCount();
        }else {
            mainPost.incrementComments();
        }
    }

    void decrementRepliesCount(int count) {
        repliesCount-=count;
        if(parent!=null) {
            parent.decrementRepliesCount(count);
        }else {
            mainPost.decrementComments(count);
        }
    }

    public synchronized void addReply(Comment reply) {
        if(this.reply==null) {
            this.reply=reply;
        }
        else {
            lastReply.next=reply;
            reply.prev=lastReply;
        }
        lastReply=reply;
        incrementRepliesCount();
    }


    public synchronized void removeSelf() {
        if(prev==null) {
            parent.reply = next;
            if(next!=null) {
                next.prev=null;
                next=null;
            }else {
                parent.lastReply=null;
            }

        }
        else {
            prev.next=next;
            if(next!=null) next.prev=prev;
            else parent.lastReply=prev;
            next=null;
            prev=null;

        }
        decrementRepliesCount(repliesCount+1);
        parent=null;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return repliesCount == comment.repliesCount && Objects.equals(content, comment.content) && Objects.equals(author, comment.author) && Objects.equals(date, comment.date) && Objects.equals(prev, comment.prev) && Objects.equals(next, comment.next) && Objects.equals(reply, comment.reply) && Objects.equals(lastReply, comment.lastReply) && Objects.equals(parent, comment.parent) && Objects.equals(mainPost, comment.mainPost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, author, date, prev, next, reply, lastReply, parent, mainPost, repliesCount);
    }

    public Comment getPrev() {
        return prev;
    }

    public Comment getNext() {
        return next;
    }

    public Comment getReply() {
        return reply;
    }

    public Comment getLastReply() {
        return lastReply;
    }

    public Comment getParent() {
        return parent;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public int compareTo(Comment comment) {
        return 0;
    }

    public Post getMainPost() {
        return mainPost;
    }

    public void setMainPost(Post mainPost) {
        this.mainPost = mainPost;
    }


}
