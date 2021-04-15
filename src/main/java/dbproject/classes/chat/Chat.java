package dbproject.classes.chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import dbproject.classes.utility.InputReader;

public class Chat {
    Connection connection;
    Integer user_id;

    public Chat(Connection connection, Integer user_id){
        this.connection = connection;
        this.user_id = user_id;
    }

    public void choseFriendToSendMessageTo() throws Exception{
        HashMap<String,Integer> friends = getFriends();
        String choice = "";

        do{
            System.out.println("Friends");
            for(Map.Entry<String,Integer> entry: friends.entrySet())
                System.out.println(entry.getKey());

            System.out.println("\nWrite the index and name of the person you want to ");
            System.out.printf("chat with or write stop to go back: ");
            choice = InputReader.readString();

            if(friends.containsKey(choice)){
                ChatWithFriend chat = new ChatWithFriend(connection, user_id, friends.get(choice));
                chat.startChat();
            }

        }while(!choice.equals("stop"));
    }

    private HashMap<String,Integer> getFriends() throws Exception{
        HashMap<String,Integer> friends = new HashMap<>();
        String query = "SELECT user_id, name " +
                       "FROM Users " +
                       "JOIN Friend ON user_id = friend AND users = ?";

        try{
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, user_id);

            ResultSet results = pstmt.executeQuery();

            while(results.next()){
                String name = results.getString("name");
                int id = results.getInt("user_id");

                friends.put(id + "-" + name, id);
            }

            pstmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return friends;
    }
}
