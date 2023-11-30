package simulator.factories;

import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;

public class NewInterCityRoadEventBuilder extends NewRoadEventBuilder{

	//Constructor
	
	public NewInterCityRoadEventBuilder() {
		super("new_inter_city_road");
		
	}

	//Metodos
	
	@Override
	Event createTheRoad() {
		
		return new NewInterCityRoadEvent(time, id, src, dest, length, co2limit, maxSpeed, weather);
	}

}
