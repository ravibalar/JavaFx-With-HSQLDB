package utility;

// Invalid access of user
public class InvalidAccessException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidAccessException(String cause) {
        super(cause);
    }
}