package model;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Exceptions.FlowUnStartedException;
import builder.FlowBuilder;
import constants.NodeConstants;

public class Service implements Runnable {

	protected final Integer id;
	protected final Integer maxQueueSize;
	protected final ExecutorService replicas;
	protected final FlowDescriptor flowDescriptor;
	protected final Integer successRate;
	
	protected Queue<SequenceFlow> pendingRequests;
	
	protected Random random;
	
	public Service(Integer id, Double successRate, Integer maxQueueSize, Integer replicaCount, FlowDescriptor flow) {
		this.id           	 = id;
		this.maxQueueSize 	 = maxQueueSize;
		this.successRate  	 = (int)(successRate * 1000)/1000;
		this.replicas     	 = Executors.newFixedThreadPool(replicaCount);
		this.flowDescriptor  = flow;
		this.pendingRequests = new LinkedList<SequenceFlow>();
		
		this.random = new Random();
	}
	
	
	public synchronized boolean addRequest(SequenceFlow caller) {
		if (maxQueueSize  < pendingRequests.size()) {
			pendingRequests.add(caller);
			return true;
		}
		return false;
	}
	
	public Integer getRequestLoad() {
		return pendingRequests.size();
	}
	
	protected boolean successfulRequest() {
		int rand = random.nextInt(100000);
		if (rand > successRate) return false;
		return true;
	}
	
	@Override
	public void run() {
		while(true) {
			if (pendingRequests.size() > 0) {
				SequenceFlow caller = pendingRequests.poll();
				if (successfulRequest()) {
					Flow flow = FlowBuilder.buildFlow(caller, this, this.flowDescriptor);
					replicas.execute(flow);
				} else {
					try {
						caller.registerResponse(this, false);
					} catch (FlowUnStartedException e) {
						e.printStackTrace();
					}
				}
			} else {
				synchronized(this) {
					try {
						wait(NodeConstants.SLEEP_INTERVAL_MILLIS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
