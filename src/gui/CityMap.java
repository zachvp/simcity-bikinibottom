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
import agent.Constants;

/**
 * A panel containing the map of the city
 * @author Victoria Dea
 *
 */
public class CityMap extends JPanel implements MouseListener {

		
	private int col, row = 0;
	
	ArrayList<Building> buildings;
	BuildingView buildingView; //Ref to buildingview
	BufferedImage image;
	ImageIcon icon;

	public CityMap(){
		Dimension panelDim = new Dimension(Constants.MAP_WIDTH, Constants.MAP_HEIGHT);
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
	
	/**
	 * Reference to the panel that holds all the internal building views
	 * @param panel A BuildingView panel
	 */
	public void setBuildingView(BuildingView panel){
		buildingView = panel;
	}
	
	/**
	 * Adds a new building to the map
	 * @param name Name of the building
	 */
	public void addBuildingToMap(String name){
		if (col >= Constants.MAX_BLOCK_COL){
			row++;
			col = 0;
			//if (row > MAX_ROW){ System.out.println("at max map capacity");}
		}
		Building b = new Building(col*150 +10, row*150 +10, Constants.BUILDING_WIDTH, Constants.BUILDING_HEIGHT);
		b.setName(name);
		buildings.add(b);
		col++;
	}

	/**
	 * Paints the images on the map
	 */
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

	/**
	 * Returns an ArrayList of Buildings
	 * @return an ArrayList of Buildings
	 */
	public ArrayList<Building> getBuildings() {
		return buildings;
	}

	/**
	 * Handles mouse clicks on the map
	 */
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
