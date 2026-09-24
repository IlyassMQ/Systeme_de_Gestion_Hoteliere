package exception;

public class UserNoteFoundException extends Exception {
    public UserNoteFoundException(String message) {
        super(message);
    }
    public UserNoteFoundException(){
        super("User Note founde");
    }
}
