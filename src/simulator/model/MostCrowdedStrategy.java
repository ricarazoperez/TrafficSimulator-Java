package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {

	//Atributos
	
	private int timeSlot;
	
	//Constructor
	
	public MostCrowdedStrategy(int timeSlot){
		this.timeSlot = timeSlot;
	}
	
	//Métodos 
	
	@Override
	
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		
		if (roads.isEmpty())     						 return -1;
		else if(currGreen == -1) 						 return mostCrowded(0,qs);
		else if(currTime - lastSwitchingTime < timeSlot) return currGreen;
		else 											 return mostCrowded((currGreen + 1) % qs.size() , qs);	
	}
	
	//Metodo local
	
	private int mostCrowded(int index, List <List<Vehicle>> qs) {
		int maxSize = qs.get(index).size();
		int nextGreen = index;
		
		
		for (int i = 0; i < qs.size(); i++) {
			if(qs.get(index).size() > maxSize) {
				maxSize = qs.get(index).size();
				nextGreen = index;
			}
			index = (index + 1) % qs.size(); 
		}
		
		return nextGreen;
		
	}

}
