package dbproject.classes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import dbproject.classes.MakePost.Filetype;
import dbproject.classes.utility.InputReader;
import dbproject.classes.utility.Validator;

public class PostMultimedia {
    Connection connection;
    int user_id;

    public PostMultimedia(Connection connection, int user_id){
        this.connection = connection;
        this.user_id = user_id;
    }

    public void createPostWithMultimedia(){
        String[] file = uploadFile().split("/");
        java.util.Date date = new java.util.Date();	
        java.sql.Time sqlTime=new java.sql.Time(date.getTime());
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        String url = "https://twitaroo.com/" + user_id + "/" + file[0] ; // I don't have a server so I just made up an url
        String type = Filetype.valueOf(Validator.getFileType(file[0])).toString();

        String query = "INSERT INTO PostMultimedia VALUES (default,?,?,?,?,?,?,?::FILETYPE,?,?)";
     
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            
            pstmt.setInt(1, user_id);
            pstmt.setDate(2, sqlDate);
            pstmt.setTime(3, sqlTime);
            pstmt.setString(4, readPostText());
            pstmt.setString(5, file[0]);
            pstmt.setInt(6, getFileSize());
            pstmt.setObject(7, type);
            pstmt.setString(8, url);
            if(file[1].equals("null"))
                pstmt.setTime(9,null);
            else
                pstmt.setTime(9, getSqlTime(file[1]));
            
            pstmt.execute();
            pstmt.close();

        } catch (Exception e) { 
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private String readPostText(){
        System.out.printf("Insert the text of your post: ");
        return InputReader.readString();
    }

    private String getFileNameFromPath(String path){
        String[] A = path.split("/");
        return  A[A.length - 1];
    }

    private java.sql.Time getSqlTime(String string){
        LocalTime time = LocalTime.parse(string, DateTimeFormatter.ISO_TIME);
        return Time.valueOf(time);
    }

    private String uploadFile(){
        while(true){
            System.out.printf("Insert the file path: ");
            String pathToFile= InputReader.readString();
            String file = getFileNameFromPath(pathToFile);

            if(Validator.isValidPhoto(file))
                return file + "/null";
            
            if(Validator.isValidVideo(file)){
                String length = readVideoLength();
                return file += "/"+length;   
            }
        }
    }

    private String readVideoLength(){
        String length = "";

        do{
            System.out.println("\nThe video length must be written in the following format ");
            System.out.println("HH:MM:SS and it must be under 12 minutes.");
            System.out.printf("Insert the length of the video: ");
            length = InputReader.readString();
        }while(!Validator.isValidTimeLength(length));

        return length;
    }

    private int getFileSize() {
        int size = 0;
    
        while (size < 1 || size > 20) {
            System.out.println("\nThe maximum file size is = 20");
            System.out.printf("Insert file size: ");
            size = InputReader.readInt();
        }
    
        return size;
    }

}
