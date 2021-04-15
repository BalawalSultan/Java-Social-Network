package dbproject.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;

import dbproject.classes.utility.InputReader;

public class Post {
    Connection connection;
    int user_id;

    public Post(Connection connection, int user_id){
        this.connection = connection;
        this.user_id = user_id;
    }
    
    public void createPost(){
        String query = "INSERT INTO Post VALUES (default,?,?,?,?)";

        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            java.util.Date date = new java.util.Date();
            java.sql.Time sqlTime=new java.sql.Time(date.getTime());
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
       
            pstmt.setInt(1, user_id);
            pstmt.setDate(2, sqlDate);
            pstmt.setTime(3, sqlTime);
            pstmt.setString(4, readPostText());
            pstmt.execute();
            pstmt.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private String readPostText(){
        System.out.printf("Insert the text of your post: ");
        return InputReader.readString();
    }
}
