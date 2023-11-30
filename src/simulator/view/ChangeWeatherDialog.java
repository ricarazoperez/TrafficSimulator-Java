package simulator.view;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.model.Road;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JPanel first;
	private JPanel second;
	private JPanel third;
	private JLabel description;
	private JLabel road;
	private JLabel weather;
	private JLabel ticks;
	private DefaultComboBoxModel<String> rModel;
	private DefaultComboBoxModel<Weather> wModel;
	private JComboBox <String> rBox;
	private JComboBox <Weather> wBox;
	private JSpinner sticks;
	private JButton salir;
	private JButton okey;
	private int status;
	
	public ChangeWeatherDialog(Frame parent) {
		super(parent, true);
		status = 0;
		mainPanel = new JPanel();
		this.setContentPane(mainPanel);
		this.setTitle("Change Road Weather");
		this.setLayout(new GridLayout(3,1, 5,5));
		this.setBounds(200, 200, 500, 300);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initDialog();
		this.pack();
	}
	
	private void initDialog() {
		
		first = new JPanel();
		second = new JPanel();
		third = new JPanel();
		description = new JLabel("Schedule an event to change the weather of a road after a given number of simulation ticks from now.");
		first.add(description);
		road = new JLabel("Road:");
		weather = new JLabel ("Weather:");
		ticks =  new JLabel("Ticks:");
		rModel = new DefaultComboBoxModel<>();
		rBox = new JComboBox <>(rModel);
		wModel = new DefaultComboBoxModel<>();
		wBox= new JComboBox <>();
		sticks= new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
		second.setLayout(new FlowLayout(FlowLayout.LEFT));
		second.add(road);
		second.add(rBox);
		second.add(weather);
		second.add(wBox);
		second.add(ticks);
		second.add(sticks);
		salir=new JButton("Cancel");
		okey=new JButton("OK");
		third.add(salir);
		third.add(okey);
		mainPanel.add(first);
		mainPanel.add(second);
		mainPanel.add(third);
		initFunctions();
	}
	
	private void initFunctions() {
		salir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status = 0;
				setVisible(false);
			}
		});
		
		okey.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				status = 1;
				setVisible(false);
			}
		});
	}
	
	public int open(List<Road> roads, List<Weather> weathers) {
		
		rModel.removeAllElements();
		for(Road r : roads) {
			rBox.addItem(r.getId());
		}
		
		wModel.removeAllElements();
		for(Weather w : weathers) {
			wBox.addItem(w);
		}
		
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);
		setVisible(true);
		return status;
	}
	
	
	public String getRoad() {
		return (String) rBox.getSelectedItem();
	}
	
	public Weather getWeather() {
		return (Weather) wBox.getSelectedItem();
	}
	
	
	public int getTicks() {
		return (int) sticks.getValue();
	}
	
	
}
