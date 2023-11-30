package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import Exceptions.ExecutionException;

public class RoadMap {
	
	//Atributos
	
	private List<Junction> junctions;
	private List<Road> roads;
	private List<Vehicle> vehicles;
	private Map<String, Junction> idJunctions;
	private Map<String, Road> idRoads;
	private Map<String, Vehicle> idVehicles;
	
	//Constructor
	
	RoadMap(){
		junctions   = new ArrayList<Junction>();
		roads       = new ArrayList<Road>();
		vehicles    = new ArrayList<Vehicle>();
		idJunctions = new HashMap<String,Junction>();
		idRoads     = new HashMap<String,Road>();
		idVehicles  = new HashMap<String, Vehicle>();
		
	}
	
	//Métodos 
	
	void addJunction(Junction j) throws ExecutionException {
		
		if(idJunctions.containsKey(j.getId())) throw new ExecutionException("There is already a junction with the same id");
		junctions.add(j);
		idJunctions.put(j.getId(), j);
		
	}
	
	void addRoad(Road r) throws ExecutionException {
		
		if(idRoads.containsKey(r.getId())) throw new ExecutionException("There is already a road with the same id");
		if((!idJunctions.containsKey(r.getSrc().getId())) || (!idJunctions.containsKey(r.getDest().getId()))) throw new ExecutionException("RoadMap doesn´t contain the junctions which connect the new Road");
		roads.add(r);
		idRoads.put(r.getId(), r);
		
		
		
	}
	
	void addVehicle(Vehicle v) throws ExecutionException {
		
		
		if(idVehicles.containsKey(v.getId())) throw new ExecutionException("There is already a vehicle with the same id");
		for(int i = 0; i < v.getItinerary().size() - 1; i++) {
			Junction uno = v.getItinerary().get(i);
			Junction dos = v.getItinerary().get(i + 1);
			if(uno.roadTo(dos) == null) throw new ExecutionException("Invalid itinerary");
			
		}
		
		vehicles.add(v);
		idVehicles.put(v.getId(), v);
	}
	
	void reset() {
		junctions.clear();
		roads.clear();
		vehicles.clear();
		idJunctions.clear();
		idRoads.clear();
		idVehicles.clear();
		
	}
	
	
	
	public JSONObject report() {
		JSONObject jo = new JSONObject();
		
		JSONArray jsonJunctions = new JSONArray();
		JSONArray jsonRoads     = new JSONArray();
		JSONArray jsonVehicles  = new JSONArray();
		
		
		for(int i = 0; i < junctions.size(); i++) jsonJunctions.put(junctions.get(i).report());
		
		for(int j = 0; j < roads.size(); j++)     jsonRoads.put(roads.get(j).report());
		
		for(int z = 0; z < vehicles.size(); z++)  jsonVehicles.put(vehicles.get(z).report());
		
		
		jo.put("junctions", jsonJunctions);
		jo.put("roads", jsonRoads);
		jo.put("vehicles", jsonVehicles);
		
		
		return jo;
	}
	
	//Getters 
	
	public Junction getJunction(String id) {
		if(idJunctions.containsKey(id)) return idJunctions.get(id);
		else return null;
	}
	
	public Road getRoad(String id) {
		if(idRoads.containsKey(id)) return idRoads.get(id);
		else return null;
	}
	
	public Vehicle getVehicle(String id) {
		if(idVehicles.containsKey(id)) return idVehicles.get(id);
		else return null;
	}
	
	public List<Junction> getJunctions(){
		return Collections.unmodifiableList(new ArrayList<>(junctions));
	}
	
	public List<Road> getRoads(){
		return Collections.unmodifiableList(new ArrayList<>(roads));
	}
	
	public List<Vehicle> getVehicles(){
		return Collections.unmodifiableList(new ArrayList<>(vehicles));
	}
	
}
