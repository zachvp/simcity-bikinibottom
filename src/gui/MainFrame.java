package gui;


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

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import market.gui.AnimationPanel;
import market.gui.MarketControlPanel;
import market.gui.MarketRecords;
import parser.BuildingDef;
import parser.BuildingPosParser;
import restaurant.strottma.gui.RestaurantPanel;
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
	private JPanel InfoListSlot	= new JPanel();
	
	//Panels
	private BuildingView buildingViewPanel;
	private CityView cityViewPanel;
	public InfoPanel infoPanel;
	
	private PersonCreationPanel personCreationPanel;

	private InfoList buildingList;
	private InfoList personList;
	private CitizenRecords citizenRecords;
	private ArrayList<Building> buildings; //TODO get building list from city view
	
	//TODO Create infoDisplayPanel (all button lists access this)?
	
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
        cityViewPanel = new CityView(cityDim.width, cityDim.height, this);
        cityViewPanel.setBuildingView(buildingViewPanel);
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
        
        JTabbedPane tabbedPane = new JTabbedPane();
        buildingList = new InfoList(listDim.width, listDim.height);
        personList = new InfoList(listDim.width, listDim.height);
        buildingList.setInfoPanel(infoPanel);
        personList.setInfoPanel(infoPanel);
        citizenRecords = new CitizenRecords(this);
        personList.setCitizenRecords(citizenRecords);
        buildingList.setBuildingList(cityViewPanel.getBuildings());
        citizenRecords.setInfoPanel(infoPanel);
        buildingList.setBuildingView(buildingViewPanel);
        tabbedPane.addTab("Buildings", buildingList);
        tabbedPane.addTab("People", personList);
        InfoListSlot.add(tabbedPane);
        buildingViewPanel.setBuildingList(buildingList);
        cityViewPanel.setInfoPaneltoMap(infoPanel);
        
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
	
	private void constructCity(List<BuildingDef> list) {
		for(BuildingDef b: list){
			String buildingName = b.getName();
			LocationTypeEnum type = b.getType();
			
			if(type == LocationTypeEnum.Bank){
				JPanel BankAnimationPanel = new JPanel();
				BankAnimationPanel.setBackground(Color.blue);
				buildingViewPanel.addCard(BankAnimationPanel, buildingName);//creates card and corresponding button
				cityViewPanel.addBuildingToMap(buildingName, LocationTypeEnum.Bank);
				//TODO add method to set control panel to infoPanel, should
				//		follow addBuildingToMap() path
				//add building details
			}
			if(type == LocationTypeEnum.House){
				JPanel HouseAnimationPanel = new JPanel();
				HouseAnimationPanel.setBackground(Color.green);
				buildingViewPanel.addCard(HouseAnimationPanel, buildingName);
				cityViewPanel.addBuildingToMap(buildingName, LocationTypeEnum.House); 
			}
			if(type == LocationTypeEnum.Restaurant){
				restaurant.strottma.gui.AnimationPanel animationPanel= new restaurant.strottma.gui.AnimationPanel();
				//TODO change restpanel to restRecords
				//restaurant.strottma.gui.RestaurantPanel restPanel = new restaurant.strottma.gui.RestaurantPanel(animationPanel);
				//citizenRecords.addBuildingRecord(restPanel);
				//JPanel animationPanel = new JPanel();
				//animationPanel.setBackground(Color.darkGray);
				buildingViewPanel.addCard(animationPanel, buildingName);
				cityViewPanel.addBuildingToMap(buildingName, LocationTypeEnum.Restaurant); 
			}
			if(type == LocationTypeEnum.Market){
				market.gui.AnimationPanel animationPanel = new market.gui.AnimationPanel();
				
				MarketRecords marketRecords = new MarketRecords(animationPanel);
				MarketControlPanel controlPanel = new MarketControlPanel(marketRecords);
				
				citizenRecords.addBuildingRecord(marketRecords);
				
				
				
				
				buildingViewPanel.addCard(animationPanel, buildingName);
				//cityViewPanel.addBuildingToMap(buildingName, LocationTypeEnum.Market, controlPanel);
				
				
			}
			if(type == LocationTypeEnum.Hospital){
				personCreationPanel = new PersonCreationPanel(buildingViewPanel.getDim());
				personCreationPanel.setRecords(citizenRecords);
				buildingViewPanel.addCard(personCreationPanel, "Hospital");
				cityViewPanel.addBuildingToMap("Hospital", LocationTypeEnum.Hospital);
			}
			if(type == LocationTypeEnum.Apartment){
				personCreationPanel = new PersonCreationPanel(buildingViewPanel.getDim());
				personCreationPanel.setRecords(citizenRecords);
				buildingViewPanel.addCard(personCreationPanel, "Apartment");
				cityViewPanel.addBuildingToMap("Apartment", LocationTypeEnum.Apartment);
			}
			if(type == LocationTypeEnum.None){
				personCreationPanel = new PersonCreationPanel(buildingViewPanel.getDim());
				personCreationPanel.setRecords(citizenRecords);
				buildingViewPanel.addCard(personCreationPanel, "None");
				cityViewPanel.addBuildingToMap("None", LocationTypeEnum.None);
			}

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
