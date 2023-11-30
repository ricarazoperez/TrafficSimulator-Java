package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Atributos
	private String[] columnas = { "Time", "Desc."};
	private List<Event> events = new ArrayList<Event>();
	
	//Constructor
	
	public EventsTableModel(Controller ctrl) {
		ctrl.addObserver(this);
	}
	
	//Metodos
	
	@Override
	public int getRowCount() {
		return events.size();
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

		if(rowIndex >= events.size() || columnIndex >= columnas.length) return null;
		else {
			Event e = events.get(rowIndex);
			if(columnIndex == 0) return e.getTime();
			else return e.toString();
		}
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		
		this.events.clear();
		for(Event e : events) this.events.add(e);
		this.fireTableDataChanged();
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.events.clear();
		for(Event e : events) this.events.add(e);
		this.fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.events.add(e);
		this.fireTableDataChanged();
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.events.clear();
		for(Event e : events) this.events.add(e);
		this.fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onError(String err) {
	}
}
