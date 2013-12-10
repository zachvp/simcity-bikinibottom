package gui;

import housing.interfaces.Dwelling;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.junit.Ignore;

import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.Constants;
import agent.WorkRole;
import agent.PersonAgent.HungerLevel;
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
	
	int nameIndex=0;
	
	class MyComboBoxItem {
		public Object object;
		private String blankString;
		public MyComboBoxItem(Object object, String blankString) {
			this.object = object;
			this.blankString = blankString;
		}
		public String toString() {
			if (object != null) return object.toString();
			else return blankString;
		}
	}

	private CitizenRecords citizenRecords;
	Classifieds classifieds = ClassifiedsClass.getClassifiedsInstance();

	JTextField nameTextF;
	JComboBox<MyComboBoxItem> buildingsCB;
	JComboBox<MyComboBoxItem> occupationsCB;
	JComboBox<MyComboBoxItem> residencesCB;
	JComboBox<String> wealthCB;
	JComboBox<String> carCB;
	JComboBox<String> hungerCB;
	JComboBox<String> restaurantCB;
	JComboBox<String> foodCB;
	JButton createButton;
	ArrayList<MyComboBoxItem> buildList = new ArrayList<MyComboBoxItem>();
	MyComboBoxItem[] buildingArray;
	ArrayList<MyComboBoxItem> occList = new ArrayList<MyComboBoxItem>();
	MyComboBoxItem[] occupationArray;
	ArrayList<MyComboBoxItem> resList = new ArrayList<MyComboBoxItem>();
	MyComboBoxItem[] residentArray;

	JLabel msg;
	private JButton populateButton;
	private BufferedImage image;

	public PersonCreationPanel(){
		Dimension d = new Dimension(Constants.ANIMATION_PANEL_WIDTH, Constants.ANIMATION_PANEL_HEIGHT-20);

		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		setBackground(Color.white);
		setLayout(new BorderLayout());

		//JPanel half = new JPanel();
		//Dimension halfDim = new Dimension((int)(d.width*0.6), d.height);
		
		
		
		JPanel welcomeTextPanel = new JPanel();
		JLabel welcomeText = new JLabel("Welcome to the Hospital!");	//north
		Dimension textDim = new Dimension(d.width, (int)(d.height*0.10));
		welcomeTextPanel.setPreferredSize(textDim);
		welcomeTextPanel.setMaximumSize(textDim);
		welcomeTextPanel.setMinimumSize(textDim);
		welcomeTextPanel.setOpaque(false);
		Font font = new Font("Serif", Font.BOLD, 20);

		welcomeText.setFont(font);
		welcomeTextPanel.add(welcomeText);

		//TODO test
		//		WorkRole test = new TellerRole(null, null);
		
		//Input Panel
		JPanel inputPanel = new JPanel();
		Dimension inputDim = new Dimension((int)(d.width*0.6), (int)(d.height*0.9));
		inputPanel.setPreferredSize(inputDim);
		inputPanel.setMaximumSize(inputDim);
		inputPanel.setMinimumSize(inputDim);
		inputPanel.setLayout(new BorderLayout());
		//inputPanel.setBackground(Color.white);
		inputPanel.setOpaque(false);
		
		JPanel inputPanelLeft = new JPanel();
		Dimension inputDimL = new Dimension((int)(d.width*0.14), (int)(d.height*0.6));
		inputPanelLeft.setPreferredSize(inputDimL);
		inputPanelLeft.setMaximumSize(inputDimL);
		inputPanelLeft.setMinimumSize(inputDimL);
		inputPanelLeft.setLayout(new GridLayout(9,1,5,5));
		//inputPanel.setBackground(Color.white);
		inputPanelLeft.setOpaque(false);
		
		JPanel inputPanelright = new JPanel();
		Dimension inputDimR = new Dimension((int)(d.width*0.46), (int)(d.height*0.4));
		inputPanelright.setPreferredSize(inputDimR);
		inputPanelright.setMaximumSize(inputDimR);
		inputPanelright.setMinimumSize(inputDimR);
		inputPanelright.setLayout(new GridLayout(9,1,5,5));
		//inputPanel.setBackground(Color.white);
		inputPanelright.setOpaque(false);
		

		nameTextF = new JTextField("Enter a name");
		checkSomewhereforBuildings();
		buildingsCB = new JComboBox<MyComboBoxItem>(buildingArray);
		buildingsCB.addActionListener(this);
		checkClassifiedsforJobs();
		occupationsCB = new JComboBox<MyComboBoxItem>(occupationArray);
		checkClassifiedsforHome();
		residencesCB = new JComboBox<MyComboBoxItem>(residentArray);
		wealthCB = new JComboBox<String>(new String[] {"Rich", "Middle", "Poor"});
		carCB = new JComboBox<String>(new String[] {"Yes", "No"});
		restaurantCB = new JComboBox<String>(new String[] 
				{"Willing to go", "Not willing to go"});
		hungerCB = new JComboBox<String>(new String[] {"Starving", "Hungry",
						"Neutral", "Satisfied", "Full"});
		foodCB = new JComboBox<String>(new String[] {"Has food at home",
				"Doesn't have food at home"});

		nameTextF.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				nameTextF.setText(null);
			}
		});
		
		// default values for person creation
		if(nameIndex> Constants.NAMES.size()){
			nameIndex = 0;
		}
		nameTextF.setText(Constants.NAMES.get(nameIndex));
		nameIndex++;
		buildingsCB.setSelectedIndex(0);
		occupationsCB.setSelectedIndex(0);
		if(resList.size() > 1) residencesCB.setSelectedIndex(1);
		else residencesCB.setSelectedIndex(0);
		wealthCB.setSelectedIndex(1);
		carCB.setSelectedIndex(0);
		hungerCB.setSelectedIndex(4);
		restaurantCB.setSelectedIndex(0);
		foodCB.setSelectedIndex(0);

		inputPanelLeft.add(new JLabel("Name: "));
		inputPanelright.add(nameTextF);
		inputPanelLeft.add(new JLabel("Workplace: "));
		inputPanelright.add(buildingsCB);
		inputPanelLeft.add(new JLabel("Job: "));
		inputPanelright.add(occupationsCB);
		inputPanelLeft.add(new JLabel("Residence: "));
		inputPanelright.add(residencesCB);
		inputPanelLeft.add(new JLabel("Status: "));
		inputPanelright.add(wealthCB);
		inputPanelLeft.add(new JLabel("Hunger: "));
		inputPanelright.add(hungerCB);
		inputPanelLeft.add(new JLabel("Restaurant: "));
		inputPanelright.add(restaurantCB);
		inputPanelLeft.add(new JLabel("Food: "));
		inputPanelright.add(foodCB);
		inputPanelLeft.add(new JLabel("Car: "));
		inputPanelright.add(carCB);
		
		inputPanel.add(inputPanelLeft, BorderLayout.WEST);
		inputPanel.add(inputPanelright, BorderLayout.EAST);
		
		//Buttons and msg
		JPanel south  = new JPanel();
		Dimension sDim = new Dimension((int)(d.width*0.6), (int)(d.height*0.3));
		south.setPreferredSize(sDim);
		south.setMaximumSize(sDim);
		south.setMinimumSize(sDim);
		//south.setLayout(new GridLayout(5,2,5,5));
		//south.setBackground(Color.white);
		south.setOpaque(false);
		
		createButton = new JButton("Create");
		createButton.addActionListener(this);
		
		populateButton = new JButton("Populate selected workplace");
		populateButton.addActionListener(this);

		ClassifiedsClass.getClassifiedsInstance().addListener(this);

		msg = new JLabel("");
		Dimension msgDim = new Dimension((int)(d.width*0.6), (int)(d.height*0.05));
		msg.setPreferredSize(msgDim);
		
		south.add(createButton);
		south.add(populateButton);
		south.add(msg);
		
		inputPanel.add(south, BorderLayout.SOUTH);
		
		//Image Panel 
		try {
			image = ImageIO.read(getClass().getResource("doctor.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}		
		JPanel imagePanel = new JPanel();
		JLabel imageLabel = new JLabel(new ImageIcon(image));
		Dimension iDim = new Dimension((int)(d.width*0.4), (int)(d.height*0.7));
		imagePanel.setPreferredSize(iDim);
		imagePanel.setMaximumSize(iDim);
		imagePanel.setMinimumSize(iDim);
		imagePanel.setLayout(new BorderLayout());
		imagePanel.setOpaque(false);
		imagePanel.add(imageLabel, BorderLayout.CENTER);
		
		
		add(welcomeTextPanel, BorderLayout.NORTH);
		add(inputPanel, BorderLayout.EAST);
		add(imagePanel, BorderLayout.WEST);
		//add(south, BorderLayout.SOUTH);
		
		carCB.setSelectedIndex(1);
		hungerCB.setSelectedIndex(4);
		restaurantCB.setSelectedIndex(0);
		foodCB.setSelectedIndex(0);
		classifiedsUpdated();
		checkSomewhereforBuildings();
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == createButton){
			
			if (((MyComboBoxItem)residencesCB.getSelectedItem())
					.object != null) {
				MyComboBoxItem jobItem = (MyComboBoxItem) (occupationsCB
						.getSelectedItem());
				MyComboBoxItem homeItem = (MyComboBoxItem) (residencesCB
						.getSelectedItem());
				String name = nameTextF.getText();
				WorkRole job = (WorkRole) (jobItem.object);
				Dwelling home = (Dwelling) (homeItem.object);
				String status = (String) wealthCB.getSelectedItem();
				String hungerLevel = (String) hungerCB.getSelectedItem();
				String restaurant = (String) restaurantCB.getSelectedItem();
				String foodAtHome = (String) foodCB.getSelectedItem();
				boolean hasCar = ((String) carCB.getSelectedItem())
						.equals("Yes");
				if (!incompleteInputs(name, status,
						(String) carCB.getSelectedItem())) {
					//reset input fields
					nameTextF.setText("");
					occupationsCB.setSelectedIndex(0);
					if(resList.size() > 1) residencesCB.setSelectedIndex(1);
					else residencesCB.setSelectedIndex(0);					
					wealthCB.setSelectedIndex(1);
					carCB.setSelectedIndex(1);
					restaurantCB.setSelectedIndex(0);
					hungerCB.setSelectedIndex(4);
					foodCB.setSelectedIndex(0);

					//create new PersonAgent
					//PersonAgent newPerson = new PersonAgent(name);
					//TODO add all person info
					citizenRecords.addCitizen(name, job, home, status, hasCar,
							hungerLevel, restaurant, foodAtHome);
					if (Constants.PRINT)
						System.out.println(name
								+ " has been added to your city!");

					//TODO pull up newPerson's infopanel
					msg.setText(name + " was created.");

					// default values for person creation
					if(nameIndex>= Constants.NAMES.size()){
						nameIndex = 0;
					}
					nameTextF.setText(Constants.NAMES.get(nameIndex));
					nameIndex++;

					checkClassifiedsforJobs();
					checkClassifiedsforHome();
					
					occupationsCB.setSelectedIndex(0);
					if(resList.size() > 1) residencesCB.setSelectedIndex(1);
					else residencesCB.setSelectedIndex(0);
					wealthCB.setSelectedIndex(1);
					carCB.setSelectedIndex(1);
					restaurantCB.setSelectedIndex(0);
					hungerCB.setSelectedIndex(4);
					foodCB.setSelectedIndex(0);
				} else {
					msg.setText("Please complete all inputs");
				}
			} else {
				msg.setText("Can't create a person without a house");
			}
		} else if (e.getSource() == populateButton){
			// TODO check for residences too!
			if (resList.size() < occList.size()) {
				msg.setText("Not enough housing for all required workers.");
				return;
			}
			if(nameTextF.getText()==null || nameTextF.getText().equals("")){
				msg.setText("Please complete all inputs");
				return;

			}
			
			while(occList.size() > 1) {
				occupationsCB.setSelectedIndex(1);
				residencesCB.setSelectedIndex(1);
				actionPerformed(new ActionEvent(createButton, 0, ""));
				checkClassifiedsforJobs();
				checkClassifiedsforHome();
				
			}
		} else if (e.getSource() == buildingsCB) {
			checkClassifiedsforJobs();
			checkClassifiedsforHome();
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
		checkSomewhereforBuildings();
		checkClassifiedsforJobs();
		checkClassifiedsforHome();
	}

	//TODO needs testing
	private void checkClassifiedsforJobs(){
		occList.clear();
		occList.add(new MyComboBoxItem(null,"None"));

		ArrayList<WorkRole> newJobs = new ArrayList<WorkRole>();
		MyComboBoxItem comboBoxItem = (MyComboBoxItem)buildingsCB.getSelectedItem();
		CityBuilding building = null;
		if (comboBoxItem != null) building = (CityBuilding) comboBoxItem.object;
		newJobs.addAll((ArrayList<WorkRole>) classifieds.getJobsForBuilding(building
				, true));

		for(WorkRole w: newJobs){
			occList.add(new MyComboBoxItem(w,"None"));
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
		resList.add(new MyComboBoxItem(null,"None"));

		ArrayList<Dwelling> newHomes = new ArrayList<Dwelling>();
		newHomes.addAll((ArrayList<Dwelling>) classifieds.getRooms(true));

		for(Dwelling w: newHomes){
			resList.add(new MyComboBoxItem(w,"None")); //TODO check if name is correct
		}
		residentArray = resList.toArray(new MyComboBoxItem[newHomes.size()]);
		
		if(residencesCB != null){
			residencesCB.removeAllItems();
			for (MyComboBoxItem c: residentArray){
				residencesCB.addItem(c);
			}
		}
	}
	
	private void checkSomewhereforBuildings() {
		buildList.clear();
		buildList.add(new MyComboBoxItem(null,"All"));
		
		ArrayList<CityLocation> newBuildings = new ArrayList<CityLocation>();
		newBuildings.addAll(classifieds.getWorkplaces());
		
		for(CityLocation l: newBuildings){
			buildList.add(new MyComboBoxItem(l,"All")); //TODO check if name is correct
		}
		buildingArray = buildList.toArray(new MyComboBoxItem[newBuildings.size()]);
		
		if(buildingsCB != null){
			buildingsCB.removeAllItems();
			for (MyComboBoxItem c: buildingArray){
				buildingsCB.addItem(c);
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

	private boolean incompleteInputs(String name,
			String wealth, String car) {
		return (name == null) ||(name.equals("Enter a name")) ||(name.equals(""))
				|| ((wealth.equals("Select a Status"))
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
