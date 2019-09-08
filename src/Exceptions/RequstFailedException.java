package Exceptions;

@SuppressWarnings("serial")
public class RequstFailedException extends Exception {
	
	public RequstFailedException() {
		super("Failed processing request");
	}

}
