package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Payment {
    private UUID id;
    private Reservation reservation_code;
    private BigDecimal amount_ht;
    private PaymentStatus status;
    private BigDecimal tva;
    private BigDecimal amount_ttc;
    private PaymentMethode paymentMethode;
    private LocalDate payment_date;

    public Payment(UUID id, Reservation reservation_code, BigDecimal amount_ht, PaymentStatus status, BigDecimal tva, BigDecimal amount_ttc, PaymentMethode paymentMethode, LocalDate payment_date) {
        this.id = id;
        this.reservation_code = reservation_code;
        this.amount_ht = amount_ht;
        this.status = status;
        this.tva = tva;
        this.amount_ttc = amount_ttc;
        this.paymentMethode = paymentMethode;
        this.payment_date = payment_date;
    }
    public UUID getId(){
        return id;
    }
    public Reservation getReservation_code() {
        return reservation_code;
    }

    public void setReservation_code(Reservation reservation_code) {
        this.reservation_code = reservation_code;
    }

    public BigDecimal getAmount_ht() {
        return amount_ht;
    }

    public void setAmount_ht(BigDecimal amount_ht) {
        this.amount_ht = amount_ht;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public BigDecimal getTva() {
        return tva;
    }

    public void setTva(BigDecimal tva) {
        this.tva = tva;
    }

    public BigDecimal getAmount_ttc() {
        return amount_ttc;
    }

    public void setAmount_ttc(BigDecimal amount_ttc) {
        this.amount_ttc = amount_ttc;
    }

    public PaymentMethode getPaymentMethode() {
        return paymentMethode;
    }

    public void setPaymentMethode(PaymentMethode paymentMethode) {
        this.paymentMethode = paymentMethode;
    }

    public LocalDate getPayment_date() {
        return payment_date;
    }

    public void setPayment_date() {
        this.payment_date = payment_date;
    }
}
