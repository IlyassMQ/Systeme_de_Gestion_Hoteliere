package policy;

import model.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface PricingStrategy {
    public BigDecimal calculatePrice(BigDecimal roomPrice, LocalDate checkIn, LocalDate checkOut, LocalDateTime created_at);
}
