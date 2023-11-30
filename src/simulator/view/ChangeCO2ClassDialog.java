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

import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JPanel first;
	private JPanel second;
	private JPanel third;
	private JLabel description;
	private JLabel vehicle;
	private JLabel CO2;
	private JLabel ticks;
	private JComboBox <String> vBox;
	private DefaultComboBoxModel<String> vModel;
	private JComboBox <Integer> COBox;
	private DefaultComboBoxModel<Integer> cModel;
	private JSpinner sticks;
	private JButton salir;
	private JButton okey;
	private int status;
	
	public ChangeCO2ClassDialog(Frame parent) {
	super(parent, true);
	status = 0;
	mainPanel = new JPanel();
	this.setContentPane(mainPanel);
	this.setTitle("Change Co2 Class");
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
		description=new JLabel("Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now.");
		first.add(description);
		vehicle=new JLabel("Vehicle:");
		CO2=new JLabel ("CO2 Class:");
		ticks=new JLabel("Ticks:");
		vModel = new DefaultComboBoxModel<>();
		vBox=new JComboBox<>(vModel);
		
		cModel = new DefaultComboBoxModel<>();
		for (int i = 0; i < 11; i++)	cModel.addElement(i);
		COBox= new JComboBox<>(cModel);
		

		sticks= new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
		second.setLayout(new FlowLayout(FlowLayout.LEFT));
		second.add(vehicle);
		second.add(vBox);
		second.add(CO2);
		second.add(COBox);
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
	
	public int open(List<Vehicle > vehicles) {
		
		vModel.removeAllElements();
		for(int i = 0; i < vehicles.size(); i++) {
			vBox.addItem(vehicles.get(i).getId());
		}
		
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);
		setVisible(true);
		return status;
	}
	
	public String getVehicle() {
		return (String) vBox.getSelectedItem();
	}
	
	public int getCont() {
		return (int) COBox.getSelectedItem();
	}
	
	public int getTicks() {
		return (int) sticks.getValue();
	}
 	
}
