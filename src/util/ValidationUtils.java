package util;

import java.security.PublicKey;

public class ValidationUtils {

    public static boolean emailVerfication(String email){
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        if (!email.matches(regex)){
            throw new IllegalArgumentException("Enter a valid email");
        }
        return true;
    }

    public static boolean passwordVerfication(String password){
        String regex = "^[A-Za-z0-9]{6,}$";
        if (!password.matches(regex)){
            throw new IllegalArgumentException("Enter a valid Password");
        }
        return true;
    }
    public static boolean phoneVerfication(String phone){
        String regex = "^[0-9]{10}$";
        if (!phone.matches(regex)){
            throw new IllegalArgumentException("Enter a valid phone number");
        }
        return true;
    }


}
