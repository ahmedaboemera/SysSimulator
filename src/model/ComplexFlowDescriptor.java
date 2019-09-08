package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Exceptions.InvalidFlowDescriptionException;

public class ComplexFlowDescriptor extends FlowDescriptor {

	private List<List<Service>> flowDiagram;
	private Map<Service, Integer> retryCounts;
	
	public ComplexFlowDescriptor(List<List<Service>> diagram, Map<Service, Integer> retryCounts) {
		super(0);
		this.flowDiagram = diagram;
		this.retryCounts = retryCounts;
	}
	
	public ComplexFlowDescriptor() {
		super(0);
		this.flowDiagram = new ArrayList<List<Service>>();
		this.retryCounts = new HashMap<Service, Integer>();
	}
	
	public void addStage() {
		this.flowDiagram.add(new ArrayList<Service>());
	}

	public void addService(Service service, Integer retryCount) throws InvalidFlowDescriptionException {
		if (retryCounts.containsKey(service)) {
			throw new InvalidFlowDescriptionException();
		}
		flowDiagram.get(flowDiagram.size() - 1).add(service);
		retryCounts.put(service, retryCount);
	}
	
	public boolean isComplex() {
		return true;
	}

	public List<List<Service>> getFlowDiagram() {
		return flowDiagram;
	}

	public Map<Service, Integer> getRetryCounts() {
		return retryCounts;
	}
}
