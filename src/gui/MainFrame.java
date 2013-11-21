package gui;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import agent.PersonAgent;


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
	
	//Panel Slots
	private JPanel cityViewSlot = new JPanel();
	private JPanel buildingViewSlot = new JPanel();
	private JPanel infoPanelSlot = new JPanel();
	private JPanel InfoListSlot	= new JPanel();
	
	//Panels
	private BuildingView buildingViewPanel;
	private CityView cityViewPanel;
	private InfoPanel infoPanel;
	
	private PersonCreationPanel personCreationPanel;

	private InfoList buildingList;
	private InfoList personList;
	private CitizenRecords citizenRecords;
	
	//TODO Create infoDisplayPanel (all button lists access this)?
	
    //TODO Add timer here?
    
	
	//TODO test agent
	
	public MainFrame(){
		
		//TODO FullScreen frame?
		//Toolkit tk = Toolkit.getDefaultToolkit();  
		//WINDOWX = ((int) tk.getScreenSize().getWidth());  
		//WINDOWY = ((int) tk.getScreenSize().getHeight()); 
		//setExtendedState(Frame.MAXIMIZED_BOTH);
		
		setBounds(50,50, WINDOWX, WINDOWY);
		
		//TODO clone repo to ensure image shows up
		try {
			setContentPane(new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("sky_background.png")))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setLayout(new BorderLayout(5,10));
		 
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
        infoPanel.setCitizenRecords(citizenRecords);
        infoPanelSlot.add(infoPanel);
        
        //List of Buildings/People buttons
        Dimension listDim = new Dimension((int)(WINDOWX * .4), (int) (WINDOWY * .3));
        InfoListSlot.setPreferredSize(listDim);
        InfoListSlot.setMaximumSize(listDim);
        InfoListSlot.setMinimumSize(listDim);
        InfoListSlot.setOpaque(false);
        //buildingListSlot.setBorder(BorderFactory.createTitledBorder("Building List"));
        //buildingListSlot.setLayout(new CardLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        buildingList = new InfoList(listDim.width, listDim.height);
        
        
        personList = new InfoList(listDim.width, listDim.height);
        buildingList.setInfoPanel(infoPanel);
        personList.setInfoPanel(infoPanel);
        citizenRecords = new CitizenRecords(this);
        //citizenRecords.setPersonInfoList(personList);
        buildingList.setBuildingView(buildingViewPanel);
    
        
        //TODO addInfoPanel(infoPanel)?
       // buildingList.setInfoPanel(buildingInfoPanel);
       // personList.setInfoPanel(personInfoPanel);
        tabbedPane.addTab("Buildings", buildingList);
        tabbedPane.addTab("People", personList);
        InfoListSlot.add(tabbedPane);
        //buildingListSlot.add(buildingList);
                
        buildingViewPanel.setBuildingList(buildingList);
        //TODO
        personCreationPanel = new PersonCreationPanel(buildingViewPanel.getDim());
        personCreationPanel.setRecords(citizenRecords);
        buildingViewPanel.addCard(personCreationPanel, "Hospital");
        cityViewPanel.addBuildingToMap("Hospital");
        
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
		
	//	ArrayList<Building> buildings = cityViewPanel.getBuildings();
		//for (int i=0; i<buildings.size(); i++ ) {
			//Building b = buildings.get(i);
			//BuildingPanel bp = new BuildingPanel( b, i, this );
			//b.setBuildingPanel( bp );
			//buildingPanels.add( bp, "" + i );
		//}
		
		
		
		
		//TODO animation panel/map test
		String name = "Magenta";
		JPanel testAnimationPanel = new JPanel();
		testAnimationPanel.setSize(buildingViewPanel.getSize());
		testAnimationPanel.setBackground(Color.magenta);
		buildingViewPanel.addCard(testAnimationPanel, name);//creates card and corresponding button
		cityViewPanel.addBuildingToMap(name); //creates building on map
		
		String name2 = "Cyan";
		JPanel testAnimationPanel2 = new JPanel();
		testAnimationPanel2.setBackground(Color.CYAN);
		buildingViewPanel.addCard(testAnimationPanel2, name2);
		cityViewPanel.addBuildingToMap(name2);
		/*for(int i=0; i<8; i++){
			cityViewPanel.addBuildingToMap(name);
			cityViewPanel.addBuildingToMap(name2);
		}
		*/
	
		
	}
	
	//TODO read in from config file, building (type, name)
	//make buildingAnimationPanels example
	//BankAnimation bankAnimation = new BuildingAnimation("Bank");
	//addAnimationPanel(bankAnimation, "Bank");
	//buildingViewPanel.showCard("Bank")
	
	
	//TODO add animation panels here?
	public void addAnimationPanel(JPanel panel, String name){
		buildingViewPanel.addCard(panel, name);
	}
	
	public void addPersonToCity(PersonAgent p){
		//TODO
	}
	
	//from personinfolist, later cityMap/animationpanel
	public void displayPersonInfo(){
		
	}
	//from citymap and buildinginfolist
	public void displayBuildingInfo(String name){
		
		//infoPanel.updateInfoPanel(building);
	}
	
	
	public InfoList getPersonInfoList(){
		return personList;
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
