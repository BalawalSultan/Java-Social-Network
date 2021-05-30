package dbproject;


import java.sql.Connection;
import java.sql.DriverManager;
import dbproject.classes.utility.InputReader;

public class ConnectionInitializer {
    
    public static Connection initializeConnection(){
        Connection connection = null;
    
        try{
            String dbPort, dbName, dbUser, dbPassword;

            System.out.printf("\nInsert the port on which your database is located: ");
            dbPort = InputReader.readString();

            System.out.printf("Insert the name of the database you want to use: ");
            dbName = InputReader.readString();

            System.out.printf("Insert the name of the database you want to use: ");
            dbUser = InputReader.readString();

            System.out.printf("Insert the password of the database you selected: ");
            dbPassword = InputReader.readPassword();

            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:" + dbPort 
                       + "/" + dbName + "?user=" + dbUser + "&password=" + dbPassword;
        
            // Get a connection. Since establishing a connection is expensive, we will
            // use one connection for all our statements. We need to ensure that the
            // connection is closed after the use.
            connection = DriverManager.getConnection(url);
        }catch(Exception e){
            System.out.println("An error occurred while connection to the database");
            e.printStackTrace();
        }
        
        return connection;
    }

}
