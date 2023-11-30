package simulator.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import Exceptions.InvalidArgumentException;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;

public class ControlPanel extends JPanel implements TrafficSimObserver{
	
	//Atributos
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private JToolBar toolBar;
	private JButton loadEventButton;
	private JButton contButton;
	private JButton weatherButton;
	private JButton runButton;
	private JButton stopButton;
	private JButton exitButton;
	private JSpinner ticks;
	private JLabel lticks;
	private boolean _stopped;
	
	
	
	//Constructor
	
	public ControlPanel(Controller _ctrl) {
		super();
		this._ctrl = _ctrl;
		_ctrl.addObserver(this);
		this.setLayout(new BorderLayout());
		initPanel();
		
	}
	
	private void initPanel() {
		
		initComponents();
		this.add(toolBar, BorderLayout.PAGE_START);
		
	}
	
	private void initComponents() {
		
		_stopped = true;
		toolBar = new JToolBar();
		loadEventButton = new JButton(new ImageIcon ("./resources/icons/open.png"));
		contButton = new JButton(new ImageIcon("./resources/icons/co2class.png"));
		weatherButton = new JButton(new ImageIcon("./resources/icons/weather.png"));
		runButton = new JButton(new ImageIcon("./resources/icons/run.png"));
		stopButton = new JButton(new ImageIcon("./resources/icons/stop.png"));
		lticks = new JLabel("Ticks:");
		exitButton = new JButton(new ImageIcon("./resources/icons/exit.png"));
		ticks = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
		ticks.setToolTipText("Simulation tick to run: 1-10000");
		ticks.setMaximumSize(new Dimension(100, 50));
		
		toolBar.add(loadEventButton);
		toolBar.add(contButton);
		toolBar.add(weatherButton);
		toolBar.add(runButton);
		toolBar.add(stopButton);
		toolBar.add(lticks);
		toolBar.add(ticks);
		toolBar.add(Box.createGlue());
		toolBar.add(exitButton);
		
		componentFunctions();
	}
	
	private void componentFunctions() {
		
		//loadEventButton
		loadEventButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("./resources/examples"));
				int respuesta = fc.showOpenDialog(null);
				if(respuesta == JFileChooser.APPROVE_OPTION) {
					File archivoElegido = fc.getSelectedFile();
					try {
					InputStream in = new FileInputStream(archivoElegido);
						_ctrl.reset();
						_ctrl.loadEvents(in);
					}catch(InvalidArgumentException | FileNotFoundException ex) {
						onError("Error");
					}
				}
			}
		});
		
		
		//contbutton
		contButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contDialog();
			}
		});
			
		//weatherbutton
		
		weatherButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				weatherDialog();
			}
		});
		//runButton
		runButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_stopped = false;
				enableToolBar(false);
				run_sim((int) ticks.getValue());
			}
		});
		
		//stopButton
		
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		
		
		//exitButton
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int i = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit", 
						"quit", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
				
				if(i == JOptionPane.YES_OPTION) System.exit(0);
			}
			
		});
	
	}
	
	private void run_sim(int n) {
		if(n > 0 && !_stopped) {
			try {
				_ctrl.run(n, null);
			}catch(Exception e) {
				onError(e.getMessage());
				_stopped = true;
				return;
			}
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					run_sim(n - 1);
				}
			});
		} else {
			enableToolBar(true);
			_stopped = true;
		}
	}
	
	private void enableToolBar(boolean b) {
		
		for(Component c : toolBar.getComponents()) {
			if (b) c.setEnabled(true);
			else c.setEnabled(false);
		}
		if(!b) stopButton.setEnabled(true);
		
	}
	
	
	private void stop() {
		_stopped = true;
	}
	
	private void contDialog() {
		ChangeCO2ClassDialog coDialog = new ChangeCO2ClassDialog((Frame) SwingUtilities.getWindowAncestor(this));
		int status = coDialog.open(_ctrl.getSim().getRoadMap().getVehicles());
		if(status == 1)
			try {
				_ctrl.nuevaCont(coDialog.getVehicle(), coDialog.getCont(), coDialog.getTicks());
			} catch (InvalidArgumentException e) {
				// TODO Auto-generated catch block
				onError(e.getMessage());
			}
		else coDialog.dispose();
	}
	
	private void weatherDialog() {
		ChangeWeatherDialog weDialog = new ChangeWeatherDialog((Frame) SwingUtilities.getWindowAncestor(this));
		List<Weather> weathers = new ArrayList<Weather>();
		for(int i = 0; i < Weather.values().length; i++) {
			weathers.add(Weather.values()[i]);
		}
		
		int status = weDialog.open(_ctrl.getSim().getRoadMap().getRoads(), weathers);
		if(status == 1) {
			try {
				_ctrl.nuevoWeather(weDialog.getRoad(), weDialog.getWeather(), weDialog.getTicks());
			} catch (InvalidArgumentException e) {
				onError(e.getMessage());
			}
		}else weDialog.dispose();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		ticks.setValue(10);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		
	}

	@Override
	public void onError(String err) {
		
		JOptionPane.showMessageDialog(null, err, "ERROR", JOptionPane.INFORMATION_MESSAGE);
		
	}

}
