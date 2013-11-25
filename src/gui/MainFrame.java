package gui;


import gui.test.MockBuilding;
import housing.ResidentialBuilding;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import market.gui.MarketBuilding;
import bank.gui.BankBuilding;
import agent.Constants;
import kelp.KelpClass;
import parser.BuildingDef;
import parser.BuildingPosParser;
import parser.CornersWithBusstopsParser;
import sun.net.www.content.text.PlainTextInputStream;
import transportation.interfaces.*;
import transportation.mapbuilder.MapBuilder;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;


/** 
 * Main GUI class
 * Contains the main frame and structure for panels
 * @author Victoria Dea
 *
 */
public class MainFrame extends JFrame implements ActionListener {


	//TODO  write down steps to test GUI manually 


	private int WINDOWX = 1200;
	private int WINDOWY = 700;

	private BufferedInputStream stream;
	private List<BuildingDef> needToBuild;

	//Panel Slots
	private JPanel cityViewSlot = new JPanel();
	private JPanel buildingViewSlot = new JPanel();
	private JPanel infoPanelSlot = new JPanel();
	private JPanel InfoListSlot        = new JPanel();

	//Panels
	private BuildingView buildingViewPanel;
	private CityView cityViewPanel;
	private CityMap map;
	public InfoPanel infoPanel;	

	private InfoList buildingList;
	private InfoList personList;
	private CitizenRecords citizenRecords;
	private List<Building> constructedBuildings = new ArrayList<Building>();
	private Semaphore semaphore = new Semaphore(0);
	private Timer timer = new Timer();

	//TODO Add timer here?

	public MainFrame(){

		//FullScreen frame
		//Toolkit tk = Toolkit.getDefaultToolkit();  
		//WINDOWX = ((int) tk.getScreenSize().getWidth());  
		//WINDOWY = ((int) tk.getScreenSize().getHeight()); 
		//setExtendedState(Frame.MAXIMIZED_BOTH);

		setBounds(50,50, WINDOWX, WINDOWY);

		try {
			setContentPane(new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("sky_background.png")))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setLayout(new BorderLayout(5,10));

		try {
			stream = (BufferedInputStream)getClass().getResource("BuildingConfig.csv").getContent();
			needToBuild = BuildingPosParser.parseBuildingPos(stream);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//Internal Building View
		Dimension buildingDim = new Dimension((int)(WINDOWX * .5), (int) (WINDOWY * .7));
		buildingViewSlot.setPreferredSize(buildingDim);
		buildingViewSlot.setMaximumSize(buildingDim);
		buildingViewSlot.setMinimumSize(buildingDim);
		buildingViewSlot.setBorder(BorderFactory.createTitledBorder("Building View"));
		buildingViewSlot.setOpaque(false);
		buildingViewPanel = new BuildingView(buildingDim.width, buildingDim.height);
		buildingViewSlot.add(buildingViewPanel);

		//City map view
		Dimension cityDim = new Dimension((int)(WINDOWX * .5), (int) (WINDOWY * .7));
		cityViewSlot.setPreferredSize(cityDim);
		cityViewSlot.setMaximumSize(cityDim);
		cityViewSlot.setMinimumSize(cityDim);
		cityViewSlot.setOpaque(false);
		cityViewSlot.setBorder(BorderFactory.createTitledBorder("City View"));
		map = new CityMap();
		cityViewPanel = new CityView(cityDim.width, cityDim.height, this, map);

		map.setBuildingView(buildingViewPanel);
		cityViewSlot.add(cityViewPanel);

		//Information Panel
		Dimension infoDim = new Dimension((int)(WINDOWX * .6), (int) (WINDOWY * .3));
		infoPanelSlot.setPreferredSize(infoDim);
		infoPanelSlot.setMaximumSize(infoDim);
		infoPanelSlot.setMinimumSize(infoDim);
		infoPanelSlot.setBorder(BorderFactory.createTitledBorder("Information Panel"));
		infoPanelSlot.setOpaque(false);
		infoPanel = new InfoPanel(infoDim.width, infoDim.height);        
		infoPanelSlot.add(infoPanel);

		//List of Buildings/People buttons
		Dimension listDim = new Dimension((int)(WINDOWX * .4), (int) (WINDOWY * .3));
		InfoListSlot.setPreferredSize(listDim);
		InfoListSlot.setMaximumSize(listDim);
		InfoListSlot.setMinimumSize(listDim);
		InfoListSlot.setOpaque(false);

		//Lists displaying People and Buildings in the city
		JTabbedPane tabbedPane = new JTabbedPane();
		buildingList = new InfoList(listDim.width, listDim.height);
		personList = new InfoList(listDim.width, listDim.height);
		buildingList.setOtherTab(personList);
		personList.setOtherTab(buildingList);
		buildingList.setInfoPanel(infoPanel);
		personList.setInfoPanel(infoPanel);
		citizenRecords = new CitizenRecords(this);
		personList.setCitizenRecords(citizenRecords);
		buildingList.setBuildingList(map.getBuildings());
		citizenRecords.setInfoPanel(infoPanel);
		buildingList.setBuildingView(buildingViewPanel);
		tabbedPane.addTab("Buildings", buildingList);
		tabbedPane.addTab("People", personList);
		InfoListSlot.add(tabbedPane);
		buildingViewPanel.setBuildingList(buildingList);
		map.setInfoPanel(infoPanel);

		//JPanel to hold infoPanelSlot and buildingListSlot
		JPanel infoHolder = new JPanel();
		Dimension infoHolderDim = new Dimension(WINDOWX, (int) (WINDOWY * .3));
		infoHolder.setPreferredSize(infoHolderDim);
		infoHolder.setMaximumSize(infoHolderDim);
		infoHolder.setMinimumSize(infoHolderDim);
		infoHolder.setOpaque(false);
		infoHolder.setLayout(new BorderLayout(5,10));
		infoHolder.add(infoPanelSlot, BorderLayout.WEST);
		infoHolder.add(InfoListSlot, BorderLayout.EAST);

		add(buildingViewSlot, BorderLayout.EAST);
		add(cityViewSlot, BorderLayout.WEST);
		add(infoHolder, BorderLayout.SOUTH);        

		//Constructs buildings from config file
		constructCity(needToBuild);
	}

	/**
	 * Builds the entire city
	 * @param list A list of Buildings defined in the config file
	 */
	private void constructCity(List<BuildingDef> list) {
		int x;
		int y;
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				semaphore.release();
				
			}
		}, 8000);
		
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for(BuildingDef b: list){
			String buildingName = b.getName();
			LocationTypeEnum type = b.getType();

			x = map.getNextBuildingX();
			y = map.getNextBuildingY();

			if(type == LocationTypeEnum.Bank){
				BankBuilding bank = new BankBuilding(x, y, Constants.BUILDING_WIDTH, Constants.BUILDING_HEIGHT); //Rectangle
				bank.setName(buildingName);
				construct(bank);


				//keep Buildings in a list for kelp
				//calc pos of new rect -- getNextBuildingPos() from CityMap
				//JPanel BankAnimaitonPanel = bank.getAnimationPanel()
				//bank.gui.AnimationPanel BankAnimationPanel = new bank.gui.AnimationPanel();

				//construct(buildingName, BankAnimationPanel, LocationTypeEnum.Bank);

				//buildingViewPanel.addCard(BankAnimationPanel, buildingName);//creates card and corresponding button
				//cityViewPanel.addBuildingToMap(buildingName, LocationTypeEnum.Bank);
				// add method to set control panel to infoPanel, should
				//		follow addBuildingToMap() path
				//add building details
			}
			if(type == LocationTypeEnum.House){
				ResidentialBuilding house = new ResidentialBuilding(x, y, Constants.BUILDING_WIDTH, Constants.BUILDING_HEIGHT);
				house.setName(buildingName);
				construct(house); 
			}
			if(type == LocationTypeEnum.Restaurant){
				//restaurant.strottma.gui.AnimationPanel animationPanel= new restaurant.strottma.gui.AnimationPanel();

				//RestaurantRecords restRecords = new restaurant.strottma.gui.RestaurantRecords(animationPanel);
				//citizenRecords.addBuildingRecord(restRecords);

				//construct(buildingName, animationPanel, LocationTypeEnum.Restaurant); 
			}
			if(type == LocationTypeEnum.Market){
				MarketBuilding market = new MarketBuilding(x, y, Constants.BUILDING_WIDTH, Constants.BUILDING_HEIGHT);
				market.setName(buildingName);
				construct(market);
			}
			if(type == LocationTypeEnum.Hospital){
				HospitalBuilding hospital = new HospitalBuilding(x, y, Constants.BUILDING_WIDTH, Constants.BUILDING_HEIGHT);
				hospital.setRecords(citizenRecords);
				hospital.setName(buildingName);
				construct(hospital);
			}
			if(type == LocationTypeEnum.Apartment){
				//AptBuilding apt = new AptBuilding(x, y, Constants.BUILDING_WIDTH, Constants.BUILDING_HEIGHT);
				//apt.setName(buildingName);
				//construct(apt);
			}
			if(type == LocationTypeEnum.None){
				MockBuilding mock = new MockBuilding(x, y, Constants.BUILDING_WIDTH, Constants.BUILDING_HEIGHT);
				mock.setName(buildingName);
				construct(mock);
			}
		}
		initializeCornerMapAndKelp(constructedBuildings);
	}

	/**
	 * Adds Building GUI components to the MainFrame
	 * @param building A Building
	 */
	private void construct(Building building){		
		buildingViewPanel.addCard(building.getAnimationPanel(), building.getName());//creates card and corresponding button
		map.addBuildingToMap(building);
		infoPanel.addBuildingInfoPanel(building.getInfoPanel(), building.getName());
		constructedBuildings.add(building);
	}

	void initializeCornerMapAndKelp(List<Building> buildings) {
		try {
			PlainTextInputStream stream = 
					(PlainTextInputStream)getClass()
					.getResource("CornersWithBusstops.txt").getContent();
			Set<Integer> cornersWithBusstops = 
					CornersWithBusstopsParser.parseCornersWithBusstops(stream);
			MapBuilder.createMap(buildings.size(), cornersWithBusstops);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<Corner> corners = MapBuilder.getCreatedCorners();
		List<Corner> busRoute = MapBuilder.getBusRoute();

		ArrayList<CityLocation> locations =
				new ArrayList<CityLocation>(buildings);

		for (Corner corner : corners) {
			locations.add(corner);
		}

		try {
			KelpClass.getKelpInstance().setData(locations, busRoute);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Utilities **/

	public InfoList getPersonInfoList(){
		return personList;
	}
	public CitizenRecords getCitizenRecords(){
		return citizenRecords;
	}
	public InfoPanel getInfoPanel(){
		return infoPanel;
	}
	public void actionPerformed(ActionEvent e) {

	}

	/**
	 * Main routine to get GUI started
	 */
	public static void main(String[] args) {
		MainFrame gui = new MainFrame();
		gui.setTitle("SimCity");
		gui.setVisible(true);
		gui.setResizable(false);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}