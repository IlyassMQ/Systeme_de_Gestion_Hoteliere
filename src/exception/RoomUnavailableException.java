package exception;

public class RoomUnavailableException extends Exception{

    public RoomUnavailableException(String message) {
        super(message);
    }

    public RoomUnavailableException() {
        super("Room Unvailable");
    }
}
