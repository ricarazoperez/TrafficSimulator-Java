package simulator.model;

import Exceptions.ExecutionException;
import Exceptions.InvalidArgumentException;

public class NewCityRoadEvent extends NewRoadEvent {

	
	
	//Constructor
	public NewCityRoadEvent(int time, String id, String srcJunc, String destJunc, int length,
			int co2Limit, int maxSpeed, Weather weather){
		super(time, id, srcJunc, destJunc, length, co2Limit, maxSpeed, weather);
	}

	
	//Metodos
	
	void execute(RoadMap map) throws InvalidArgumentException, ExecutionException {
		
		CityRoad NewCityRoad = new CityRoad(id, maxSpeed, map.getJunction(srcJunc), map.getJunction(destJunc), 
											co2Limit, length, weather);
		map.addRoad(NewCityRoad);
	}
	
	public String toString() {
		return "New CityRoad '" + this.id +"'";
	}
}
