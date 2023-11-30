package simulator.model;

import Exceptions.ExecutionException;
import Exceptions.InvalidArgumentException;

public class NewInterCityRoadEvent extends NewRoadEvent {

	
	//Constructor
	
	public NewInterCityRoadEvent(int time, String id, String srcJunc, String destJunc,
								 int length, int co2Limit, int maxSpeed,Weather weather) {
		super(time, id, srcJunc, destJunc, length, co2Limit, maxSpeed, weather);
		
	}
	
	
	//Metodos
	
	void execute(RoadMap map) throws InvalidArgumentException, ExecutionException {
		
		InterCityRoad NewInterCityRoad = new InterCityRoad(id, map.getJunction(srcJunc), 
										map.getJunction(destJunc), maxSpeed, co2Limit, length, weather);
		map.addRoad(NewInterCityRoad);
	}
	
	public String toString() {
		return "New InterCityRoad '" + this.id +"'";
	}
}
