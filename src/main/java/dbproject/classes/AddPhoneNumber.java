package dbproject.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;

import dbproject.classes.utility.InputReader;
import dbproject.classes.utility.Validator;

public class AddPhoneNumber {
    Connection connection;
    int user_id;

    public AddPhoneNumber(Connection connection, int user_id){
        this.connection = connection;
        this.user_id = user_id;
    }

    public void askForPhoneNumber(){
        String choice = "";

        do{
            System.out.println("\nDo you want to add a phone number?");
            System.out.printf("answer with yes or no: ");
            choice = InputReader.readString();

            if(choice.toLowerCase().equals("yes"))
                insertPhoneNumber();

        }while(!choice.toLowerCase().equals("no"));
    }

    public void insertPhoneNumber(){
        String query = "INSERT INTO Phone_Number VALUES(?,?,?)";
        try{
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, user_id);
            pstmt.setString(2, readPrefix());
            pstmt.setLong(3, readPhoneNumber());

            pstmt.executeUpdate();
            pstmt.close();
        }catch(Exception e){
            System.out.println("Could not add phone number");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public String readPrefix(){
        String prefix = "";

        while(!Validator.isValidPrefix(prefix)){
            System.out.printf("\nInsert the prefix(+XXXX): ");
            prefix = InputReader.readString();
        }

        return prefix;
    }

    public Long readPhoneNumber(){
        String phoneNumber = "";

        while(!Validator.isValidPhoneNumber(phoneNumber)){
            System.out.println("A valid phone number must contain");
            System.out.println(" at least 7 digits and at most 15 digits.");
            System.out.printf("Insert phone number: ");
            phoneNumber = InputReader.readString();
        }

        return Long.valueOf(phoneNumber);
    }
}
