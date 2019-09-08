package model;

import java.util.List;
import java.util.Map;

public class FlowDescriptor {

	private int maxSleepTime;
	
	public int getMaxSleepTime() {
		return maxSleepTime;
	}

	public FlowDescriptor(int maxSleepTime) {
		this.maxSleepTime = maxSleepTime;
	}
	
	public boolean isComplex() {
		return false;
	}
	
	public List<List<Service>> getFlowDiagram() {
		return null;
	}

	public Map<Service, Integer> getRetryCounts() {
		return null;
	}
}
