package simulator.model;

import Exceptions.ExecutionException;
import Exceptions.InvalidArgumentException;

public class NewRoadEvent extends Event {
	
	//Atributos
	protected String id;
	protected String srcJunc;
	protected String destJunc;
	protected int length;
	protected int co2Limit;
	protected int maxSpeed; 
	protected Weather weather;
	
	
	//Constructor
	public NewRoadEvent(int time, String id, String srcJunc, String destJunc, 
						int length, int co2Limit, int maxSpeed, Weather weather) {
		super(time);
		this.id 	   = id;
		this.srcJunc   = srcJunc;
		this.destJunc  = destJunc;
		this.length    = length;
		this.co2Limit  = co2Limit;
		this.maxSpeed  = maxSpeed;
		this.weather   = weather;
		
	}
	
	//Metodos

	@Override
	void execute(RoadMap map) throws InvalidArgumentException, ExecutionException {
	}
	
	
}
