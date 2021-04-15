package dbproject;

import java.sql.Connection;
import java.sql.DriverManager;

import dbproject.classes.Registration;
import dbproject.classes.utility.InputReader;

public class App {

  public static void main(String[] args) throws Exception {
    String dbName = "twitaroo";
    String name = "postgres";
    String password = "postgres";
    int choice;

    // We assume that PostgreSQL is installed on localhost, and that it
    // contains a database named idblab05, over which we have already executed
    // the SQL code in lab05jdbcs1.sql.
    Class.forName("org.postgresql.Driver");
    String url = "jdbc:postgresql://localhost:5432" + "/" + dbName + "?user=" + name + "&password=" + password;

    // Get a connection. Since establishing a connection is expensive, we will
    // use one connection for all our statements. We need to ensure that the
    // connection is closed after the use (handle possible exceptions carefully!).
    Connection conn = DriverManager.getConnection(url);

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
