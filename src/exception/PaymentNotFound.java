package exception;

public class PaymentNotFound extends RuntimeException {
    public PaymentNotFound(String message) {
        super(message);
    }

    public PaymentNotFound() {
        super("Payment Not Found");
    }
}
