package dbproject.classes;

import java.sql.Connection;

import dbproject.classes.utility.InputReader;

public class TweetMenu {
    Connection connection;
    Integer user_id;

    public TweetMenu(Connection connection, Integer user_id){
        this.connection = connection;
        this.user_id = user_id;
    }

    public void askToMakeTweet(){
        String answer ="";
        do{
            System.out.println("\nAnswer with yes to make a tweet or no to go back.");
            System.out.println("Do you want to make a tweet?");
            System.out.printf("Answer: ");
            answer = InputReader.readString();

            if(answer.equals("yes")){
                Tweet tweet = new Tweet(connection, user_id);
                tweet.createTweet();
            }
                 
        }while(!answer.equals("stop"));
    }
    
}
