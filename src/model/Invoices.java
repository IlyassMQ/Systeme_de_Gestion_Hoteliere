package model;

import java.util.UUID;

public class Invoices {
    private UUID id;
    private String invoice_number;
    private Payment payments;

    public Invoices(UUID id, String invoice_number, Payment payments) {
        this.id = id;
        this.invoice_number = invoice_number;
        this.payments = payments;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public Payment getPayments() {
        return payments;
    }

    public void setPayments(Payment payments) {
        this.payments = payments;
    }
}
