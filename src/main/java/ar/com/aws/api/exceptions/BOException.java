package ar.com.aws.api.exceptions;

public class BOException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3627616243256892185L;

    public BOException() {
        super();
    }
	
    public BOException(String message) {
        super(message);
    }

    public BOException(String message, Throwable cause) {
        super(message, cause);
    }

    public BOException(Throwable cause) {
        super(cause);
    }
    
}
