package simulator.view;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Atributos
	private JLabel lTime;
	private JLabel lEvent;
	
	
	//Constructor
	public StatusBar(Controller ctrl) {
		ctrl.addObserver(this);
		lTime  = new JLabel("Time:  0");
		lEvent = new JLabel("Welcome!");
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(lTime);
		this.add(lEvent);
	}
	
	
	//Metodos
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		lTime.setText("Time:  " + time);
		lEvent.setText(""); 
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		lEvent.setText("Event added: " + e.toString());
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		lTime.setText("Time:  0");
		lEvent.setText("Welcome!");
		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		
	}

	@Override
	public void onError(String err) {
		
	}
	

}
