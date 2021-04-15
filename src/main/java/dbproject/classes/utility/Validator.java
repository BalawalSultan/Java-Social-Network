package dbproject.classes.utility;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;

public class Validator {
    
    private static final int BCryptWorkload = 15;

    public static boolean isMail(String mail){
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mail);

        return matcher.matches();
    }

    //The regex for the password was taken from
    //https://mkyong.com/regular-expressions/how-to-validate-password-with-regular-expression/
    public static boolean isValidPassword(String password){
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{7,12}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public static String hashPassword(String password){
        String salt = BCrypt.gensalt(BCryptWorkload);
        String hash = BCrypt.hashpw(password, salt);

        return hash;
    }

    public static boolean compareWithHash(String password, String hash){
        return BCrypt.checkpw(password, hash);
    }

    public static boolean isValidTimeLength(String time){
        final String regex = "^(00:(?:(?=[1])[1][0-1]|[0][0-9]):[0-5][0-9])";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(time);

        return matcher.matches();
    }

    public static boolean isValidPrefix(String phoneNumber){
        String regex = "^\\+(?:[0-9]?){1,4}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber){
        String regex = "^[0-9]{7,15}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.matches();
    }
    
    public static Boolean isValidPhoto(String fileName){
        ArrayList<String> validExtensions = new ArrayList<>();
        String extension = getFileType(fileName);
        validExtensions.add("png");
        validExtensions.add("jpeg");
        validExtensions.add("jpg");
        validExtensions.add("gif");

        return validExtensions.contains(extension.toLowerCase());
    }

    public static Boolean isValidVideo(String fileName){
        return getFileType(fileName).toLowerCase().equals("mp4");
    }
    
    public static String getFileType(String file){
        String[] A = file.split("\\.");
        return  A[A.length - 1];
    }
}
