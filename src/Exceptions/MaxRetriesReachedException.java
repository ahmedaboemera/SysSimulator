package Exceptions;

public class MaxRetriesReachedException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8655211245655709422L;

	public MaxRetriesReachedException() {
		super("Max No. of retries reached 5** status code back.");
	}

}
