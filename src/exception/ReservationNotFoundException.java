package exception;

public class ReservationNotFoundException extends Exception{

    public ReservationNotFoundException(String message) {
        super(message);
    }

    public ReservationNotFoundException() {
        super("Reservation Not Found");
    }
}
