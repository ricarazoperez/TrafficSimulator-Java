package simulator.model;

import Exceptions.InvalidArgumentException;

public class InterCityRoad extends Road {

	//Constructor
	
	InterCityRoad(String id, Junction srcJunc, Junction destJunc,
			int maxSpeed, int contLimit, int length, Weather weather) throws InvalidArgumentException {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
		
	}

	//Métodos
	
	@Override
	void reduceTotalContamination() {
		int tc = totalPollution;
		int x;
		
		if(weather == Weather.SUNNY) x = 2;
		else if (weather == Weather.CLOUDY) x = 3;
		else if (weather == Weather.RAINY) x = 10;
		else if (weather == Weather.WINDY) x = 15;
		else x = 20;
		
		totalPollution = (int)(((100.0 - x)/100.0)*tc);
		
	}

	@Override
	void updateSpeedLimit() {
		
		if(totalPollution > contLimit) actualSpeedLimit = (int)(maxSpeed*0.5);
		else actualSpeedLimit = this.maxSpeed;
		
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) throws InvalidArgumentException {
		
		if(weather != Weather.STORM) return actualSpeedLimit;
	
		else  return (int) (actualSpeedLimit * 0.8);
	}

}
