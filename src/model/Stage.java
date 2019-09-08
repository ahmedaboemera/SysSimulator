package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stage {
	
	private List<Service> services;
	
	public List<Service> getServices() {
		return services;
	}

	private Map<Service, ResponseState> responses;
	private Map<Service, Integer> retryCounts;

	public Stage(List<Service> services, Map<Service, Integer> retries) {
		this.services 	 = services;
		this.retryCounts = retries;
		
		responses = new HashMap<Service, ResponseState>();
		initializeResponses();
	}
	
	private void initializeResponses() {
		for(Service service : services) {
			responses.put(service, ResponseState.PENDING);
		}
	}
	
	public ResponseState isComplete() {
		boolean hasFailures = false;
		boolean hasPending  = false;
		for(Service service : services) {
			if (!responses.containsKey(service)) {
				return ResponseState.PENDING; 
			} else {
				ResponseState state = responses.get(service); 
				if (state == ResponseState.PENDING){
					hasPending = true;
				} else if (state == ResponseState.FAILED) {
					hasFailures = true;
				}
			}
		}
		if (hasFailures) {
			return ResponseState.FAILED;
		} else if (hasPending) {
			return ResponseState.PENDING;
		}
		return ResponseState.SUCCEEDED;
	}
	
	public boolean canRetry(Service service) {
		return responses.get(service) == ResponseState.PENDING;
	}
	
	public void updateServiceResponse(Service service, boolean success) {
		if (success) {
			responses.put(service, ResponseState.SUCCEEDED);
		} else {
			if (retryCounts.get(service) > 0) {
				retryCounts.put(service, retryCounts.get(service) - 1);
				responses.put(service, ResponseState.PENDING);
			} else {
				responses.put(service, ResponseState.FAILED);
			}
		}
	}
}
