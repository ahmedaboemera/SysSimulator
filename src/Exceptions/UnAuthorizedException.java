package Exceptions;

@SuppressWarnings("serial")
public class UnAuthorizedException extends Exception {

	public UnAuthorizedException() {
		super("UnAuthorized to perform this action");
	}
}
