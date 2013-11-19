package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class CityMap extends JPanel implements MouseListener {

	private final int MAP_WIDTH = 800;
	private final int MAP_HEIGHT = 800;
	private final int MAX_COL = 5;
	private final int MAX_ROW = 4;	
	private int col, row = 0;
	
	ArrayList<Building> buildings;
	BuildingView buildingView; //Ref to buildingview


	public CityMap(){
		Dimension panelDim = new Dimension(MAP_WIDTH, MAP_HEIGHT);
		setPreferredSize(panelDim);
		setMaximumSize(panelDim);
		setMinimumSize(panelDim);
		setBackground(Color.gray);

		buildings = new ArrayList<Building>();

		addMouseListener(this);
	}
	
	public void setBuildingView(BuildingView panel){
		buildingView = panel;
	}
	
	public void addBuildingToMap(String name){
		if (col > MAX_COL){
			row++;
			col = 0;
			if (row > MAX_ROW){ System.out.println("at max map capacity");}
		}
		Building b = new Building(col*150 +10, row*150 +10, 100, 100);
		b.setName(name);
		buildings.add(b);
		col++;
	}


	public void paintComponent( Graphics g ) {
		System.out.println("paint");

		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
		
		
		g2.setColor( Color.black );

		for ( int i=0; i<buildings.size(); i++ ) {
			Building b = buildings.get(i);
			g2.fill(b);
		}
	}

	public ArrayList<Building> getBuildings() {
		return buildings;
	}


	public void mouseClicked(MouseEvent me) {
		//Check to see which building was clicked

		for ( int i=0; i<buildings.size(); i++ ) {
			Building b = buildings.get(i);
			if ( b.contains( me.getX(), me.getY() ) ) {
				buildingView.showCard(b.getName());
				System.out.println(b.getName()+" clicked");

			}
		}


	}

	public void mouseEntered(MouseEvent arg0) {		
	}

	public void mouseExited(MouseEvent arg0) {		
	}

	public void mousePressed(MouseEvent arg0) {	
	}

	public void mouseReleased(MouseEvent arg0) {		
	}
}
