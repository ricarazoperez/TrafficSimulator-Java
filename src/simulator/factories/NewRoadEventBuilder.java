package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.Weather;

public abstract class NewRoadEventBuilder extends Builder<Event> {

	//Atributos
	protected int time;
	protected int length;
	protected int co2limit;
	protected int maxSpeed;
	protected String id;
	protected String src;
	protected String dest;
	protected Weather weather;
	
	
	//Constructor
	
	NewRoadEventBuilder(String type) {
		super(type);
		
	}

	
	//Metodos 
	
	@Override
	protected Event createTheInstance(JSONObject data) {
		String w;
		
		if (data == null) return null;
		if(!data.has("time") || !data.has("id") || !data.has("src") || !data.has("dest") || 
		   !data.has("length") || !data.has("co2limit") || !data.has("maxspeed") || !data.has("weather"))return null;

		time     = data.getInt("time");
		id       = data.getString("id");
		src      = data.getString("src");
		dest     = data.getString("dest");
		length   = data.getInt("length");
		co2limit = data.getInt("co2limit");
		maxSpeed = data.getInt("maxspeed");
		w        = data.getString("weather");
		weather  = Weather.valueOf(w);
		return createTheRoad();
	}
	
	abstract Event createTheRoad();

}