package dbproject.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ShowTimeSpent{
    Connection connection;
    int user_id;

    public ShowTimeSpent(Connection connection, int user_id){
        this.connection = connection;
        this.user_id = user_id;
    }

    public void printTimeSpent() throws Exception{
        java.util.Date date = new java.util.Date();	
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        String query = "SELECT "+
                            "SUM(CASE WHEN end_time <> null " +
                                    "THEN (end_time - start_time) " +
                                    "ELSE (LOCALTIME - start_time) "+
                                "END) AS timeSpent " +
                       "FROM Session WHERE users = ? AND date = ?";

        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, user_id);
        pstmt.setDate(2, sqlDate);

        ResultSet result = pstmt.executeQuery();
        String timeSpent = null;
        while(result.next())
            timeSpent = result.getString("timeSpent");

        if(timeSpent == null)
            System.out.println("You just logged in!");
        else{
            String[] time = timeSpent.split("\\.");
            System.out.printf("\nThe time you spent on Twitaroo today is %s.", time[0]);
        }
        
    }
}