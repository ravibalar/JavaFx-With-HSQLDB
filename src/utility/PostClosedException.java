package utility;

// Posting closed
public class PostClosedException extends Exception {

    private static final long serialVersionUID = 1L;

    public PostClosedException(String cause) {
        super(cause);
    }
}