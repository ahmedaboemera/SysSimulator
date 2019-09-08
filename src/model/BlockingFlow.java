package model;

import java.util.Random;

import Exceptions.FlowUnStartedException;
import utils.ThreadUtils;

public class BlockingFlow extends Flow {
	
	private int maxSleepTime;
	private final Random random;
	
	public BlockingFlow(SequenceFlow caller, Service service, int sleepTime) {
		super(caller, service);
		this.maxSleepTime = sleepTime;
		
		this.random = new Random();
	}
	
	private long getSleepTime() { 
		return random.nextInt(this.maxSleepTime) * 1L;
	}
	
	@Override
	public void run() {
		ThreadUtils.goIdle(getSleepTime());
		try {
			this.getCaller().registerResponse(this.getService(), true);
		} catch (FlowUnStartedException e) {
			e.printStackTrace();
		}
	}

}
