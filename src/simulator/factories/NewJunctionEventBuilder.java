package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import Exceptions.InvalidArgumentException;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {

	//Atributos
	private Factory<LightSwitchingStrategy> lssFactory;
	private Factory<DequeuingStrategy> dqsFactory;
	
	//Constructor
	
	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super("new_junction");
		this.lssFactory = lssFactory;
		this.dqsFactory = dqsFactory;
		
	}

	//Metodos 
	
	@Override
	protected Event createTheInstance(JSONObject data) throws InvalidArgumentException {
		int time, xCoor, yCoor;
		String id;
		JSONArray CoorList;
		JSONObject joStrategy;
		DequeuingStrategy dqStrategy;
		LightSwitchingStrategy lsStrategy;
		
		if(data == null) return null;
		if(!data.has("time") || !data.has("id") ||
		   !data.has("coor") || !data.has("ls_strategy") || !data.has("dq_strategy")) return null;
		
		time = data.getInt("time");
		id = data.getString("id");
		CoorList = data.getJSONArray("coor");
		xCoor = CoorList.getInt(0);
		yCoor = CoorList.getInt(1);
		joStrategy = data.getJSONObject("ls_strategy");
		lsStrategy = this.lssFactory.createInstance(joStrategy);
		joStrategy = data.getJSONObject("dq_strategy");
		dqStrategy = this.dqsFactory.createInstance(joStrategy);
		
		return new NewJunctionEvent(time, id, lsStrategy, dqStrategy,xCoor, yCoor);
	}

}
