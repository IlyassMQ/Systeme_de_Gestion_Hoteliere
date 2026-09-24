package exception;

public class EmailAlreadyExistsException extends Exception {


    public EmailAlreadyExistsException(String message){
        super(message);
    }
    public EmailAlreadyExistsException(){
        super("Email ALready Exists");
    }
}
