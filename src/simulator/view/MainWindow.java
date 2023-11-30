package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;

public class MainWindow extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Atributos
	private Controller _ctrl;
	
	//Constructor
	
	public MainWindow(Controller ctrl) {
		super("Traffic Simulator");
		this._ctrl = ctrl;
		initGUI();
	}
	
	//Metodos
	
	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
		
		mainPanel.add(new ControlPanel(this._ctrl), BorderLayout.PAGE_START);
		mainPanel.add(new StatusBar(this._ctrl), BorderLayout.PAGE_END);
		
		JPanel viewsPanel = new JPanel(new GridLayout(1,2));
		mainPanel.add(viewsPanel, BorderLayout.CENTER);
		
		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(tablesPanel);
		
		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.Y_AXIS));
		viewsPanel.add(mapsPanel);
		
		//tables
		JPanel eventsView = createViewPanel(new JTable(new EventsTableModel(this._ctrl)), "Events");
		eventsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(eventsView);
		JPanel vehiclesView = createViewPanel(new JTable(new VehiclesTableModel(this._ctrl)), "Vehicles");
		vehiclesView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(vehiclesView);
		JPanel roadsView = createViewPanel(new JTable(new RoadsTableModel(this._ctrl)), "Roads");
		roadsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(roadsView);
		JPanel junctionsView = createViewPanel(new JTable(new JunctionsTableModel(this._ctrl)), "Junctions");
		junctionsView.setPreferredSize(new Dimension(500, 200));
		tablesPanel.add(junctionsView);
		
		
		//maps
		JPanel mapView = createViewPanel(new MapComponent(this._ctrl), "Map");
		mapView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapView);
		JPanel mapByRoadView = createViewPanel(new MapByRoadComponent(this._ctrl), "Map by Road");
		mapByRoadView.setPreferredSize(new Dimension(500, 400));
		mapsPanel.add(mapByRoadView);
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	private JPanel createViewPanel(JComponent c, String title) {
		JPanel p = new JPanel( new BorderLayout());
		p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1),
					title,TitledBorder.LEFT, TitledBorder.TOP));
		p.add(new JScrollPane(c));
		return p;
	}

}
