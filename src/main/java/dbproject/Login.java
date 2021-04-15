package dbproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dbproject.classes.utility.InputReader;
import dbproject.classes.utility.Validator;

public class Login {
    Connection connection;

    public Login(Connection connection){
        this.connection = connection;
    }

    public Integer signIn() throws Exception{
        String mail = insert_mail();
        String password = insert_password();

        String query = "SELECT user_id, password, name FROM Users WHERE mail = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, mail);

        ResultSet result = pstmt.executeQuery();
        Integer id = null;
        while(result.next()){
            String hashedPassword = result.getString("password");
            System.out.println("Verifying your password....");
            if(Validator.compareWithHash(password, hashedPassword)){
                System.out.println("\nWelcome " + result.getString("name"));
                id = result.getInt("user_id");
            }else{
                System.out.println("\nWrong password for mail \" " + mail + "\".");
            }
        }

        pstmt.close();

        return id;
    }

    private String insert_mail(){
        String mail = "";

        System.out.printf("Please insert your email: ");
        mail = InputReader.readString();

        while(!Validator.isMail(mail)){
            System.out.println("The mail you entered is not a valid mail!");
            System.out.printf("Please reinsert your mail: ");
            mail = InputReader.readString();
        }

        return mail;
    }

    private String insert_password(){
        String password = "";

        System.out.printf("Please insert your password: ");
        password = InputReader.readPassword();

        while(!Validator.isValidPassword(password)){
            System.out.println("The password you entered is wrong!");
            System.out.printf("Please reinsert your password: ");
            password = InputReader.readPassword();
        }

        return password;
    }
}
