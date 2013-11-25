package gui;

import housing.interfaces.Dwelling;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import agent.WorkRole;
import classifieds.Classifieds;
import classifieds.ClassifiedsChangedListener;
import classifieds.ClassifiedsClass;

/**
 * Panel for creating PersonAgents from UI
 * Disguised as a "Hospital" Building
 * @author Victoria Dea
 *
 */
public class PersonCreationPanel extends JPanel implements ActionListener,
ClassifiedsChangedListener{
	
	class MyComboBoxItem {
		public Object object;
		public MyComboBoxItem(Object object) {
			this.object = object;
		}
		public String toString() {
			if (object != null) return object.toString();
			else return "None";
		}
	}

	private CitizenRecords citizenRecords;
	Classifieds classifieds = ClassifiedsClass.getClassifiedsInstance();

	JTextField nameTextF;
	JComboBox<MyComboBoxItem> occupationsCB;
	JComboBox<String> residencesCB;
	JComboBox<String> wealthCB;
	JComboBox<String> carCB;
	JButton createButton;
	ArrayList<MyComboBoxItem> occList = new ArrayList<MyComboBoxItem>();
	MyComboBoxItem[] occupationArray;
	ArrayList<String> resList = new ArrayList<String>();
	String[] residentArray;

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

		//TODO test
		//		WorkRole test = new TellerRole(null, null);

		JPanel inputPanel = new JPanel();
		Dimension inputDim = new Dimension(d.width, (int)(d.height*0.3));
		inputPanel.setPreferredSize(inputDim);
		inputPanel.setMaximumSize(inputDim);
		inputPanel.setMinimumSize(inputDim);
		inputPanel.setLayout(new GridLayout(5,2,5,5));
		inputPanel.setBackground(Color.white);

		nameTextF = new JTextField("Enter a name");		
		checkClassifiedsforJobs();
		occupationsCB = new JComboBox<MyComboBoxItem>(occupationArray);
		checkClassifiedsforHome();
		residencesCB = new JComboBox<String>(residentArray);
		wealthCB = new JComboBox<String>(new String[] {"Select a Status", "Rich", "Middle", "Poor"});
		carCB = new JComboBox<String>(new String[] {"Has a Car", "Yes", "No"});

		nameTextF.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				nameTextF.setText(null);
			}
		});
		
		// default values for person creation
		nameTextF.setText("mr balloon hands");
		occupationsCB.setSelectedIndex(0);
		residencesCB.setSelectedIndex(1);
		wealthCB.setSelectedIndex(1);
		carCB.setSelectedIndex(2);

		inputPanel.add(new JLabel("Name: "));
		inputPanel.add(nameTextF);
		inputPanel.add(new JLabel("Occupation: "));
		inputPanel.add(occupationsCB);
		inputPanel.add(new JLabel("Residence: "));
		inputPanel.add(residencesCB);
		inputPanel.add(new JLabel("Status: "));
		inputPanel.add(wealthCB);
		inputPanel.add(new JLabel("Car: "));
		inputPanel.add(carCB);

		createButton = new JButton("Create");
		createButton.addActionListener(this);

		ClassifiedsClass.getClassifiedsInstance().addListener(this);

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
			MyComboBoxItem jobItem = (MyComboBoxItem)(occupationsCB.
					getSelectedItem());
			
			String name = nameTextF.getText();
			WorkRole job = (WorkRole)(jobItem.object);
			String home = (String)residencesCB.getSelectedItem();
			String status = (String)wealthCB.getSelectedItem();
			boolean hasCar = ((String)carCB.getSelectedItem()).equals("Yes");

			if(!incompleteInputs(name, home, status, (String)carCB.getSelectedItem())){
				//reset input fields
				nameTextF.setText("");
				occupationsCB.setSelectedIndex(0);
				residencesCB.setSelectedIndex(0);
				wealthCB.setSelectedIndex(0);
				carCB.setSelectedIndex(0);

				//create new PersonAgent
				//PersonAgent newPerson = new PersonAgent(name);
				//TODO add all person info
				citizenRecords.addCitizen(name, job, home, status, hasCar);
				System.out.println(name +" has been added to your city!");

				//TODO pull up newPerson's infopanel
				msg.setText(name + " was created.");
				
				// default values for person creation
				nameTextF.setText("mr balloon hands");
				try {
					occupationsCB.setSelectedIndex(1);
				} catch (IllegalArgumentException except) {
					// oh well
					// except.printStackTrace();
				}
				try {
					residencesCB.setSelectedIndex(1);
				} catch (IllegalArgumentException except) {
					// oh well
					// except.printStackTrace();
				}
				wealthCB.setSelectedIndex(1);
				carCB.setSelectedIndex(2);
			}
			else{
				msg.setText("Please complete all inputs");
			}			
		}
		//		
		//		if (e.getSource() == occupationsCB) {
		//			checkClassifiedsforJobs();
		//		}
		//		if (e.getSource() == residencesCB) {
		//			checkClassifiedsforHome();
		//		}
	}

	public void classifiedsUpdated() {
		checkClassifiedsforJobs();
		checkClassifiedsforHome();
	}

	//TODO needs testing
	private void checkClassifiedsforJobs(){
		occList.clear();
		occList.add(new MyComboBoxItem(null));

		ArrayList<WorkRole> newJobs = new ArrayList<WorkRole>();
		newJobs.addAll((ArrayList<WorkRole>) classifieds.getJobsForBuilding(null, true));

		for(WorkRole w: newJobs){
			occList.add(new MyComboBoxItem(w));
		}
		occupationArray = occList.toArray(new MyComboBoxItem[newJobs.size()]);

		if(occupationsCB != null){
			occupationsCB.removeAllItems();
			for (MyComboBoxItem c: occupationArray){
				occupationsCB.addItem(c);

			}
		}
	}

	//TODO needs testing
	private void checkClassifiedsforHome(){
		resList.clear();
		resList.add("Select a Residence");
		resList.add("None");

		ArrayList<Dwelling> newHomes = new ArrayList<Dwelling>();
		newHomes.addAll((ArrayList<Dwelling>) classifieds.getRooms(true));

		for(Dwelling w: newHomes){
			resList.add(w.toString()); //TODO check if name is correct
		}
		residentArray = resList.toArray(new String[newHomes.size()]);
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

	private boolean incompleteInputs(String name, String home,
			String wealth, String car) {
		return (name == null) ||(name.equals("Enter a name")) ||(name.equals(""))
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
