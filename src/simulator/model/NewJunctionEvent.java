package simulator.model;

import Exceptions.ExecutionException;
import Exceptions.InvalidArgumentException;

public class NewJunctionEvent extends Event {

	//Atributos 
	private String id;
	private LightSwitchingStrategy lsStrategy;
	private DequeuingStrategy dqStrategy;
	private int xCoor;
	private int yCoor;
	
	
	
	//Constructor
	
	public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy,
			DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		
		super(time);
		this.id = id;
		this.lsStrategy = lsStrategy;
		this.dqStrategy = dqStrategy;
		this.xCoor = xCoor;
		this.yCoor = yCoor;
		
	}

	@Override
	void execute(RoadMap map) throws InvalidArgumentException, ExecutionException {
		
		Junction NewJunction = new Junction(id, lsStrategy, dqStrategy, xCoor, yCoor);
		map.addJunction(NewJunction);
		
	}
	
	public String toString() {
		return "New Junction '" + this.id +"'";
	}
}
