package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver {

	//Atributos
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] columnas = {"Id", "Location", "Itinerary", "CO2 Class", "Max.Speed",
			 "Speed", "Total CO2", "Distance"};
	private List<Vehicle> vehicles = new ArrayList<Vehicle>();

	//Constructor

	public VehiclesTableModel(Controller ctrl) {
		ctrl.addObserver(this);
	}

	//Metodos

	@Override
	public int getRowCount() {
		return vehicles.size();
	}

	@Override
	public int getColumnCount() {
		return columnas.length;
	}

	public String getColumnName(int col) {
		return columnas[col];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(rowIndex >= vehicles.size() || columnIndex >= columnas.length) return null;
		else {
			Vehicle v = vehicles.get(rowIndex);
			if(columnIndex == 0) return v.getId();
			else if(columnIndex == 1) return v.getState();
			else if(columnIndex == 2) return v.getItinerary();
			else if(columnIndex == 3) return v.getContClass();
			else if(columnIndex == 4) return v.getMaxSpeed();
			else if(columnIndex == 5) return v.getSpeed();
			else if(columnIndex == 6) return v.getTotalCO2();
			else return v.getDistance();
		}
	}


	//Metodos
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		vehicles.clear();
		for(Vehicle v : map.getVehicles()) vehicles.add(v);
		this.fireTableDataChanged();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		vehicles.clear();
		for(Vehicle v : map.getVehicles()) vehicles.add(v);
		this.fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {	
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		vehicles.clear();
		for(Vehicle v : map.getVehicles()) vehicles.add(v);
		this.fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {	
	}

	@Override
	public void onError(String err) {
	}

}
