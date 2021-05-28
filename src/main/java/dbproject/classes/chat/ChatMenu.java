package dbproject.classes.chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import dbproject.classes.utility.InputReader;

public class ChatMenu {
    Connection connection;
    Integer user_id;

    public ChatMenu(Connection connection, Integer user_id){
        this.connection = connection;
        this.user_id = user_id;
    }

    public void startMenu() throws Exception{
        HashMap<String,Integer> followers = getFollowers();
        String choice = "";

        do{
            System.out.println("Followers");
            for(Map.Entry<String,Integer> entry: followers.entrySet())
                System.out.println(entry.getKey());

            System.out.println("\nWrite the index and name of the person you want to ");
            System.out.printf("chat with or write stop to go back: ");
            choice = InputReader.readString();

            if(followers.containsKey(choice)){
                Chat chat = new Chat(connection, user_id, followers.get(choice));
                chat.startChat();
            }

        }while(!choice.equals("stop"));
    }

    private HashMap<String,Integer> getFollowers() throws Exception{
        HashMap<String,Integer> followers = new HashMap<>();
        String query = "SELECT user_id, username " +
                       "FROM Users " +
                       "JOIN Followers ON user_id = follower AND followed = ?";
        try{
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, user_id);

            ResultSet results = pstmt.executeQuery();

            while(results.next()){
                String name = results.getString("username");
                int id = results.getInt("user_id");

                followers.put(id + "-" + name, id);
            }

            pstmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return followers;
    }
}
