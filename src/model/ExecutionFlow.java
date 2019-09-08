package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import Exceptions.FailedExecutionException;
import Exceptions.InvalidFlowDescriptionException;
import Exceptions.PendingStageSuccesException;

public class ExecutionFlow {

	private int iterator;
	
	private List<List<Service>> sequenceDescription;
	private HashSet<Service> nodes;
	private HashMap<Service, Integer> retryCountMap;
	
	public HashSet<Service> getNodes() {
		return nodes;
	}

	public void setNodes(HashSet<Service> nodes) {
		this.nodes = nodes;
	}

	public ExecutionFlow(List<List<Service>> flow, List<List<Integer>> retries) throws InvalidFlowDescriptionException {
		sequenceDescription = flow;
		iterator = -1;
		initialize(flow, retries);
	}
	
	private void initialize(List<List<Service>> flow, List<List<Integer>> retries) throws InvalidFlowDescriptionException {
		nodes = new HashSet<Service>();
		for(List<Service> stage : flow) {
			for(Service node : stage) {
				if (nodes.contains(node)) {
					throw new InvalidFlowDescriptionException();
				} else {
					nodes.add(node);
				}
			}
		}
		retryCountMap = new HashMap<Service, Integer>();
		
	}
	
	public boolean canRetry(Service node) {
		retryCountMap.put(node, retryCountMap.get(node) - 1);
		return retryCountMap.get(node) >= 0;
	}

	public boolean hasNext() {
		return iterator < sequenceDescription.size() - 1;
	}
	
	public List<Service> getNextParallelSequence(HashMap<Service, ResponseState> responseStates) throws FailedExecutionException, PendingStageSuccesException {
		if (iterator >= sequenceDescription.size()) {
			return null;
		}
		if (iterator == -1) {
			iterator++;
			return sequenceDescription.get(iterator);
		}
		boolean hasFailures = checkAnyFailedSubTasks(responseStates);
		if (hasFailures) {
			throw new FailedExecutionException();
		}
		boolean isStageComplete = isStageSuccess(responseStates, iterator);
		if (isStageComplete) {
			iterator++;
			return sequenceDescription.get(iterator);
		} else {
			throw new PendingStageSuccesException();
		}
	}
	
	private boolean isStageSuccess(HashMap<Service, ResponseState> responseStates, int iterator) {
		for (Service node : sequenceDescription.get(iterator)) {
			if (responseStates.get(node) != ResponseState.SUCCEEDED) {
				return false;
			}
		}
		return true;
	}
	
	private boolean checkAnyFailedSubTasks(HashMap<Service, ResponseState> responseStates) {
		for (Service node : responseStates.keySet()) {
			if (responseStates.get(node) == ResponseState.FAILED) {
				return false;
			}
		}
		return true;
	}
	
}
