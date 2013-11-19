package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

//provides scrolling view of city map

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
	
	public void addBuildingToMap(String name){
		map.addBuildingToMap(name);
	}
	public void setBuildingView(BuildingView panel){
		map.setBuildingView(panel);
	}
	
	public ArrayList<Building> getBuildings() {
		return map.getBuildings();
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
