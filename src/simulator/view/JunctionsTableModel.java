package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver {
	
	//Atributos
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] columnas = {"Id", "Green", "Queues"}; 
	private List<Junction> junctions = new ArrayList<Junction>();
	
	//Constructor
	
	public JunctionsTableModel(Controller ctrl) {
		ctrl.addObserver(this);
	}

	
	//Metodos
	
	@Override
	public int getRowCount() {
		return junctions.size();
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
		
		if(rowIndex >= junctions.size() || columnIndex >= columnas.length) return null;
		else {
			Junction j = junctions.get(rowIndex);
			if(columnIndex == 0) return j.getId();
			else if(columnIndex == 1) return j.getGreen();
			else return j.getQueues();
		}
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		junctions.clear();
		for(Junction j : map.getJunctions()) junctions.add(j);
		this.fireTableDataChanged();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		junctions.clear();
		for(Junction j : map.getJunctions()) junctions.add(j);
		this.fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		junctions.clear();
		for(Junction j : map.getJunctions()) junctions.add(j);
		this.fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {

	}

	@Override
	public void onError(String err) {
		
	}

}
