package gui;

import gui.PersonCreationPanel.MyComboBoxItem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import kelp.KelpClass;
import market.Item;
import market.gui.MarketBuilding;
import market.interfaces.Cashier;
import transportation.gui.TransportationGuiController;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import CommonSimpleClasses.sound.Sound;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.SingletonTimer;
import CommonSimpleClasses.XYPos;

public class ScenarioPanel extends JPanel implements ActionListener{ 

	JPanel imagePanel, eastPanel, populatePanel, normativePanel;
	Dimension d, eastDim, populateDim, normativeDim;

	
	//populate panel components
	ArrayList<MyComboBoxItem> buildList = new ArrayList<MyComboBoxItem>();
	MyComboBoxItem[] buildingArray;
	JComboBox<MyComboBoxItem> workplaceCB;
	JButton populateButton;
	
	//normative panel components
	JRadioButton scenarioA, scenarioB, scenarioC, scenarioJ; //TODO Add more scenarios here
	JButton runScenarioButton;
	JRadioButton scenarioE, scenarioF, scenarioG, scenarioO, scenarioP;
	JRadioButton scenarioQ, scenarioR, scenarioS;
	private PersonCreationPanel pcp;
	Timer timer = SingletonTimer.getInstance();
	JCheckBox gradingViewCB;
	JLabel msg;
	
	private Sound Testing = Sound.getInstance();
	
	public ScenarioPanel(PersonCreationPanel pcp) {
		this.pcp = pcp;
		d = new Dimension(Constants.ANIMATION_PANEL_WIDTH, Constants.ANIMATION_PANEL_HEIGHT-20);
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		setBackground(Color.white);
		setLayout(new BorderLayout());
		
		imagePanel = new JPanel();
		Dimension imageDim = new Dimension((int)(d.width*0.50),(int)(d.height));
		imagePanel.setPreferredSize(imageDim);
		imagePanel.setMaximumSize(imageDim);
		imagePanel.setMinimumSize(imageDim);
		imagePanel.setOpaque(false);
		
		eastPanel = new JPanel();
		eastDim = new Dimension((int)(d.width),(int)(d.height));
		eastPanel.setPreferredSize(eastDim);
		eastPanel.setMaximumSize(eastDim);
		eastPanel.setMinimumSize(eastDim);
		eastPanel.setOpaque(false);
		
		gradingViewCB = new JCheckBox("Grading View");
		gradingViewCB.addActionListener(this);
		gradingViewCB.setOpaque(false);
		
		JPanel north = new JPanel();
		north.setPreferredSize(new Dimension(d.width, (int)(d.height*0.2)));
		north.setLayout(new BorderLayout());
		north.setOpaque(false);
		
		populatePanel = new JPanel();
		normativePanel = new JPanel();
		makePopulatePanel();
		makeNormativePanel();
		
		north.add(populatePanel, BorderLayout.WEST);
		north.add(gradingViewCB, BorderLayout.EAST);
		
		//eastPanel.add(north, BorderLayout.NORTH);
		eastPanel.add(normativePanel, BorderLayout.SOUTH);
		
		//add(imagePanel, BorderLayout.WEST);
		//add(eastPanel, BorderLayout.EAST);
		//add(north, BorderLayout.NORTH);
		add(eastPanel, BorderLayout.CENTER);
	}

	private void makePopulatePanel() {
		populateDim = new Dimension((int)(d.width*0.7),(int)(eastDim.height*0.2));
		populatePanel.setPreferredSize(populateDim);
		populatePanel.setMaximumSize(populateDim);
		populatePanel.setMinimumSize(populateDim);
		populatePanel.setOpaque(false);
		
		Font font = new Font("Serif", Font.BOLD, 16);
		JLabel title = new JLabel("Populate Workplaces");
		title.setFont(font);
		
		//Middle panel
		JPanel middle = new JPanel();
		Dimension middleDim = new Dimension(populateDim.width, (int)(populateDim.height*0.3));
		middle.setPreferredSize(middleDim);
		middle.setLayout(new GridLayout());
		middle.setOpaque(false);
		
		JLabel workplaceLabel = new JLabel("Workplace: ", JLabel.CENTER);
		
		workplaceCB = new JComboBox<MyComboBoxItem>(); //TODO add array here
		workplaceCB.addActionListener(this);
		Dimension cbDim = new Dimension((int)(populateDim.width*0.8), (int)(middleDim.height));
		workplaceCB.setPreferredSize(cbDim);
		
		middle.add(workplaceLabel);
		middle.add(workplaceCB);
		
		//Populate Button
		populateButton = new JButton("Populate");
		populateButton.addActionListener(this);
		
		populatePanel.add(title);
		populatePanel.add(middle);
		populatePanel.add(populateButton);
	}

	private void makeNormativePanel() {
		normativeDim = new Dimension((int)(eastDim.width), (int)(eastDim.height*0.65));
		normativePanel.setPreferredSize(normativeDim);
		normativePanel.setMaximumSize(normativeDim);
		normativePanel.setMinimumSize(normativeDim);
		normativePanel.setOpaque(false);
		
		Font font = new Font("Serif", Font.BOLD, 16);
		JLabel title = new JLabel("Normative Scenarios", JLabel.CENTER);
		title.setFont(font);
		Dimension titleDim = new Dimension(normativeDim.width, (int)(normativeDim.height*0.15));
		title.setPreferredSize(titleDim);
		runScenarioButton = new JButton("Run Scenario");
		runScenarioButton.addActionListener(this);
		
		//RadioButtons
		scenarioA = new JRadioButton("Scenario A");
		scenarioB = new JRadioButton("Scenario B");
		scenarioC = new JRadioButton("Scenario C");
		scenarioE = new JRadioButton("Scenario E");
		scenarioF = new JRadioButton("Scenario F");
		scenarioG = new JRadioButton("Scenario G");
		scenarioJ = new JRadioButton("Scenario J");
		scenarioO = new JRadioButton("Scenario O");
		scenarioP = new JRadioButton("Scenario P");
		scenarioQ = new JRadioButton("Scenario Q");
		scenarioR = new JRadioButton("Scenario R");
		scenarioS = new JRadioButton("Scenario S");
		scenarioA.setOpaque(false);
		scenarioB.setOpaque(false);
		scenarioC.setOpaque(false);
		scenarioE.setOpaque(false);
		scenarioF.setOpaque(false);
		scenarioG.setOpaque(false);
		scenarioJ.setOpaque(false);
		scenarioO.setOpaque(false);
		scenarioP.setOpaque(false);
		scenarioQ.setOpaque(false);
		scenarioR.setOpaque(false);
		scenarioS.setOpaque(false);
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(scenarioA);
		buttonGroup.add(scenarioB);
		buttonGroup.add(scenarioC);
		buttonGroup.add(scenarioE);
		buttonGroup.add(scenarioF);
		buttonGroup.add(scenarioG);
		buttonGroup.add(scenarioJ);
		buttonGroup.add(scenarioO);
		buttonGroup.add(scenarioP);
		buttonGroup.add(scenarioQ);
		buttonGroup.add(scenarioR);
		buttonGroup.add(scenarioS);
		JPanel rButtons = new JPanel();
		rButtons.setPreferredSize(new Dimension(normativeDim.width, (int)(normativeDim.height*0.6)));
		rButtons.setLayout(new GridLayout(5,1,1,1));
		rButtons.setOpaque(false);
		rButtons.add(scenarioA);
		rButtons.add(scenarioB);
		rButtons.add(scenarioC);
		rButtons.add(scenarioE);
		rButtons.add(scenarioF);
		rButtons.add(scenarioG);
		rButtons.add(scenarioJ);
		rButtons.add(scenarioO);
		rButtons.add(scenarioP);
		rButtons.add(scenarioQ);
		rButtons.add(scenarioR);
		rButtons.add(scenarioS);
		
		msg = new JLabel("", JLabel.CENTER);
		msg.setPreferredSize(new Dimension(normativeDim.width-20, (int)(normativeDim.height*0.15)));

		normativePanel.add(title);
		normativePanel.add(rButtons);
		normativePanel.add(runScenarioButton);
		normativePanel.add(msg);
	}
	
	private void say(String str){
		msg.setText(str);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == populateButton){
			
			
		}
		
		// TODO Add delays to scenarios?
		if(e.getSource() == runScenarioButton){
			Testing.playSound("testing.wav");
			if (scenarioA.isSelected()) {
				employAllWorkplaces();
				createNonWorkingPersonThatVisitsEverywhere();
				
			} else if (scenarioB.isSelected()) {
				employAllWorkplaces();
				for (int i = 0; i < 3; i++)
					createNonWorkingPersonThatVisitsEverywhere();
				
			} else if (scenarioC.isSelected()) {
				employRestaurants();
				employMarkets();
				makeRestaurantsLowOnFood();
				triggerRestaurantsCooksSchedulers();
				
			} else if (scenarioE.isSelected()) {
				employAllWorkplaces();
				
			} else if (scenarioF.isSelected()) {
				employAllWorkplaces();
				createUnemployedUntil50People();
				explainScenarioF();
				
			} else if (scenarioG.isSelected()) {
				employMarkets();
				employRestaurantsWithoutCook();
				fakeRestaurantOrder();
				describeTheRestOfScenarioG();
				
			} else if (scenarioJ.isSelected()) {
				employAllWorkplaces();
				createUnemployedUntil50People();
				describeHowToHaveMoreTraffic();
				
			} else if (scenarioO.isSelected()) {
				employBanks();
				createBankRobber();
				
			} else if (scenarioP.isSelected()) {
				triggerCarAccident();
				
			} else if (scenarioQ.isSelected()) {
				triggerPedestrianAccident();
				
			} else if (scenarioR.isSelected()) {
				employAllWorkplaces();
				createUnemployedUntil50People();
				describeHowScenarioR();
				
			} else if (scenarioS.isSelected()) {
				employAllWorkplaces();
				createUnemployedUntil50People();
				describeHowScenarioS();
			}
			
			
			runScenarioButton.setEnabled(false);
		}
		
		if(e.getSource() == gradingViewCB){
			if(gradingViewCB.isSelected()){
				//TODO grading view
			}
			else{
				//TODO pretty view
			}
		}
		
	}

	private void describeHowToHaveMoreTraffic() {
		say("Create people with cars to add more traffic.");
		
	}

	private void describeTheRestOfScenarioG() {
		say("Add a cook to the restaurant to make it able to "
				+ "accept the delivery.");
		
	}

	private void explainScenarioF() {
		say("We run the normative scenario, people "
				+ "will naturally avoid closed venues.");
	}

	private void describeHowScenarioS() {
		say("Each building has a staff tab were you can hire and"
				+ "fire people.");
	}

	private void describeHowScenarioR() {
		say("Banks are closed on weekends");
	}
	
	private void employRestaurantsWithoutCook() {
		pcp.employRestaurantsWithoutCook();
	}

	private void triggerPedestrianAccident() {
		TransportationGuiController.getInstance().startPedestrianCrashSequence();
	}

	private void triggerCarAccident() {
		TransportationGuiController.getInstance().startCarCrashSequence();
	}

	private void createBankRobber() {
		say("Create a new person, go inside his info panel, and "
				+ "click the rob bank button.");
		
	}

	private void createUnemployedUntil50People() {
		pcp.createUnemployedUntil150People();
		
	}

	private void fakeRestaurantOrder() {
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				final MarketBuilding market = getOpenMarket();
				if (market != null) {
					this.cancel();
					market.interfaces.Cashier cashier 
						= (Cashier) market.getGreeter();
					List<CityLocation> restaurants = KelpClass.
							getKelpInstance().placesNearMe(market,
							LocationTypeEnum.Restaurant);
					RestaurantFakeOrderInterface restaurant =
							(RestaurantFakeOrderInterface) restaurants.get(0);
					List<Item> shoppingList = new ArrayList<Item>();
					shoppingList.add(new Item(Constants.FOODS.get(0), 1));
					cashier.msgPhoneOrder(shoppingList, restaurant.getCashier(),
							restaurant.getCook(), restaurant, 0);
				}
			}
		}, 0, 4000);
		
		
	}

	protected MarketBuilding getOpenMarket() {
		List<CityLocation> markets = 
				KelpClass.getKelpInstance().placesNearMe
					(new XYPos(0,0), LocationTypeEnum.Market);
		for (CityLocation cL : markets) {
			if (((MarketBuilding)(cL)).isOpen()) {
				return (MarketBuilding)cL;
			}
		}
		
		return null;
	}

	private void triggerRestaurantsCooksSchedulers() {
		List<CityLocation> restaurants = KelpClass.
				getKelpInstance().placesNearMe(new XYPos(0,0),
				LocationTypeEnum.Restaurant);
		for (CityLocation res : restaurants) {
			((RestaurantFakeOrderInterface)res).getCook().stateChanged();
		}
	}

	private void makeRestaurantsLowOnFood() {
		List<CityLocation> restaurants = 
				KelpClass.getKelpInstance().placesNearMe
				(new XYPos(0,0), LocationTypeEnum.Restaurant);
		
		for (CityLocation res : restaurants) {
			((RestaurantFakeOrderInterface)res).makeLowOnFood();
		}
	}

	private void employMarkets() {
		pcp.employMarkets();
	}

	private void employBanks() {
		pcp.employBanks();
	}

	private void employRestaurants() {
		pcp.employRestaurants();		
	}

	private void createNonWorkingPersonThatVisitsEverywhere() {
		pcp.createNonWorkingPersonThatVisitsEverywhere();
	}

	private void employAllWorkplaces() {
		pcp.populateAll();
	}


}
