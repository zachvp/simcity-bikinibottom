package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;




/** 
 * Main GUI class
 * Contains the main frame and structure for panels
 * @author Victoria Dea
 *
 */
public class MainFrame extends JFrame implements ActionListener {
	
	private int WINDOWX = 1500;
	private int WINDOWY = 800;
	
	private JPanel cityViewSlot = new JPanel();
	private JPanel buildingViewSlot = new JPanel();
	private JPanel infoPanelSlot = new JPanel();
	private JPanel buildingListSlot	= new JPanel();
	
	private BuildingView buildingViewPanel;
	private BuildingList buildingList;
	
	//TODO Test
	//JButton b = new JButton("grey");
    //JButton b2 = new JButton("red");
	
    //TODO Add timer here
    
	public MainFrame(){
		
		//TODO FullScreen?
		//Toolkit tk = Toolkit.getDefaultToolkit();  
		//WINDOWX = ((int) tk.getScreenSize().getWidth());  
		//WINDOWY = ((int) tk.getScreenSize().getHeight()); 
		//setExtendedState(Frame.MAXIMIZED_BOTH);
		
		setBounds(50,50, WINDOWX, WINDOWY);
		setLayout(new BorderLayout(5,10));
		 
		//Internal Building View
        Dimension buildingDim = new Dimension((int)(WINDOWX * .5), (int) (WINDOWY * .7));
        buildingViewSlot.setPreferredSize(buildingDim);
        buildingViewSlot.setMaximumSize(buildingDim);
        buildingViewSlot.setMinimumSize(buildingDim);
        buildingViewSlot.setBorder(BorderFactory.createTitledBorder("Building View"));
        buildingViewPanel = new BuildingView(buildingDim.width, buildingDim.height);
        buildingViewSlot.add(buildingViewPanel);
        
        //City map view
        Dimension cityDim = new Dimension((int)(WINDOWX * .5), (int) (WINDOWY * .7));
        cityViewSlot.setPreferredSize(cityDim);
        cityViewSlot.setMaximumSize(cityDim);
        cityViewSlot.setMinimumSize(cityDim);
        cityViewSlot.setBorder(BorderFactory.createTitledBorder("City View"));
        //TODO Add cityViewPanel to cityViewSlot
        
        /*/TODO Test
        b.addActionListener(this);
        b2.addActionListener(this);
        cityViewSlot.add(b);
        cityViewSlot.add(b2);//*/
             
        //Information Panel
        Dimension infoDim = new Dimension((int)(WINDOWX * .6), (int) (WINDOWY * .3));
        infoPanelSlot.setPreferredSize(infoDim);
        infoPanelSlot.setMaximumSize(infoDim);
        infoPanelSlot.setMinimumSize(infoDim);
        infoPanelSlot.setBorder(BorderFactory.createTitledBorder("Information Panel"));
        //TODO Add infoPanel to infoPanelSlot
        
        //List of Buildings
        Dimension listDim = new Dimension((int)(WINDOWX * .4), (int) (WINDOWY * .3));
        buildingListSlot.setPreferredSize(listDim);
        buildingListSlot.setMaximumSize(listDim);
        buildingListSlot.setMinimumSize(listDim);
        buildingListSlot.setBorder(BorderFactory.createTitledBorder("Building List"));
        buildingList = new BuildingList(listDim.width, listDim.height);
        buildingListSlot.add(buildingList);
        buildingViewPanel.setBuildingList(buildingList);
        
        //JPanel to hold infoPanelSlot and buildingListSlot
        JPanel infoHolder = new JPanel();
        Dimension infoHolderDim = new Dimension(WINDOWX, (int) (WINDOWY * .3));
        infoHolder.setPreferredSize(infoHolderDim);
        infoHolder.setMaximumSize(infoHolderDim);
        infoHolder.setMinimumSize(infoHolderDim);
        infoHolder.setLayout(new BorderLayout(5,10));
        infoHolder.add(infoPanelSlot, BorderLayout.WEST);
        infoHolder.add(buildingListSlot, BorderLayout.EAST);
        
		add(buildingViewSlot, BorderLayout.EAST);
		add(cityViewSlot, BorderLayout.WEST);
		add(infoHolder, BorderLayout.SOUTH);	

		//test
		/*JPanel blank2 = new JPanel();
		blank2.setBackground(Color.red);
		buildingViewPanel.addCard(blank2, "blank2");*/
	}
	
	//buttons won't add to buildingList when created in constructor
	//TODO add animation panels here
	public void addAnimationPanel(JPanel panel, String name){
		buildingViewPanel.addCard(panel, name);
	}
	
	public void actionPerformed(ActionEvent e) {
		//TODO Test
		/*if(e.getSource() == b2){
			buildingViewPanel.showCard("blank2");
			buildingList.addBuilding("blank2");
		}
		
		if(e.getSource() == b){
			buildingViewPanel.showCard("blank");
			buildingList.addBuilding("blank");
		}//*/
		
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
