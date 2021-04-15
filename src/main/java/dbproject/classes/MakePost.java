package dbproject.classes;

import java.sql.Connection;

import dbproject.classes.utility.InputReader;

public class MakePost {
    enum Filetype {png, jpeg, jpg, gif, mp4}
    Connection connection;
    Integer user_id;

    public MakePost(Connection connection, Integer user_id){
        this.connection = connection;
        this.user_id = user_id;
    }

    public void selectPostType(){
        String answer ="";
        do{
            System.out.println("\nAnswer with yes or no, or write stop to go back.");
            System.out.println("Do you want to attach a multimedia to your Post?");
            System.out.printf("Answer with yes or no: ");
            answer = InputReader.readString();

            if(answer.equals("yes"))
                createPostWithMultimedia();
            else if(answer.equals("no"))
                createPost();
                 
        }while(!answer.equals("stop"));
    }

    private void createPostWithMultimedia(){
        PostMultimedia post = new PostMultimedia(connection, user_id);
        post.createPostWithMultimedia();
    }

    private void createPost(){
        Post post = new Post(connection, user_id);
        post.createPost();
    }
    
}
