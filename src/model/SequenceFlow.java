package model;

import java.util.List;

import Exceptions.FlowUnStartedException;
import Exceptions.PendingStageSuccesException;
import constants.NodeConstants;
import utils.ThreadUtils;

public class SequenceFlow extends Flow {

	private int iterator;
	private List<Stage> stages;
	
	
	public SequenceFlow(SequenceFlow caller, Service service, List<Stage> stages) {
		super(caller, service);
		this.stages = stages;
	
		this.iterator = -1; 
	}
	
	
	public void registerResponse(Service service, boolean success) throws FlowUnStartedException {
		if (iterator == -1) {
			throw new FlowUnStartedException();
		} else if (iterator == stages.size()) {
			return;
		}
		Stage currentStage = stages.get(iterator);
		currentStage.updateServiceResponse(service, success);
		if (!success) {
			boolean canRetry = currentStage.canRetry(service);
			if (!canRetry) {
				this.getCaller().registerResponse(this.getService(), false);
			} else {
				invokeNode(service, NodeConstants.SLEEP_INTERVAL_MILLIS);
			}
		}
	}
	
	
	private void invokeNode(Service dependant, long sleepTime) throws FlowUnStartedException {
		if (sleepTime != 0) {
			ThreadUtils.goIdle(sleepTime);
		}
		boolean canRequest = dependant.addRequest(this);
		if (!canRequest) {
			registerResponse(dependant, false);
		}
	}
	
	private ResponseState getCurrentStageState() {
		if (iterator == -1 || iterator == stages.size()) {
			return ResponseState.SUCCEEDED;
		} else {
			return stages.get(iterator).isComplete();
		}
	}
	
	private Stage nextStage() throws PendingStageSuccesException {
		ResponseState currentState = getCurrentStageState();
		if (currentState == ResponseState.PENDING) {
			throw new PendingStageSuccesException();
		} else if (currentState == ResponseState.SUCCEEDED ){
			iterator++;
			if (iterator >= stages.size()) {
				return null;
			} else {
				return stages.get(iterator);
			}
		}
		return null;
	}
	
	private boolean isRunning() {
		if (iterator == -1) {
			return true;
		} else if (iterator == stages.size()) {
			return false;
		} else {
			return stages.get(iterator).isComplete() != ResponseState.FAILED;
		}
		
	}
	
	private boolean isGracefullTermination() {
		if (iterator >= stages.size()) {
			return stages.get(stages.size() - 1).isComplete() == ResponseState.SUCCEEDED;
		} else {
			return false;
		}
	}
	
	@Override
	public void run() {
		while(isRunning()) {
			try {
				Stage nextStage = nextStage();
				if (nextStage != null) {
					for(Service service : nextStage.getServices()) {
						try {
							invokeNode(service, 0);
						} catch (FlowUnStartedException e) {
							e.printStackTrace();
						}
					}
				} else {
					break;
				}
			} catch(PendingStageSuccesException e) {
				ThreadUtils.goIdle(NodeConstants.SLEEP_INTERVAL_MILLIS);
			}
		}
		try {
			if (isGracefullTermination()) {
				this.getCaller().registerResponse(this.getService(), true);
			} else {
				this.getCaller().registerResponse(this.getService(), false);
			}
		} catch(FlowUnStartedException e) {
			e.printStackTrace();
		}
		
	}

	
	

}
