package gui;

import gui.PersonCreationPanel.MyComboBoxItem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import kelp.KelpClass;
import market.Item;
import market.gui.MarketBuilding;
import market.interfaces.Cashier;

import com.sun.org.apache.bcel.internal.generic.Select;

import restaurant.anthony.gui.RestaurantBuilding;
import transportation.FakePassengerRole;
import transportation.gui.TransportationGuiController;
import classifieds.Classifieds;
import classifieds.ClassifiedsClass;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.SingletonTimer;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
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
		eastDim = new Dimension((int)(d.width*0.50),(int)(d.height));
		eastPanel.setPreferredSize(eastDim);
		eastPanel.setMaximumSize(eastDim);
		eastPanel.setMinimumSize(eastDim);
		eastPanel.setOpaque(false);
		
		populatePanel = new JPanel();
		normativePanel = new JPanel();
		makePopulatePanel();
		makeNormativePanel();
		
		eastPanel.add(populatePanel, BorderLayout.NORTH);
		eastPanel.add(normativePanel, BorderLayout.SOUTH);
		
		//add(imagePanel, BorderLayout.WEST);
		//add(eastPanel, BorderLayout.EAST);
		add(eastPanel, BorderLayout.CENTER);
	}

	private void makePopulatePanel() {
		populateDim = new Dimension((int)(eastDim.width),(int)(eastDim.height*0.2));
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
		middle.setLayout(new BorderLayout());
		middle.setOpaque(false);
		
		JLabel workplaceLabel = new JLabel("Workplace: ", JLabel.CENTER);
		
		workplaceCB = new JComboBox<MyComboBoxItem>(); //TODO add array here
		workplaceCB.addActionListener(this);
		Dimension cbDim = new Dimension((int)(populateDim.width*0.7), (int)(populateDim.height*.15));
		workplaceCB.setPreferredSize(cbDim);
		
		middle.add(workplaceLabel, BorderLayout.WEST);
		middle.add(workplaceCB, BorderLayout.EAST);
		
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
		rButtons.setPreferredSize(new Dimension(normativeDim.width, (int)(normativeDim.height*0.7)));
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

		normativePanel.add(title);
		normativePanel.add(rButtons);
		normativePanel.add(runScenarioButton);
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == populateButton){
			
			
		}
		
		// TODO Add delays to scenarios?
		if(e.getSource() == runScenarioButton){
			if (scenarioA.isEnabled()) {
				employAllWorkplaces();
				startDay();
				createNonWorkingPersonThatVisitsEverywhere();
				
			} else if (scenarioB.isEnabled()) {
				employAllWorkplaces();
				startDay();
				for (int i = 0; i < 3; i++)
					createNonWorkingPersonThatVisitsEverywhere();
				
			} else if (scenarioC.isEnabled()) {
				employRestaurants();
				employMarkets();
				startDay();
				makeRestaurantsLowOnFood();
				triggerRestaurantsCooksSchedulers();
				
			} else if (scenarioE.isEnabled()) {
				employAllWorkplaces();
				
			} else if (scenarioF.isEnabled()) {
				// TODO fill this
				
			} else if (scenarioG.isEnabled()) {
				employMarkets();
				employRestaurantsWithoutCook();
				startDay();
				fakeRestaurantOrder();
				describeTheRestOfScenarioG();
				
			} else if (scenarioJ.isEnabled()) {
				employAllWorkplaces();
				createUnemployedUntil50People();
				dealWithHavingMoreTraffic();
				
			} else if (scenarioO.isEnabled()) {
				employMarkets();
				createBankRobber();
				
			} else if (scenarioP.isEnabled()) {
				triggerCarAccident();
				
			} else if (scenarioQ.isEnabled()) {
				triggerPedestrianAccident();
				
			} else if (scenarioR.isEnabled()) {
				employAllWorkplaces();
				createUnemployedUntil50People();
				describeHowScenarioR();
				
			} else if (scenarioS.isEnabled()) {
				employAllWorkplaces();
				createUnemployedUntil50People();
				describeHowScenarioS();
			}
			
			
			runScenarioButton.setEnabled(false);
		}
		
	}

private void describeTheRestOfScenarioG() {
		// TODO Auto-generated method stub
		
	}

	private void describeHowScenarioS() {
		// TODO Auto-generated method stub
		
	}

	private void describeHowScenarioR() {
		// TODO Auto-generated method stub
		
	}
	
	private void employRestaurantsWithoutCook() {
		// TODO Auto-generated method stub
		
	}

	private void triggerPedestrianAccident() {
		TransportationGuiController.getInstance().startPedestrianCrashSequence();
	}

	private void triggerCarAccident() {
		TransportationGuiController.getInstance().startCarCrashSequence();
	}

	private void createBankRobber() {
		// TODO Auto-generated method stub
		
	}

	private void dealWithHavingMoreTraffic() {
		// TODO Auto-generated method stub
		
	}

	private void createUnemployedUntil50People() {
		pcp.createUnemployedUntil50People();
		
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
		// TODO Auto-generated method stub
	}

	private void employMarkets() {
		pcp.employMarkets();
	}

	private void employRestaurants() {
		pcp.employRestaurants();		
	}

	private void createNonWorkingPersonThatVisitsEverywhere() {
		pcp.createNonWorkingPersonThatVisitsEverywhere();
	}

	private void startDay() {
		// TODO Auto-generated method stub
		
	}

	private void employAllWorkplaces() {
		pcp.populateAll();
	}


}
