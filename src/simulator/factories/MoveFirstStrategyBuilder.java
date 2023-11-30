package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.MoveFirstStrategy;

public class MoveFirstStrategyBuilder extends Builder<DequeuingStrategy> {

	//Constructor
	
	public MoveFirstStrategyBuilder() {
		super("move_first_dqs");
	
	}

	//Metodos 
	
	@Override
	protected DequeuingStrategy createTheInstance(JSONObject data) {
		
		return new MoveFirstStrategy();
	}

}
