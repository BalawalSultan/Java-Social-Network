package dbproject.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import dbproject.classes.utility.InputReader;

public class ChangeProfileVisibility {
    Connection connection;
    int user_id;

    public ChangeProfileVisibility(Connection connection, int user_id){
        this.connection = connection;
        this.user_id = user_id;
    }

    public void changeVisibility(){
        boolean visibility = getCurrentVisibility();
        String choiche = "";
        String s = "public.";

        if(!visibility)
            s = "not public.";

        do{
            System.out.println("\nCurrently your profile is " + s);
            System.out.printf("Do you want to change it?(yes, no): ");
            choiche = InputReader.readString();
        }while(!choiche.toLowerCase().equals("yes") &&  !choiche.toLowerCase().equals("no"));

        if(choiche.toLowerCase().equals("yes"))  
            changeVisibility(!visibility);    
    }

    private void changeVisibility(boolean visibility){
        String query = "UPDATE Users " +
                       "SET is_profile_public = ? "+
                       "WHERE user_id = ?";

        try{
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setBoolean(1, visibility);
            pstmt.setInt(2, user_id);
            pstmt.executeUpdate();

            pstmt.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean getCurrentVisibility(){
        boolean visibility = false;

        String query = "SELECT is_profile_public " +
                       "FROM Users WHERE user_id = ?";

        try{
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, user_id);

            ResultSet results = pstmt.executeQuery();

            while(results.next())
                visibility = results.getBoolean("is_profile_public");

            pstmt.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return visibility;
    }
}
