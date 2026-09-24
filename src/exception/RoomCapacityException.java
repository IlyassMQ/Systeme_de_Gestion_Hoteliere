package exception;

public class RoomCapacityException extends RuntimeException {
    public RoomCapacityException(String message) {
        super(message);
    }
    public RoomCapacityException(){
        super("The Guest number is more then the room Capacity");
    }
}
