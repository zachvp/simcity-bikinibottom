package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CityMap extends JPanel implements MouseListener {

	private final int MAP_WIDTH = 900;
	private final int MAP_HEIGHT = 700;
	private final int MAX_COL = 6;
	//private final int MAX_ROW = 4;	
	private int col, row = 0;
	
	ArrayList<Building> buildings;
	BuildingView buildingView; //Ref to buildingview
	BufferedImage image;
	ImageIcon icon;

	public CityMap(){
		Dimension panelDim = new Dimension(MAP_WIDTH, MAP_HEIGHT);
		setPreferredSize(panelDim);
		//setMaximumSize(panelDim);
		//setMinimumSize(panelDim);
		//setBackground(Color.gray);
		
		
		try {
			image = ImageIO.read(getClass().getResource("map_background.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel backgroundImage;
		icon = new ImageIcon(image);
		backgroundImage = new JLabel(icon);

		//add(backgroundImage);
		buildings = new ArrayList<Building>();

		addMouseListener(this);
	}
	
	public void setBuildingView(BuildingView panel){
		buildingView = panel;
	}
	
	public void addBuildingToMap(String name){
		if (col >= MAX_COL){
			row++;
			col = 0;
			//if (row > MAX_ROW){ System.out.println("at max map capacity");}
		}
		Building b = new Building(col*150 +10, row*150 +10, 90, 90);
		b.setName(name);
		buildings.add(b);
		col++;
	}


	public void paintComponent( Graphics g ) {
		Graphics2D g2 = (Graphics2D)g;
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.drawImage(icon.getImage(), 0, 0, null);
		//g2.setColor(getBackground());
		
        
		
	

		for ( int i=0; i<buildings.size(); i++ ) {
			Building b = buildings.get(i);
			if(b.getName().equals("Hospital")){
				g2.setColor(Color.white);
				g2.fill(b);
				
			}else{
				g2.setColor(Color.DARK_GRAY);
				g2.fill(b);
			}
			
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
