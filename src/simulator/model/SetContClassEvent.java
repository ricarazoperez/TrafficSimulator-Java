package simulator.model;

import java.util.List;

import Exceptions.ExecutionException;
import Exceptions.InvalidArgumentException;
import simulator.misc.Pair;

public class SetContClassEvent extends Event {

	//Atributos
	// cs contiene  ==>  idVehicle | contClass 
	private List<Pair<String, Integer>> cs;
	
	//Constructor
	public SetContClassEvent(int time, List<Pair<String,Integer>> cs) throws InvalidArgumentException {
		super(time);
		if(cs == null) throw new InvalidArgumentException("Cs list can´t be null");
		this.cs = cs;
	}
	
	//Metodos

	@Override
	void execute(RoadMap map) throws InvalidArgumentException, ExecutionException {
		
		for (int i = 0; i < cs.size(); i++) {
			if(map.getVehicle(cs.get(i).getFirst()) == null) throw new ExecutionException("Vehicle " + 
														cs.get(i).getFirst() + " doesn´t exist in map");
			map.getVehicle(cs.get(i).getFirst()).setContaminationClass(cs.get(i).getSecond());
		}
		
	}
	
	public String toString() {
		String s = "Change CO2 class: [";
		
		for(int i = 0; i < cs.size(); i++) {
			s += "(" + cs.get(i).getFirst() + "," + cs.get(i).getSecond() + ")";
			if(i != cs.size() - 1) s += ", ";
		}
		
		s += "]";
		return s;
	}
}
