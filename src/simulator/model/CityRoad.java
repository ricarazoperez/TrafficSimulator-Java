package simulator.model;

import Exceptions.InvalidArgumentException;

public class CityRoad extends Road {

	//Constructor
	
	CityRoad(String id, int maxSpeed,Junction srcJunc,Junction destJunc, int contLimit, int length, Weather weather) throws InvalidArgumentException {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
		
	}
	
	//Métodos

	@Override
	void reduceTotalContamination() {
		int x;
		
		if (weather == Weather.WINDY || weather == Weather.STORM) x = 10;
		else x = 2;
		totalPollution = totalPollution - x;
		if(totalPollution < 0) totalPollution = 0;
	}

	@Override
	void updateSpeedLimit() {
		//SpeedLimit is always maxSpeed
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) throws InvalidArgumentException {
		int s = actualSpeedLimit;
		int f = v.getContClass();
		
		return (int) (((11.0-f)/11.0)*s);
	}

}
