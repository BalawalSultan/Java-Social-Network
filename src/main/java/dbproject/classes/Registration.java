package dbproject.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dbproject.classes.utility.InputReader;
import dbproject.classes.utility.Validator;

public class Registration {
    Connection connection;

    public Registration(Connection connection) throws Exception{ 
        this.connection = connection;
    }
  
    public void registerNewUser() throws Exception{
        String query = "INSERT INTO Users VALUES (default,?,?,?,?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);

        pstmt.setString(1, insert_string("username"));
        pstmt.setString(2, select_city());
        pstmt.setInt(3, insert_age());
        pstmt.setString(4, insert_password());
        pstmt.setString(5, insert_mail());
        pstmt.setBoolean(6, is_profile_public());

        pstmt.executeUpdate();

        ResultSet results = pstmt.getGeneratedKeys();
        Integer user_id = null;

        while(results.next())
            user_id = results.getInt("user_id");

        pstmt.close();

        AddPhoneNumber phoneNumber = new AddPhoneNumber(connection, user_id);
        phoneNumber.askForPhoneNumber();
    }

    private String insert_username(){
        System.out.printf("Please insert your username: ");
        String username = InputReader.readString();

        return username;
    }

    private String insert_mail(){
        String mail = "";

        do{
            System.out.printf("Please insert your email: ");
            mail = InputReader.readString();
        }while(!Validator.isMail(mail));

        return mail;
    }

    private String insert_password(){
        String password = "";

        do{
            System.out.println("The password must contain");
            System.out.println("at least one digit [0-9];");
            System.out.println("at least one lowercase Latin character [a-z];");
            System.out.println("at least one uppercase Latin character [A-Z];");
            System.out.println("at least one special character like ! @ # & ( );");
            System.out.println("A length of at least 7 characters and a maximum of 12");
            System.out.printf("Please insert your password: ");
            password = InputReader.readPassword();
            
        }while(!Validator.isValidPassword(password));
        System.out.println("Generating hash...");

        return Validator.hashPassword(password);
    }

    private Boolean is_profile_public(){
        String isPublic = "no";

        do{
            System.out.printf("Is your profile public?(yes, no): ");
            isPublic = InputReader.readString();
        }while(!isPublic.toLowerCase().equals("yes") &&  !isPublic.toLowerCase().equals("no"));

        if(isPublic.toLowerCase().equals("yes"))
            return true;

        return false;
    }

    private int insert_age(){
        int age = 0;

        do{
            System.out.println("Insert you age, it must be greater than 13 and smaller than 120.");
            System.out.printf("Please insert your age: ");
            age = InputReader.readInt();
        }while(age < 14 || age > 120);

        return age;
    }

    private String select_country() throws Exception{
        ArrayList<String> countries = getCountries();
        String country = ""; 

        do{
            System.out.println("Countries");
            for(String string : countries) {
                System.out.println(string);
            }

            System.out.printf("Please chose a country: ");
            country = InputReader.readString();

        }while(!countries.contains(country));

        return country;
    }

    private String select_city() throws Exception{
        String country = select_country();
        HashMap<String,String> cities = getCities(country);
        String city = ""; 
        do{
            System.out.println("CITIES");
            for (Map.Entry<String,String> entry: cities.entrySet())
                System.out.println(entry.getKey() + ", ");

            System.out.printf("Please chose a city: ");
            city = InputReader.readString();

        }while(!cities.containsKey(city));

        return cities.get(city);
    }

    private HashMap<String,String> getCities(String country) throws Exception{
        HashMap<String,String> cities = new HashMap<String,String>();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT cap, name FROM City WHERE country = '" + country + "'");

        while (results.next())
            cities.put(results.getString("name"), results.getString("cap"));
        
        statement.close();
        
        return cities;
    }

    private ArrayList<String> getCountries() throws Exception{
        ArrayList<String> countries = new ArrayList<String>();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("SELECT name FROM Country");

        while (results.next())
            countries.add(results.getString("name"));
        
        statement.close();
        
        return countries;
    }

}
