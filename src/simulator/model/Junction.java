package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import Exceptions.ExecutionException;
import Exceptions.InvalidArgumentException;

public class Junction extends SimulatedObject {
	
	//Atributos
	
	private List<Road> incomingRoads;
	private Map<Junction,Road> outgoingRoads;
	private List<List<Vehicle>> queues;
	private Map<Road,List<Vehicle>> roadQueue;
	private int greenLightIndex;
	private int lastSwitchingTime;
	private LightSwitchingStrategy lsStrategy;
	private DequeuingStrategy dqStrategy;
	private int xCoor;
	private int yCoor;
	
	//Constructor
	
	Junction(String id, LightSwitchingStrategy lsStrategy, 
			DequeuingStrategy dqStrategy, int xCoor, int yCoor) throws InvalidArgumentException{
		
		super(id);
		if(lsStrategy == null || dqStrategy == null) throw new InvalidArgumentException("LsStrategy and DqStrategy can't be null");
		if(xCoor < 0 || yCoor < 0) throw new InvalidArgumentException("Cordinates must be positive");
		lastSwitchingTime = 0;
		greenLightIndex = -1;
		this.lsStrategy = lsStrategy;
		this.dqStrategy = dqStrategy;
		this.xCoor = xCoor;
		this.yCoor = yCoor;
		incomingRoads = new ArrayList<Road>();
		outgoingRoads = new HashMap<Junction, Road>();
		queues = new ArrayList <List <Vehicle>>(); 
		roadQueue = new HashMap<Road, List<Vehicle>>();
	}
	
	
	//getters
	
	public int getX() {
		return xCoor;
	}
	
	public int getY() {
		return yCoor;
	}
	
	public int getGreenLightIndex(){
		return greenLightIndex;
	}
	
	public List <Road> getInRoads() {
		return incomingRoads;
	}
	
	public String getGreen() {
		if(greenLightIndex != -1) return "" + greenLightIndex;
		else return "NONE";
	}
	
	public Map<Road,List<Vehicle>> getQueues() {
		 return roadQueue;
	}
	
	//Métodos
	
	void addIncomingRoad(Road r) throws InvalidArgumentException {
		
		if(r.getDest() != this) throw new InvalidArgumentException("Road destJunction must be the same as your actual junction");
		incomingRoads.add(r);
		List<Vehicle> vehicles = new LinkedList<Vehicle>();
		queues.add(vehicles);
		roadQueue.put(r, vehicles);
		
	}
	
	void addOutGoingRoad(Road r) throws InvalidArgumentException {
		
		if(outgoingRoads.containsKey(r.getDest())) throw new InvalidArgumentException("There is already a road conected with that junction");
		
		if(this != r.getSrc()) throw new InvalidArgumentException("The new road isn't an outgoingRoad");
		
		outgoingRoads.put(r.getDest(), r);
		
	}
	
	void enter(Vehicle v) throws ExecutionException {
		
		roadQueue.get(v.getRoad()).add(v);
		
	}
	
	Road roadTo(Junction j) {
		return outgoingRoads.get(j);
	}

	@Override
	void advance(int time) throws InvalidArgumentException, ExecutionException {
		
		//Extraccion de coches de la cola del cruce
		
		if(greenLightIndex != -1) {
			List<Vehicle> actualQueue = queues.get(greenLightIndex);
			List<Vehicle> list = new ArrayList<Vehicle>();
			list = dqStrategy.dequeue(actualQueue);	
			for(int i = 0; i < list.size(); i++) {
				list.get(i).moveToNextRoad();
				actualQueue.remove(list.get(i));
			}
		}
			
		//Semaforos
			
		int nextGreen = lsStrategy.chooseNextGreen(incomingRoads, queues, greenLightIndex, lastSwitchingTime,time);
		if(nextGreen != greenLightIndex) {
			greenLightIndex = nextGreen;
			lastSwitchingTime = time;
		}
	}

	@Override
	public JSONObject report() {
		
		JSONObject jo = new JSONObject();
		
		jo.put("id", _id.toString());
		if(greenLightIndex == -1) jo.put("green", "none");
		else				      jo.put("green", incomingRoads.get(greenLightIndex).getId());
		
		JSONArray joQueues = new JSONArray();
		for(int i = 0; i < queues.size(); i++) {
			JSONObject qi = new JSONObject();
		
			JSONArray vehicles = new JSONArray();
			for(int j = 0; j < queues.get(i).size(); j++) {
				vehicles.put(queues.get(i).get(j).getId());
			}
			qi.put("road", incomingRoads.get(i).getId());
			qi.put("vehicles", vehicles);
			joQueues.put(qi);
			
		}
		jo.put("queues", joQueues);
		
		return jo;
		
	}

}
