package dbproject.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import dbproject.classes.utility.InputReader;

public class FollowUser {
    Connection connection;
    Integer user_id;

    public FollowUser(Connection connection, Integer user_id){
        this.connection = connection;
        this.user_id = user_id;
    }

    public void followUserMenu() throws Exception{
        HashMap<String,Integer> possibleUsers = getPossibleUsers();
        String choice = "";

        do{
            System.out.println("\nPossible Friends");
            for(Map.Entry<String,Integer> entry: possibleUsers.entrySet())
                System.out.println(entry.getKey());

            System.out.println("\nWrite the name of the person you want to follow");
            System.out.printf("or write stop to go back: ");
            choice = InputReader.readString();

            if(possibleUsers.containsKey(choice)){
                followUser(possibleUsers.get(choice));
                possibleUsers.remove(choice);
            }

        }while(!choice.equals("stop"));
    }

    private void followUser(Integer followed_id){
        String query = "INSERT INTO Followers VALUES (?,?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, user_id);
            pstmt.setInt(2, followed_id);
            pstmt.execute();
            pstmt.close();


        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private HashMap<String,Integer> getPossibleUsers() throws Exception{
        HashMap<String,Integer> possibleUsers = new HashMap<>();
        String query = "SELECT user_id, username " +
                       "FROM Users WHERE user_id NOT IN(" +
                            "SELECT followed FROM Followers WHERE follower = ? AND followed <> ?"+
                        ") AND user_id <> ? AND is_profile_public = true";

        try{
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, user_id);
            pstmt.setInt(2, user_id);
            pstmt.setInt(3, user_id);

            ResultSet results = pstmt.executeQuery();

            while(results.next()){
                addToMap(
                    possibleUsers,
                    results.getString("username"),
                    results.getInt("user_id")
                );
            }

            pstmt.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return possibleUsers;
    }

    private void addToMap(HashMap<String,Integer> map, String key, Integer value){
        if(!map.containsKey(key))
            map.put(key,value);
        else
            map.put(key + "-" + value,value);
    }
}
