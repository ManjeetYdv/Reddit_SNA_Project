package Utility;

import Nodes.Post;
import Nodes.SubReddit;
import Nodes.User;

import org.example.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class ReadFile {
    public static void readUsers(HashMap<Long, User> allUsers){
        InputStream inputStream = ReadFile.class.getClassLoader().getResourceAsStream("_500userData.txt");
       // InputStream inputStream = ReadFile.class.getClassLoader().getResourceAsStream("userDataSet.txt");

        if (inputStream == null) {
            System.out.println("File not found");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line into parts
                String[] parts = line.split(" ", 3); // Split into at most 3 parts: id, username, and name

                if (parts.length == 3) {
                    long id = Long.parseLong(parts[0]);
                    String username = parts[1];
                    String name = parts[2];

                    // Print or process the data
                    allUsers.put(id, new User(id, username, name));
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void readSubReddits(HashMap<Long, SubReddit> subReddits){
        InputStream inputStream = ReadFile.class.getClassLoader().getResourceAsStream("subredditData.txt");

        if (inputStream == null) {
            System.out.println("File not found");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by the "." separator
                String[] parts = line.split("\\."); // Use "\\." to escape the dot

                // Check if the split was successful
                if (parts.length == 4) {
                    // Extract fields
                    long id = Long.parseLong(parts[0]);
                    String name = parts[1];
                    String description = parts[2];
                    String type = parts[3];

                    subReddits.put(id, new SubReddit(id, name, description, type));
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void readPostsData(HashMap<Long, Post> allPosts){
        InputStream inputStream = ReadFile.class.getClassLoader().getResourceAsStream("posts.txt");
       // uniqueID,userID,username,postText,subReddit,postDate,upvotes,downvotes,totalComments,engagementScore

        if (inputStream == null) {
            System.out.println("File not found");
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by the "." separator
                String[] parts = line.split(","); // Use "\\." to escape the dot

                // Check if the split was successful
                if (parts.length == 9) {
                    // Extract fields
                    long id = Long.parseLong(parts[0]);
                    String username = parts[1];
                    User postedBy = FindNode.findUserByName(username, App.allUsers);
                    String realname = parts[2];
                    String postText = parts[3];
                    String subRedditName= parts[4];
                    SubReddit subReddit = FindNode.findRedditByName(subRedditName, App.allSubReddits);
                    LocalDateTime localDateTime = LocalDateTime.parse(parts[5], formatter);
                    int upvotes = Integer.parseInt(parts[6]);
                    int downvotes = Integer.parseInt(parts[7]);
                    double engagementScore = Double.parseDouble(parts[8]);

                    Post newPost = new Post(id, postedBy, postText, subReddit, localDateTime, upvotes, downvotes, engagementScore);
                    allPosts.put(id, newPost);

                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
