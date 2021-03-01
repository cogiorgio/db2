package exceptions;


public class BlacklistException extends Exception {

	private static final long serialVersionUID = 1L;

	public BlacklistException(String message) {
		super(message);
	}
}