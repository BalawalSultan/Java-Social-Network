package dbproject;


import java.sql.Connection;

import dbproject.classes.Registration;
import dbproject.classes.utility.InputReader;

public class App {

  public static void main(String[] args){
    int choice;

    try {
      Connection conn = ConnectionInitializer.initializeConnection();

      if(conn == null)
        return;

      // Exit condition
      boolean exit_true = false;
      int timeout = 10;

      // Main cycle
      while (!exit_true && conn.isValid(timeout)) {
        choice = get_choice();
        try {
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
              break;

            default:
              break;
          }
        } catch (Exception e) {
          System.out.println(
            "The transaction failed because the database " + 
            "generated the following exception: " + e.getMessage()
          );

          e.printStackTrace();

          if (choice == 1)
            exit_true = true;
        }
      }
      conn.close();

    } catch (Exception e) { 
      e.printStackTrace(); 
    }

  }

  private static void print_interface() {
    System.out.println();
    System.out.println("[1]Login");
    System.out.println("[2]Register");
    System.out.println("[3]exit");
  }

  private static int get_choice() {
    print_interface();
    return InputReader.read_choice(1, 3);
  }

}
