package builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.BlockingFlow;
import model.Flow;
import model.FlowDescriptor;
import model.SequenceFlow;
import model.Service;
import model.Stage;

public class FlowBuilder {

	
	public static Flow buildFlow(SequenceFlow caller, Service currentService, FlowDescriptor descriptor) {
		if (descriptor.isComplex()) {
			List<List<Service>> descriptorStages = descriptor.getFlowDiagram();
			Map<Service, Integer> retryCountMap = descriptor.getRetryCounts();
			List<Stage> stages = new ArrayList<Stage>();
			for(List<Service> stage : descriptorStages) {
				Map<Service, Integer> stageRetryMap = new HashMap<Service, Integer>();
				for(Service service : stage) {
					stageRetryMap.put(service, retryCountMap.get(service));
				}
				stages.add(new Stage(stage, stageRetryMap));
			}
			return new SequenceFlow(caller, currentService, stages);
		} else {
			return new BlockingFlow(caller, currentService, descriptor.getMaxSleepTime());
		}
	}
}
