package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver {
	
	//Atributos
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] columnas = {"Id", "Length", "Weather", "Max.Speed", "Speed Limit",
								 "Total CO2", "CO2 Limit"};
	private List<Road> roads = new ArrayList<Road>();
	//Constructor
	
	public RoadsTableModel(Controller ctrl) {
		ctrl.addObserver(this);
	}

	//Metodos
	
	@Override
	public int getRowCount() {
		return roads.size();
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
		
		if(rowIndex >= roads.size() || columnIndex >= columnas.length) return null;
		else {
			Road r = roads.get(rowIndex);
			if(columnIndex == 0) return r.getId();
			else if(columnIndex == 1) return r.getLength();
			else if(columnIndex == 2) return r.getWeather();
			else if(columnIndex == 3) return r.getMaxSpeed();
			else if(columnIndex == 4) return r.getSpeedLimit();
			else if(columnIndex == 5) return r.getTotalCO2();
			else return r.getCO2Limit();
		}
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		roads.clear();
		for(Road r : map.getRoads()) roads.add(r);
		this.fireTableDataChanged();
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		roads.clear();
		for(Road r : map.getRoads()) roads.add(r);
		this.fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		roads.clear();
		for(Road r : map.getRoads()) roads.add(r);
		this.fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		
	}

	@Override
	public void onError(String err) {
	}

}
