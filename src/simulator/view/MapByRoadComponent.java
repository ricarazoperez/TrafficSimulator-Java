package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;
import simulator.model.Weather;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Atributos
	private static final int _JRADIUS = 10;
	
	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_SRC_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);

	
	private Image _car;
	private Image _cloud;
	private Image _wind;
	private Image _storm;
	private Image _rain;
	private Image _sun;
	
	
	private RoadMap _map;
	
	//Constructor
	public MapByRoadComponent(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
		this.setPreferredSize(new Dimension(300, 200));
	}
	
	private void initGUI() {
		_car = loadImage("car.png");
		_cloud = loadImage("cloud.png");
		_wind = loadImage("wind.png");
		_storm = loadImage("storm.png");
		_rain = loadImage("rain.png");
		_sun = loadImage("sun.png");
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			drawMap(g);
		}
	}
	
	private void drawMap(Graphics g) {
		drawRoads(g);
		drawJunctions(g);
		drawVehicles(g);
		drawLastComponents(g);
	}
	
	private void drawRoads(Graphics g) {
		
		for(int i = 0; i < _map.getRoads().size(); i++) {
			g.setColor(Color.black);
			g.drawLine(50, (i + 1) * 50, this.getWidth() - 100, (i + 1) * 50);
		}
	}
	
	private void drawJunctions(Graphics g) {
		
		
		for (int i = 0; i < _map.getRoads().size(); i++) {

			Road r = _map.getRoads().get(i);
			int srcX = 50;
			int destX = this.getWidth() - 100; 
			int Y = (i+1) * 50;
			
			// draw a circle with center at (x,y) with radius _JRADIUS
			g.setColor(_JUNCTION_SRC_COLOR);
			g.fillOval(srcX - _JRADIUS / 2, Y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
			// draw the junction's identifier at (x,y)
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(r.getSrc().getId(), srcX, Y - 5);
			g.setColor(Color.red);
			int idx = r.getDest().getGreenLightIndex();
			if (idx != -1 && r.equals(r.getDest().getInRoads().get(idx))) g.setColor(Color.green);
			g.fillOval(destX - _JRADIUS / 2, Y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(_map.getRoads().get(i).getDest().getId(), destX, Y - 5);		
		}
	}
	
	private void drawVehicles(Graphics g) {
		
		for (Vehicle v : _map.getVehicles()) {
			if (v.getStatus() != VehicleStatus.ARRIVED) {
				
				Road r = v.getRoad();
				int i = _map.getRoads().indexOf(r);
				int x1 = 50;
				int x2 = this.getWidth() - 100;
				int vY = ((i + 1) * 50) - 5;
				int vX = x1 + (int) ((x2 - x1) * ((double) v.getLocation() /(double) r.getLength()));
				g.drawImage(_car, vX, vY - 6, 16, 16, this);
				g.drawString(v.getId(), vX, vY - 6);
			}
		}
		
	}
	
	private void drawLastComponents(Graphics g) {
		
		g.setColor(Color.black);
		int xId = 20;
		int xW  = this.getWidth() - 80;
		int xC  = this.getWidth() - 40;
		for (int i = 0; i < _map.getRoads().size(); i++) {
			Road r = _map.getRoads().get(i);
			int yId = (i + 1)  * 50;
			int yW  = ((i + 1) * 50) - 20;
			int yC  = ((i + 1) * 50) - 20;
			//Id Road
			g.drawString(r.getId(), xId, yId);
			//Weather
			Weather w = r.getWeather();
			if(w == Weather.CLOUDY) g.drawImage(_cloud, xW, yW, 32, 32, this);
			else if (w == Weather.RAINY)  g.drawImage(_rain, xW, yW, 32, 32, this);
			else if (w == Weather.STORM)  g.drawImage(_storm, xW, yW, 32, 32, this);
			else if (w == Weather.SUNNY)  g.drawImage(_sun, xW, yW, 32, 32, this);
			else  g.drawImage(_wind, xW, yW, 32, 32, this);
			
			//Cont
			int c = (int) Math.floor(Math.min((double) r.getTotalCO2()/(1.0 + (double) r.getCO2Limit()), 1.0) / 0.19);
			g.drawImage(loadImage("cont_" + c + ".png"), xC, yC, 32, 32, this);
		}
		
		
	}
	
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}

	public void update(RoadMap map) {
		_map = map;
		repaint();
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
	
}
