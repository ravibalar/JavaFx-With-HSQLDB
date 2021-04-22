package utility;

// Capacity exceeded exception
public class CapacityException extends Exception {

	private static final long serialVersionUID = 1L;

	public CapacityException(String cause) {
		super(cause);
	}
}