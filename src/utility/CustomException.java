package utility;

// Custom exceeded exception
public class CustomException extends Exception {
    private static final long serialVersionUID = 1L;
    private final String cause;

    public CustomException(String cause) {
        super(cause);
        this.cause = cause;
    }

    public String toString() {
        return this.cause;
    }
}