package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event>{
	
	//Constructor
	
	public NewVehicleEventBuilder() {
		super("new_vehicle");
	}

	//Metodos 
	
	@Override
	protected Event createTheInstance(JSONObject data) {
		int time, maxSpeed, contClass;
		String id;
		JSONArray it;
		List<String> itin = new ArrayList<String>();
		
		if(data == null) return null;
		if(!data.has("time") || !data.has("id") || !data.has("maxspeed") 
			|| !data.has("class") || !data.has("itinerary")) return null;
		
		time      = data.getInt("time");
		id        = data.getString("id");
		maxSpeed  = data.getInt("maxspeed");
		contClass = data.getInt("class");
		it = data.getJSONArray("itinerary");
		for(int i = 0; i < it.length(); i++) {
			itin.add(it.getString(i));
		}
		return new NewVehicleEvent(time, id, maxSpeed, contClass, itin);
	}
	
}
