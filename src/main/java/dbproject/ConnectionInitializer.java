package dbproject;


import java.sql.Connection;
import java.sql.DriverManager;
import dbproject.classes.utility.InputReader;

public class ConnectionInitializer {
    
    public static Connection initializeConnection() throws Exception{
        String dbPort, dbName, dbUser, dbPassword;
        Connection connection;

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
    
        connection = DriverManager.getConnection(url);
        
        return connection;
    }

}
