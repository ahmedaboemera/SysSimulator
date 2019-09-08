package Exceptions;

public class InvalidFlowDescriptionException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7540915995630352143L;

	public InvalidFlowDescriptionException() {
		super("Invalid Sequence flow description, Note a node can only occur once in the whole description formula, retries should be more than 0.");
	}

}
