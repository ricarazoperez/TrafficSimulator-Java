package simulator.factories;



import simulator.model.Event;
import simulator.model.NewCityRoadEvent;

public class NewCityRoadEventBuilder extends NewRoadEventBuilder {

	//Constructor
	
	 public NewCityRoadEventBuilder() {
		super("new_city_road");
	}

	//Metodos
	 
	@Override
	Event createTheRoad() {
		
		return new NewCityRoadEvent(time, id, src, dest, length, co2limit, maxSpeed, weather);
	}
	
}
