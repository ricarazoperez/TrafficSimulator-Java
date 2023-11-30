package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import Exceptions.ExecutionException;
import Exceptions.InvalidArgumentException;
import simulator.factories.Factory;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.Observable;
import simulator.model.SetContClassEvent;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;
import simulator.model.Weather;

public class Controller implements Observable<TrafficSimObserver>{
	
	//atributos 

	private TrafficSimulator sim;
	private Factory<Event> eventsFactory;
	
	//Constructor 
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) throws InvalidArgumentException {
		if(sim == null || eventsFactory == null) throw new InvalidArgumentException("Paramethers for controller can´t be null");
		this.sim = sim;
		this.eventsFactory = eventsFactory;
	}
	
	
	//Metodos 
	
	public void loadEvents(InputStream in) throws InvalidArgumentException {
		
		
		JSONObject jo = new JSONObject(new JSONTokener(in));
		if(!jo.has("events")) throw new InvalidArgumentException("Input is wrong");
		JSONArray  ja = new JSONArray(); 
		ja = jo.getJSONArray("events");
		
		for(int i = 0; i < ja.length(); i++) {
			JSONObject joEvent = ja.getJSONObject(i);
			Event e = eventsFactory.createInstance(joEvent);
			sim.addEvent(e);
		}
		
	}
	
	public void run(int n, OutputStream out) throws InvalidArgumentException, ExecutionException {
		
	
		if(out != null) {
			PrintStream p = new PrintStream(out);
			p.println("{");
			p.println(" \"states\": [");

		
			for(int i = 0; i < n; i++) {
				sim.advance();
				if(i == n - 1) p.println(sim.report());
				else p.println(sim.report() + ",");
		
			}
		
			p.println("]");
			p.println("}");
			p.close();
		}else 	sim.advance();
	
	}
	
	public void reset() {
		sim.reset();
	}
	
	public void nuevaCont(String v, int c, int t) throws InvalidArgumentException {
		Pair<String, Integer> elem = new Pair<String, Integer>(v, c);
		List<Pair<String, Integer>> l = new ArrayList<Pair<String, Integer>>();
		l.add(elem);
		addEvent(new SetContClassEvent(t + sim.getTime(), l));
	}
	
	public void nuevoWeather(String r, Weather w, int t) throws InvalidArgumentException {
	
		Pair<String, Weather> elem = new Pair<String, Weather>(r, w);
		List<Pair<String, Weather>> l = new ArrayList<Pair<String, Weather>>();
		l.add(elem);
		addEvent(new SetWeatherEvent(t + sim.getTime(), l));
	}


	public TrafficSimulator getSim() {
		return sim;
	}
	
	@Override
	public void addObserver(TrafficSimObserver o) {
		sim.addObserver(o);
	}


	@Override
	public void removeObserver(TrafficSimObserver o) {
		sim.removeObserver(o);
	}
	
	
	public void addEvent(Event e) {
		sim.addEvent(e);
	}

}
