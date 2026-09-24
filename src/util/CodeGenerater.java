package util;

import java.time.Year;
import java.util.Random;

public class CodeGenerater {
    private static int c = 1 ;
    public static String randomCode(){
        Random random = new Random();
        int Number = random.nextInt(100);
        return String.valueOf(Number);
    }

    public static String resevationCode(){
        return "RES-" + Year.now().getValue() +randomCode()+ c++;
    }

    public static String roomNumber(){
       return "Room" + randomCode() + c ;
    }
}
