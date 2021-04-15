package dbproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SessionManager {
    Connection connection;
    int user_id, session_id;

    public SessionManager(Connection connection, int user_id) {
        this.connection = connection;
        this.user_id = user_id;
    }

    public void start_session() throws SQLException{
        String query = "INSERT INTO Session VALUES (default,?,?,null,?)";
        java.util.Date date=new java.util.Date();	
        java.sql.Date sqlDate=new java.sql.Date(date.getTime());
        java.sql.Time sqlTime=new java.sql.Time(date.getTime());

        PreparedStatement pstmt = connection.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
        pstmt.setDate(1, sqlDate);
        pstmt.setTime(2, sqlTime);
        pstmt.setInt(3, user_id);

        if(pstmt.executeUpdate() == 0)
            throw new SQLException("Starting session failed, no rows affected.");
        else{
            ResultSet results = pstmt.getGeneratedKeys();

            if(results.next())
                session_id = results.getInt(1);
        }

        pstmt.close();
    }

    public void close_session() {
        String query = "UPDATE Session SET end_time = ? WHERE session_id = ? AND users = ?";
        java.util.Date date=new java.util.Date();	
        java.sql.Time sqlTime=new java.sql.Time(date.getTime());

        try{
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setTime(1, sqlTime);
            pstmt.setInt(2, session_id);
            pstmt.setInt(3, user_id);
            pstmt.executeUpdate();

            pstmt.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
}
