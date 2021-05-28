package dbproject;

import java.sql.Connection;
import java.sql.SQLException;

import dbproject.classes.FollowUser;
import dbproject.classes.AddPhoneNumber;
import dbproject.classes.ChangeProfileVisibility;
import dbproject.classes.TweetMenu;
import dbproject.classes.ShowTimeSpent;
import dbproject.classes.chat.ChatMenu;
import dbproject.classes.utility.InputReader;

public class UserInterface {
    SessionManager sessionManager;
    Connection connection;
    Integer user_id;

    public UserInterface(Connection connection, Integer user_id) throws SQLException{
        this.sessionManager = new SessionManager(connection, user_id);
        sessionManager.start_session();
        this.connection = connection;
        this.user_id = user_id;
    }

    public void start_menu(){
        int choice = 0;
        do{

            try {
                choice = get_choice();
                switch (choice) {
                    case 1: TweetMenu menu = new TweetMenu(connection, user_id);
                            menu.askToMakeTweet();
                        break;

                    case 2: FollowUser followUser = new FollowUser(connection, user_id);
                            followUser.followUserMenu();
                        break;

                    case 3: ChatMenu chatMenu = new ChatMenu(connection, user_id);
                            chatMenu.startMenu();
                        break;
                        
                    case 4: ShowTimeSpent timeSpent = new ShowTimeSpent(connection, user_id);
                            timeSpent.printTimeSpent();
                        break; 

                    case 5: ChangeProfileVisibility profile = new ChangeProfileVisibility(connection, user_id);
                            profile.changeVisibility();
                    break;

                    case 6: AddPhoneNumber phoneNumber = new AddPhoneNumber(connection, user_id);
                            phoneNumber.askForPhoneNumber();
                    break;

                    case 7: sessionManager.close_session();
                    break;
                    
                    default:
                        break;
                }
                
            }catch(Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }

        }while(choice != 7);
    }

    private void print_interface() {
        System.out.println();
        System.out.println("[1]Make a post");
        System.out.println("[2]Follow a user");
        System.out.println("[3]Send a message");
        System.out.println("[4]Show time spent on Twitaroo");
        System.out.println("[5]Change profile visibility");
        System.out.println("[6]Add phone number");
        System.out.println("[7]Logout");
    }

    private int get_choice() {
        print_interface();
        return InputReader.read_choice(1, 7);
    }

}
