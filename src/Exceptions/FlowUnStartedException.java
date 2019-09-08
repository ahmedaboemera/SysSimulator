package Exceptions;

public class FlowUnStartedException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5225195155046740308L;

	public FlowUnStartedException() {
		super("The current flow was not started to receive any responses.");
	}

}
