package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;


public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy> {

	
	//Constructor
	public RoundRobinStrategyBuilder() {
		super("round_robin_lss");
	}

	
	//Metodos
	
	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		
		int timeSlot = 1;
		
		if(data == null) return null;
		if(data.has("timeslot")) timeSlot = data.getInt("timeslot");
		return new RoundRobinStrategy(timeSlot);
	}

}
