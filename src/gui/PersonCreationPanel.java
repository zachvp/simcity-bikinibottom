package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import agent.PersonAgent;

/**
 * Panel for creating PersonAgents from UI
 * Disguised as a "Hospital" Building
 * @author Victoria Dea
 *
 */
public class PersonCreationPanel extends JPanel implements ActionListener{
	
	private CitizenRecords citizenRecords;

	
	JTextField nameText;
	JComboBox<String> occupations;
	JComboBox<String> residences;
	JComboBox<String> wealth;
	JComboBox<String> car;
	JButton createButton;
	
	JLabel msg;

	public PersonCreationPanel(){
		Dimension d = new Dimension(600, 490);
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		setBackground(Color.white);
		//setLayout(new BorderLayout());
		
		JLabel welcomeText = new JLabel("Welcome to the Hospital!");
		Dimension textDim = new Dimension(d.width, (int)(d.height*0.2));
		welcomeText.setPreferredSize(textDim);
		welcomeText.setMaximumSize(textDim);
		welcomeText.setMinimumSize(textDim);
		Font font = new Font("Serif", Font.BOLD, 20);
		
		welcomeText.setFont(font);
		
		JPanel inputPanel = new JPanel();
		Dimension inputDim = new Dimension(d.width, (int)(d.height*0.3));
		inputPanel.setPreferredSize(inputDim);
		inputPanel.setMaximumSize(inputDim);
		inputPanel.setMinimumSize(inputDim);
		inputPanel.setLayout(new GridLayout(5,2,5,5));
		inputPanel.setBackground(Color.white);
		
		nameText = new JTextField("Enter a name");
		occupations = new JComboBox<String>(new String[] {"Select an Occupation", "None"});
		residences = new JComboBox<String>(new String[] {"Select a Residence", "None"});
		wealth = new JComboBox<String>(new String[] {"Select a Status", "Rich", "Middle", "Poor"});
		car = new JComboBox<String>(new String[] {"Has a Car", "Yes", "No"});

		nameText.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				nameText.setText(null);
			}
		});

		inputPanel.add(new JLabel("Name: "));
		inputPanel.add(nameText);
		inputPanel.add(new JLabel("Occupation: "));
		inputPanel.add(occupations);
		inputPanel.add(new JLabel("Residence: "));
		inputPanel.add(residences);
		inputPanel.add(new JLabel("Status: "));
		inputPanel.add(wealth);
		inputPanel.add(new JLabel("Car: "));
		inputPanel.add(car);
		
		createButton = new JButton("Create");
		createButton.addActionListener(this);
		
		msg = new JLabel("");
		Dimension msgDim = new Dimension(d.width, (int)(d.height*0.2));
		msg.setPreferredSize(msgDim);
		msg.setMaximumSize(msgDim);
		msg.setMinimumSize(msgDim);
		
		add(welcomeText);
		add(inputPanel);
		add(createButton);
		add(msg);
		
	}

	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == createButton){
			String name = nameText.getText();
			String job = (String)occupations.getSelectedItem();
			String home = (String)residences.getSelectedItem();
			String status = (String)wealth.getSelectedItem();
			boolean hasCar = ((String)car.getSelectedItem()).equals("Yes");
			
			if(!incompleteInputs(name, job, home, status, (String)car.getSelectedItem())){
											
				//reset input fields
				nameText.setText("");
				occupations.setSelectedIndex(0);
				residences.setSelectedIndex(0);
				wealth.setSelectedIndex(0);
				car.setSelectedIndex(0);
				
				//create new PersonAgent
				//PersonAgent newPerson = new PersonAgent(name);
				//TODO add all person info
				citizenRecords.addCitizen(name, job, home, status, hasCar);
				System.out.println(name +" has been added to your city!");
				
				//TODO pull up newPerson's infopanel
				msg.setText(name + " was created");
			}
			else{
				msg.setText("Please complete all inputs");
			}
		}
		
	}
	
	/**
	 * Verifies the User Inputs when creating a new Person
	 * @param name 
	 * @param job
	 * @param home
	 * @param wealthStatus
	 * @param car
	 * @return True if any input is incomplete
	 */

	private boolean incompleteInputs(String name, String job, String home,
			String wealthStatus, String car) {
		return (name == null) ||(name.equals("Enter a name")) ||(name.equals(""))
				|| (job.equals("Select an Occupation")) 
				|| (home.equals("Select a Residence") 
				|| (wealth.equals("Select a Status"))
				|| (car.equals("Has a Car")));
		
	}
	/**
	 * Gives hospital reference to the CitizenRecords
	 * @param records the CitizenRecords
	 */
	public void setRecords(CitizenRecords records) {
		citizenRecords = records;		
	}
	
	
}
