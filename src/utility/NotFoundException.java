package utility;

// No post/user found
public class NotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public NotFoundException(String cause) {
        super(cause);
    }
}