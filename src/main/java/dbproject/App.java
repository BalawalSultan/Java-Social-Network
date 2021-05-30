package dbproject;

import java.sql.Connection;

public class App {

  public static void main(String[] args){

    try {
      Connection conn = ConnectionInitializer.initializeConnection();

      MainMenu menu = new MainMenu(conn);
      menu.openMenu();

    } catch (Exception e) { 
      System.out.println("An error occurred while connection to the database");
      e.printStackTrace();
    }

  }

}
