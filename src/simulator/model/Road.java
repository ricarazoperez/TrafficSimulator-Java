package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import Exceptions.ExecutionException;
import Exceptions.InvalidArgumentException;

public abstract class Road extends SimulatedObject {

	//Atributos
	
	protected Junction srcJunc;
	protected Junction destJunc;
	protected int length;
	protected int maxSpeed;
	protected int actualSpeedLimit; 
	protected int contLimit;
	protected Weather weather;
	protected int totalPollution;
	protected List<Vehicle> vehicles;
	
	
	//Constructora
	
	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, 
		 int length, Weather weather) throws InvalidArgumentException{
		
		super(id);
		if(maxSpeed < 0) throw new InvalidArgumentException("RoadSpeed must be > 0");
		if(contLimit < 0) throw new InvalidArgumentException("ContLimit must be > 0");
		if(length < 0) throw new InvalidArgumentException("RoadLength must be > 0");
		if(srcJunc == null || destJunc == null || weather == null) throw new InvalidArgumentException ("SrcJunc, DestJunc and weather must be != null");
		
		this.srcJunc = srcJunc;
		this.destJunc = destJunc;
		this.maxSpeed = maxSpeed;
		actualSpeedLimit = maxSpeed;
		this.contLimit = contLimit;
		this.length = length;
		this.weather = weather;
		totalPollution = 0;
		vehicles = new ArrayList<Vehicle>();
		srcJunc.addOutGoingRoad(this);
		destJunc.addIncomingRoad(this);
	}

	
	//Getters
	
	public int getLength() {
		return length;
	}
	
	public Junction getDest() {
		return destJunc;
	}
	
	public Junction getSrc() {
		return srcJunc;
	}
	
	public int getTotalCO2() {
		return totalPollution;
	}
	
	public int getCO2Limit() {
		return contLimit;
	}
	
	public Weather getWeather() {
		return weather;
	}
	
	public int getMaxSpeed() {
		return maxSpeed;
	}


	public int getSpeedLimit() {
		return actualSpeedLimit;
	}
	
	//Métodos abstractos
	
	abstract void reduceTotalContamination();
	
	abstract void updateSpeedLimit();
	
	abstract int calculateVehicleSpeed(Vehicle v) throws InvalidArgumentException;
	
	//Métodos 
	
	void enter(Vehicle v) throws ExecutionException {
		
		if(v.getLocation() != 0 || v.getSpeed() != 0) throw new ExecutionException("Vehicle can't enter to the next road");

		vehicles.add(v);
	}
	
	void exit(Vehicle v) {
		vehicles.remove(v);
	}
	
	void setWeather(Weather w) throws InvalidArgumentException {
		if(w == null) throw new InvalidArgumentException("Weather must be a valid value");
		weather = w;
	}

	void addContamination(int c) throws InvalidArgumentException {
		if (c < 0) throw new InvalidArgumentException("Contamination can't be < 0");
		totalPollution+= c;
	}
	
	@Override
	void advance(int time) throws InvalidArgumentException, ExecutionException {
		
		reduceTotalContamination();
		updateSpeedLimit();
		for(int i = 0; i < vehicles.size(); i++) {
			vehicles.get(i).setSpeed(calculateVehicleSpeed(vehicles.get(i)));
			vehicles.get(i).advance(time);
		}
		
		
		Collections.sort(vehicles); 
	}


	@Override
	public JSONObject report() {
	
		
		JSONObject jo = new JSONObject();
		
		jo.put("id", _id);
		jo.put("speedlimit", actualSpeedLimit);
		jo.put("weather", weather.toString());
		jo.put("co2", totalPollution);
		JSONArray ja = new JSONArray();
		for(int i = 0; i < vehicles.size(); i++) {
			ja.put(vehicles.get(i).getId());	
		}
		jo.put("vehicles", ja);
		
		return jo;
		
	}
	
}
