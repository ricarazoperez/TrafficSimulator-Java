package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import Exceptions.InvalidArgumentException;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;


public class SetContClassEventBuilder extends Builder<Event>{

	//Constructor
	
	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	//Metodos 
	
	@Override
	protected Event createTheInstance(JSONObject data) throws InvalidArgumentException {
				
		int time;
		List<Pair<String, Integer>> lista = new ArrayList<Pair<String, Integer>>();
		JSONArray ja;
		String vehicle;
		int contClass;
		Pair<String,Integer> par;
		
		if(data == null) return null;
		if(!data.has("time") || !data.has("info")) return null;
		time = data.getInt("time");
		ja = data.getJSONArray("info");
		for(int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			vehicle    = jo.getString("vehicle");
			contClass  = jo.getInt("class");
			par =  new Pair<String, Integer>(vehicle, contClass);
			lista.add(par);

		}
		
		return new SetContClassEvent(time, lista);
	}
	
}
