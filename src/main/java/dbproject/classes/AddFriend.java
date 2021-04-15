package dbproject.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import dbproject.classes.utility.InputReader;

public class AddFriend {
    Connection connection;
    Integer user_id;

    public AddFriend(Connection connection, Integer user_id){
        this.connection = connection;
        this.user_id = user_id;
    }

    public void chooseFriend() throws Exception{
        HashMap<String,Integer> possibleFriends = getPossibleFriends();
        String choice = "";

        do{
            System.out.println("\nPossible Friends");
            for(Map.Entry<String,Integer> entry: possibleFriends.entrySet())
                System.out.println(entry.getKey());

            System.out.println("\nWrite the name of the person you want to add");
            System.out.printf("add as friend or write stop to go back: ");
            choice = InputReader.readString();

            if(possibleFriends.containsKey(choice)){
                addAsFriend(possibleFriends.get(choice));
                possibleFriends.remove(choice);
            }

        }while(!choice.equals("stop"));
    }

    private void addAsFriend(Integer friend_id){
        String query = "INSERT INTO Friend VALUES (?,?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, user_id);
            pstmt.setInt(2, friend_id);
            pstmt.execute();
            pstmt.close();

            //if A is friend of B, B must also be a friend of A
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, friend_id);
            pstmt.setInt(2, user_id);
            pstmt.execute();
            pstmt.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private HashMap<String,Integer> getPossibleFriends() throws Exception{
        HashMap<String,Integer> possibleFriends = new HashMap<>();
        String query = "SELECT user_id, name " +
                       "FROM Users WHERE user_id NOT IN(" +
                            "SELECT friend FROM Friend WHERE users = ? AND friend <> ?"+
                        ") AND user_id <> ? AND is_profile_public = true";

        try{
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, user_id);
            pstmt.setInt(2, user_id);
            pstmt.setInt(3, user_id);

            ResultSet results = pstmt.executeQuery();

            while(results.next()){
                addToMap(
                    possibleFriends,
                    results.getString("name"),
                    results.getInt("user_id")
                );
            }

            pstmt.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return possibleFriends;
    }

    private void addToMap(HashMap<String,Integer> map, String key, Integer value){
        if(!map.containsKey(key))
            map.put(key,value);
        else
            map.put(key + "-" + value,value);
    }
}
