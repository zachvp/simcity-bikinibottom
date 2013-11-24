package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import CommonSimpleClasses.CityLocation.LocationTypeEnum;

/**
 * Provides scrolling view of city map
 * @author Victoria Dea
 *
 */
public class CityView extends JPanel implements ActionListener{
	private MainFrame mainFrame;
	private JScrollPane pane;
	private CityMap map;
	Dimension d;

	public CityView(int w, int h, MainFrame main){
		mainFrame = main;
		d = new Dimension(w-20, h-65);
		
		//setSize(d);
		//TODO setPreferred size, max and min size
		setOpaque(false);
		map = new CityMap();
		pane = new JScrollPane(map, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pane.setPreferredSize(d);
		add(pane);
		
	}
	
	/**
	 * Adds a building to the city map
	 * @param name Name of the building
	 */
	public void addBuildingToMap(String name, LocationTypeEnum type){
		map.addBuildingToMap(name, type);
	}
	
	public void addBuildingToMap(String name, LocationTypeEnum type, JPanel animationPanel){
		map.addBuildingToMap(name, type, animationPanel);
	}
	
	/**
	 * Sets reference to the BuildingView panel
	 * @param panel BuildingView panel
	 */
	public void setBuildingView(BuildingView panel){
		map.setBuildingView(panel);
	}
	
	/**
	 * Returns the list of Buildings on the map
	 * @return ArrayList of Buildings
	 */
	public ArrayList<Building> getBuildings() {
		return map.getBuildings();
	}
	
	
	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setInfoPaneltoMap(InfoPanel infoPanel) {
		map.setInfoPanel(infoPanel);
	}
	

}
