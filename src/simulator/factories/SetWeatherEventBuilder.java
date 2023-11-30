package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import Exceptions.InvalidArgumentException;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event>{

	//Constructor
	
	public SetWeatherEventBuilder() {
		super("set_weather");
	}

	//Metodos 
	
	@Override
	protected Event createTheInstance(JSONObject data) throws InvalidArgumentException {
		int time;
		List<Pair<String, Weather>> lista = new ArrayList<Pair<String, Weather>>();
		JSONArray ja;
		String road;
		Weather weather;
		Pair<String,Weather> par;
		
		if(data == null) return null;
		if(!data.has("time") || !data.has("info")) return null;
		time = data.getInt("time");
		ja = data.getJSONArray("info");
		for(int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			road = jo.getString("road");
			weather = Weather.valueOf(jo.getString("weather"));
			par =  new Pair<String, Weather>(road, weather);
			lista.add(par);

		}
		
		return new SetWeatherEvent(time, lista);
		
	}
	
}
