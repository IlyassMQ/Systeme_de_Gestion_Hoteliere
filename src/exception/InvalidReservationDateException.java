package exception;

public class InvalidReservationDateException extends Exception{

    public InvalidReservationDateException(String message) {
        super(message);
    }

    public InvalidReservationDateException() {
        super("Reservation Date Invalid");
    }
}

