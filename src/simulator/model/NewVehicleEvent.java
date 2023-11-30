package simulator.model;

import java.util.ArrayList;
import java.util.List;

import Exceptions.ExecutionException;
import Exceptions.InvalidArgumentException;

public class NewVehicleEvent extends Event {

	//Atributos
	private String id;
	private int maxSpeed;
	private int contClass;
	private List<String> itinerary;
	
	//Constructor
	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
		super(time);
		this.id        = id;
		this.maxSpeed  = maxSpeed;
		this.contClass = contClass;
		this.itinerary = itinerary;
		
	}

	
	//Metodos 
	
	@Override
	void execute(RoadMap map) throws InvalidArgumentException, ExecutionException {
		
		List<Junction> junctionItinerary = new ArrayList<Junction>();
		
		for (int i = 0; i < itinerary.size(); i++) {
			if(map.getJunction(itinerary.get(i)) == null) throw new ExecutionException("Junctions don´t exist for the itinerary given");
		}
		
		for(int j = 0; j < itinerary.size(); j++) {
			junctionItinerary.add(map.getJunction(itinerary.get(j)));
		}
		
		Vehicle NewVehicle = new Vehicle(id, maxSpeed, contClass, junctionItinerary);
		map.addVehicle(NewVehicle);
		NewVehicle.moveToNextRoad();
	}
	
	public String toString() {
		return "New Vehicle '" + this.id +"'";
	}

}
