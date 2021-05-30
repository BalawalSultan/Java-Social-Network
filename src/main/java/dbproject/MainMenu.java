package dbproject;

import java.sql.Connection;
import dbproject.classes.Registration;
import dbproject.classes.utility.InputReader;

public class MainMenu {
    Connection conn;

    public MainMenu(Connection conn){
        this.conn = conn;
    }

    public void openMenu(){
        boolean exit_true = false;
        int choice, timeout = 10;

        try {

        while (!exit_true && conn.isValid(timeout)) {
            choice = get_choice();
            switch (choice) {

            case 1:
                Login login = new Login(conn);
                Integer id = login.signIn();

                if (id != null) {
                    UserInterface menu = new UserInterface(conn, id);
                    menu.start_menu();
                    
                }else{
                    System.out.println("There is no user with that email and password!");
                }
                break;

            case 2:
                Registration registration = new Registration(conn);
                registration.registerNewUser();
                break;

            case 3:
                exit_true = true;
                conn.close();
                break;

            default:
                break;
            }
        }

        }catch (Exception e) {

            System.out.println(
                "The transaction failed because the database " + 
                "generated the following exception: " + e.getMessage()
            );

            e.printStackTrace();

            exit_true = true;
        }

    }


    private void print_interface() {
        System.out.println();
        System.out.println("[1]Login");
        System.out.println("[2]Register");
        System.out.println("[3]exit");
    }
    
    private int get_choice() {
        print_interface();
        return InputReader.read_choice(1, 3);
    }
}
