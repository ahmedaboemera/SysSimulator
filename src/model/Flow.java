package model;

public abstract class Flow implements Runnable {
	
	protected SequenceFlow caller;
	protected Service service;
	
	public Service getService() {
		return service;
	}

	public SequenceFlow getCaller() {
		return caller;
	}

	public Flow(SequenceFlow caller, Service service) {
		this.caller = caller;
	}

}
