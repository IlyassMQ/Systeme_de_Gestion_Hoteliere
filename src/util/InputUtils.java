package util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class InputUtils {

    public static String lireString(Scanner scanner,String label){
        System.out.println(label);
        String str = scanner.nextLine();
        return str;
    }

    public static Integer lireInt(Scanner scanner,String label){
        System.out.println(label);
        String nmbrIn = scanner.nextLine();
        int nmbr = Integer.parseInt(nmbrIn);
        return nmbr;
    }
    public static double lireDouble(Scanner scanner,String label){
        System.out.println(label);
        String nmbrD = scanner.nextLine();
        double d = Double.parseDouble(nmbrD);
        return d;
    }

    public static LocalDate lireDate(Scanner scanner,String label){
        System.out.println(label);
        String dateStr = scanner.nextLine();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(dateStr,dateFormatter);
        return date;
    }

    public static Long lireLong(Scanner scanner,String label){
        System.out.println(label);
        String longStr = scanner.nextLine();
        Long longg = Long.parseLong(longStr);
        return longg;
    }

    public static BigDecimal lireBigDecimal(Scanner scanner,String label){
        System.out.println(label);
        String decimalStr = scanner.nextLine();
        BigDecimal bigDecimal = new BigDecimal(decimalStr);
        return bigDecimal;
    }
}
