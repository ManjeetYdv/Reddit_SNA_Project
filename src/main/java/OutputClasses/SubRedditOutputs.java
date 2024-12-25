package OutputClasses;

import Nodes.SubReddit;

import java.util.HashMap;

public class SubRedditOutputs {
    public static void printDetailsOfSubReddits(HashMap<Long, SubReddit> subReddit){
        for(Long subRedditId : subReddit.keySet()){
            SubReddit subReddit1 = subReddit.get(subRedditId);
            System.out.println("-------");
            System.out.println("SubReddit name : "+subReddit1.name);
            System.out.println("SubReddit type : "+subReddit1.type);
            System.out.println("SubReddit description : "+subReddit1.description);
            System.out.println();
        }
    }
    public static void printSubRedditDetailsById(Long id, HashMap<Long, SubReddit> subRedditMap) {
        SubReddit subReddit = subRedditMap.get(id);

        if (subReddit != null) {
            System.out.println("-------");
            System.out.println("SubReddit name : " + subReddit.name);
            System.out.println("SubReddit type : " + subReddit.type);
            System.out.println("SubReddit description : " + subReddit.description);
            System.out.println();
        } else {
            System.out.println("SubReddit with ID " + id + " does not exist.");
        }
    }
}
