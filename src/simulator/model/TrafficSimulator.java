package simulator.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import Exceptions.ExecutionException;
import Exceptions.InvalidArgumentException;
import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable <TrafficSimObserver>{

	//Atributos
	
	private RoadMap roadMap;
	private List<Event> listEvent;
	private int time;
	private List<TrafficSimObserver> observers;
	
	//Constructor
	
	public TrafficSimulator() {
		
		roadMap = new RoadMap();
		listEvent = new SortedArrayList<Event>();
		time = 0;
		observers = new ArrayList<TrafficSimObserver>();
	}
	
	
	//Metodos
	
	public void addEvent(Event e) {
		listEvent.add(e);
		for (TrafficSimObserver o : observers) o.onEventAdded(roadMap, listEvent, e, time);
	}
	
	public void advance() throws InvalidArgumentException, ExecutionException {
		
		
		
		//Incremento time simulacion
		time++;
		
		for(TrafficSimObserver o : observers) o.onAdvanceStart(roadMap, listEvent, time);
		
		//Ejecuto eventos que tengan time = simulacion time y luego se borran de listEvent
		try {
		
			Iterator<Event> it = listEvent.iterator();
			while(it.hasNext()) {
				Event e = it.next();
				if(e.getTime() == time) {
					e.execute(roadMap);
					it.remove();
				}	
			}
		
			List<Junction> junctions = roadMap.getJunctions();
			List<Road>     roads     = roadMap.getRoads();
		
			//llamo advance de todos los cruces
			for(int j = 0; j < junctions.size(); j++) {
				junctions.get(j).advance(time);
			}
		
			//llamo advance de todas las carreteras
			for(int z = 0; z < roads.size(); z++) {
				roads.get(z).advance(time);
			}
		}catch(InvalidArgumentException | ExecutionException e ) {
			for(TrafficSimObserver o : observers) o.onError(e.getMessage());
			throw e;
		}
		
		for(TrafficSimObserver o : observers) o.onAdvanceEnd(roadMap, listEvent, time);
		
		
	}
	
	public void reset() {
		roadMap.reset();
		listEvent.clear();
		time = 0;
		for(TrafficSimObserver o : observers) o.onReset(roadMap, listEvent, time);
		
	}
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		
		jo.put("time", time);
		jo.put("state", roadMap.report());
		
		return jo;
	}
	
	public RoadMap getRoadMap() {
		return roadMap;
	}
	
	public int getTime() {
		return time;
	}
	
	@Override
	public void addObserver(TrafficSimObserver o) {
		observers.add(o);
		o.onRegister(roadMap, listEvent, time);
	}


	@Override
	public void removeObserver(TrafficSimObserver o) {
		observers.remove(o);
	}

}
