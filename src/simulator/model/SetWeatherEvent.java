package simulator.model;

import java.util.List;

import Exceptions.ExecutionException;
import Exceptions.InvalidArgumentException;
import simulator.misc.Pair;

public class SetWeatherEvent extends Event{
	
	//Atributos
	//ws contiene ==> idRoad | weather
	private List<Pair<String,Weather>> ws;
	
	//Constructor
	public SetWeatherEvent(int time, List<Pair<String,Weather>> ws) throws InvalidArgumentException {
		super(time);
		if(ws == null) throw new InvalidArgumentException("List ws can´t be null");
		this.ws = ws;
	}

	
	//Metodos
	
	@Override
	void execute(RoadMap map) throws InvalidArgumentException, ExecutionException {
		
		for(int i = 0; i < ws.size(); i++ ) {
			if(map.getRoad(ws.get(i).getFirst()) == null) throw new ExecutionException("Road " + 
													ws.get(i).getFirst() + " doesn´t exist in map");
			map.getRoad(ws.get(i).getFirst()).setWeather(ws.get(i).getSecond());
		}
		
	}

	public String toString() {
		String s = "Change Weather: [";
		
		for(int i = 0; i < ws.size(); i++) {
			s += "(" + ws.get(i).getFirst() + "," + ws.get(i).getSecond() + ")";
			if(i != ws.size() - 1) s += ", ";
		}
		
		s += "]";
		return s;
	}
}
