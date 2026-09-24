package util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CalculUtil {


    public static BigDecimal PriceCalcul(LocalDate checkIn, LocalDate checkOut, BigDecimal pricePerNight){
        long days = ChronoUnit.DAYS.between(checkIn,checkOut);
        return BigDecimal.valueOf(days).multiply(pricePerNight);

    }



}
