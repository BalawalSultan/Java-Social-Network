package dbproject.classes.utility;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;

public class InputReader {
    
    public static String readString(){
        String string = null;

        try{
            InputStreamReader input = new InputStreamReader(System.in);
            BufferedReader keyboard = new BufferedReader(input);
            string = keyboard.readLine();
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return string;
    }

    public static int readInt(){
        int n = 0;
        String s = readString();
    
        try{
            n = Integer.parseInt(s);
        }catch(NumberFormatException e){ 
            n = -1;
        }

        return n;
    }

    public static String readPassword(){
        Console console = System.console();
        return String.valueOf(console.readPassword());
    }

    public static int read_choice(int min, int max) {
        int choice = 0;
    
        do{
          System.out.printf("Select an option: ");
          choice = readInt();
        }while (choice < min || choice > max);
    
        return choice;
    }

}
