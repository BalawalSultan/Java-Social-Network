package dbproject.classes.chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import dbproject.classes.utility.InputReader;

public class ChatWithFriend {
    Connection connection;
    Integer user_id, friend_id;

    public ChatWithFriend(Connection connection, Integer user_id, Integer friend_id){
        this.connection = connection;
        this.friend_id = friend_id;
        this.user_id = user_id;
    }

    public void startChat() throws Exception{
        String message = "";

        System.out.println("press enter to refresh the chat");
        do{
            showChat();

            System.out.printf("\nwrite a message or write stop to exit: ");
            message = InputReader.readString();

            if(!message.equals("stop") && !message.isEmpty())
                sendMessage(message);

        }while(!message.equals("stop"));
    }

    private void showChat() throws Exception{
        String query = "SELECT * " +
                       "FROM Message " +
                       "WHERE (sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?)";

            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, user_id);
            pstmt.setInt(2, friend_id);
            pstmt.setInt(3, friend_id);
            pstmt.setInt(4, user_id);

            ResultSet results = pstmt.executeQuery();

            while(results.next()){
                String date = results.getString("date");
                String time = results.getString("time");
                String text = results.getString("text");
                int sender = results.getInt("sender");
                printMessage(sender, text, date, time);
            }

            pstmt.close();
    }

    private void printMessage(int sender, String text, String date, String time){
        if(sender == user_id)
            System.out.printf("\n%s %s) You: %s", date, time, text);
        else
            System.out.printf("\n%s %s) Friend: %s", date, time, text);
    }

    private void sendMessage(String message){
        String query = "INSERT INTO Message VALUES (default,?,?,?,?,?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            java.util.Date date = new java.util.Date();	
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            java.sql.Time time = new java.sql.Time(date.getTime());

            pstmt.setInt(1, user_id);
            pstmt.setInt(2, friend_id);
            pstmt.setDate(3, sqlDate);
            pstmt.setTime(4, time);
            pstmt.setString(5, message);
            pstmt.execute();
            pstmt.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
