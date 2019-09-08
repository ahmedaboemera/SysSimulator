package Exceptions;

public class FailedExecutionException extends Exception {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1305264855313071895L;

	public FailedExecutionException() {
		super("Execution failed.");
	}
}
