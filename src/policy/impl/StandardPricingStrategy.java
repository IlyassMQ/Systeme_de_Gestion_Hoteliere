package policy.impl;

import model.Reservation;
import policy.PricingStrategy;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class StandardPricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculatePrice(BigDecimal roomPrice, LocalDate checkIn, LocalDate checkOut, LocalDateTime created_at) {
        BigDecimal price = roomPrice;
        if (checkIn.getMonthValue() == 7 ||checkIn.getMonthValue() == 8 ){
            price = price.multiply(new BigDecimal("1.3"));
        }
        if (checkIn.getMonthValue() == 11 ||checkIn.getMonthValue() == 12 ||checkIn.getMonthValue() == 1 ||checkIn.getMonthValue() == 2 ){
            price = price.multiply(new BigDecimal("0.85"));
        }

        if (checkIn.getDayOfWeek() == DayOfWeek.FRIDAY  || checkIn.getDayOfWeek() == DayOfWeek.SATURDAY){
            price = price.multiply(new BigDecimal("1.15"));
        }
        if (ChronoUnit.DAYS.between(checkIn,checkOut) >= 7){
            price = price.multiply(new BigDecimal("0.90"));
        }
        if (ChronoUnit.DAYS.between(checkIn,checkOut) >= 14){
            price = price.multiply(new BigDecimal("0.85"));
        }
        if (ChronoUnit.DAYS.between(created_at.toLocalDate(), checkIn) >= 30){
            price = price.multiply(new BigDecimal("0.95"));
        }
        if (ChronoUnit.DAYS.between(created_at.toLocalDate(), checkIn) <= 3){
            price = price.multiply(new BigDecimal("1.10"));
        }
        return price;
    }
}
