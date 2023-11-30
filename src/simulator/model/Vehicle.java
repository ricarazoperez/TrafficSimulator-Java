package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import Exceptions.ExecutionException;
import Exceptions.InvalidArgumentException;

public class Vehicle extends SimulatedObject implements Comparable <Vehicle> {

	//Atributos
	
	private List<Junction> itinerary;
	private int maxSpeed;
	private int actualSpeed;
	private VehicleStatus state;
	private Road road;
	private int location;
	private int contClass;
	private int totalPollution;
	private int totalDistance;
	private int lastJunct;
	
	//Constructora
	
	Vehicle (String id, int maxSpeed, int contClass, List<Junction> itinerary) throws InvalidArgumentException{
		super(id);
		if (maxSpeed < 0 ) throw new InvalidArgumentException("Speed must be > 0");
		if (contClass < 0 || contClass > 10) throw new InvalidArgumentException("ContClass must be between 0 and 10.");
		if (itinerary.size() < 2) throw new InvalidArgumentException("Itinerary must have at least source and destination");
		this.maxSpeed = maxSpeed;
		this.contClass = contClass;
		actualSpeed = 0;
		state = VehicleStatus.PENDING;
		road = null;
		location = 0;
		totalPollution = 0;
		totalDistance = 0;
		this.itinerary  = Collections.unmodifiableList(new ArrayList<>(itinerary)); 
		lastJunct = 0;
		
		
	}
	
	//Getters
	
	public int getLocation() {
		return location;
	}
	
	public int getSpeed() {
		return actualSpeed;
	}
	
	public int getContClass() {
		return contClass;
	}
	
	public VehicleStatus getStatus() {
		return state;
	}
	
	public List<Junction> getItinerary(){
		return itinerary;
	}
	
	public Road getRoad() {
		return road;
	}
	
	public int getMaxSpeed() {
		return maxSpeed;
	}
	
	public int getTotalCO2() {
		return totalPollution;
	}
	
	public int getDistance() {
		return totalDistance;
	}
	
	public String getState() {
		if(state == VehicleStatus.PENDING) return "Pending";
		else if(state == VehicleStatus.WAITING) return "Waiting:" + itinerary.get(lastJunct).getId();
		else if(state == VehicleStatus.ARRIVED) return "Arrived";
		else return road.getId() + ":" + location;
	}
	
	//Métodos 
	
	void setSpeed(int s) throws InvalidArgumentException {
		
		if(this.state == VehicleStatus.TRAVELING) {
			if (s < 0) throw new InvalidArgumentException("Speed must be > 0"); 
			if (s > maxSpeed) actualSpeed = maxSpeed;
			else actualSpeed = s;
		}
	}
	
	void setContaminationClass(int c) throws InvalidArgumentException {
		if (contClass < 0 || contClass > 10) throw new InvalidArgumentException("ContClass must be between 0 and 10.");
		contClass = c;
	}
	
	
	@Override
	void advance(int time) throws InvalidArgumentException, ExecutionException {
		if(state == VehicleStatus.TRAVELING) {
			int previousLocation = location;
			Junction actualJunction;
			
			location = location + actualSpeed;
			if(location > road.getLength()) location = road.getLength();
			int c = contClass * (location - previousLocation);
			totalDistance+= location - previousLocation;
			totalPollution += c;
			road.addContamination(c);
			if (location == road.getLength()){
				lastJunct++;
				actualJunction = itinerary.get(lastJunct);
				actualJunction.enter(this);
				actualSpeed = 0;
				state = VehicleStatus.WAITING;
			}
		}
		
	}
	
	void moveToNextRoad() throws ExecutionException {
		
		
		if(state != VehicleStatus.PENDING && state != VehicleStatus.WAITING) throw new ExecutionException("Vehicle status must be PENDING or WAITING");
	
		Junction actualJunction = itinerary.get(lastJunct);
		
		if(state == VehicleStatus.PENDING) {
			road = actualJunction.roadTo(itinerary.get(lastJunct + 1));
			state = VehicleStatus.TRAVELING;
			road.enter(this);
		}
		else if(lastJunct < (itinerary.size() - 1)) {
			road.exit(this);
			road = actualJunction.roadTo(itinerary.get(lastJunct + 1));
			state = VehicleStatus.TRAVELING;
			location = 0;
			road.enter(this);
		}else {
			road.exit(this);
			actualSpeed = 0;
			state = VehicleStatus.ARRIVED;
		}
		

	}
	

	
	public JSONObject report() {
		
		JSONObject jo = new JSONObject();
		jo.put("id", _id);
		jo.put("speed", actualSpeed);
		jo.put("distance", totalDistance);
		jo.put("co2", totalPollution);
		jo.put("class", contClass);
		jo.put("status", state.toString());
		if(state != VehicleStatus.PENDING && state != VehicleStatus.ARRIVED) {
			jo.put("road", road);
			jo.put("location", location);
		}
		return jo;
	}

	@Override
	
	public int compareTo(Vehicle o) {
		
		return this.location > o.location ? -1 : this.location < o.location ? 1 : 0;
	}
	
}
