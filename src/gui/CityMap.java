package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.Constants;
import agent.gui.Gui;

/**
 * A panel containing the map of the city
 * @author Victoria Dea
 *
 */
public class CityMap extends JPanel implements MouseListener, ActionListener {

	private int col, row = 0;
	
	ArrayList<Building> buildings;
	BuildingView buildingView; //Ref to buildingview
	BufferedImage image;
	ImageIcon icon;
	InfoPanel infoPanel;
	
	private List<Gui> guis = new ArrayList<Gui>();

	private java.util.Timer utilTimer;

	public CityMap(){
		Dimension panelDim = new Dimension(Constants.MAP_WIDTH, Constants.MAP_HEIGHT);
		setPreferredSize(panelDim);
		
		try {
			image = ImageIO.read(getClass().getResource("map_background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		icon = new ImageIcon(image);

		buildings = new ArrayList<Building>();

		addMouseListener(this);
		
		javax.swing.Timer timer = new javax.swing.Timer(10, this); //call update every 1/10 sec
    	timer.start();
    	
    	utilTimer = new java.util.Timer();
    	utilTimer.scheduleAtFixedRate(new TimerTask() {
    		public void run() {
    			updatePosition();
    		}
    	}, 100, 100);
	}
	
	/**
	 * Returns the x coordinate of the next available spot on the map 
	 * @return x coordinate
	 */
	public int getNextBuildingX(){
		return col*(Constants.BUILDING_WIDTH+Constants.SPACE_BETWEEN_BUILDINGS)
				+ Constants.MAP_MARGIN_X;
	}
	/**
	 * Returns the y coordinate of the next available spot on the map 
	 * @return y coordinate
	 */
	public int getNextBuildingY(){
		return row*(Constants.BUILDING_HEIGHT+Constants.SPACE_BETWEEN_BUILDINGS)
				+ Constants.MAP_MARGIN_Y;
	}
	
	/**
	 * Adds a new building to the map
	 * @param name Name of the building
	 */
	public void addBuildingToMap(Building building){
		col++;
		if (col >= Constants.MAX_BLOCK_COL){
			row++;
			col = 0;
		}
		buildings.add(building);
	}
	

	/**
	 * Paints the images on the map
	 */
	public void paintComponent( Graphics g ) {
		Graphics2D g2 = (Graphics2D)g;
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.drawImage(icon.getImage(), 0, 0, null);

        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
				
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

	public void updatePosition() {
		for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
	}
	
	/**
	 * Handles mouse clicks on the map
	 */
	public void mouseClicked(MouseEvent me) {

		for ( int i=0; i<buildings.size(); i++ ) {
			Building b = buildings.get(i);
			if ( b.contains( me.getX(), me.getY() ) ) {
				buildingView.showCard(b.getName());
				infoPanel.updateBuildingInfoPanel(b);
				System.out.println(b.getName()+" clicked");

			}
		}
	}
	
	/** Utilities **/
	
	/**
	 * Adds Gui to the Map
	 * @param gui 
	 */
	public void addGui(Gui gui) {
        guis.add(gui);
    }
	
	/**
	 * Reference to the panel that holds all the internal building views
	 * @param panel A BuildingView panel
	 */
	public void setBuildingView(BuildingView panel){
		buildingView = panel;
	}
	
	public void setInfoPanel(InfoPanel i){
		infoPanel = i;
	}
	
	/**
	 * Returns an ArrayList of Buildings
	 * @return an ArrayList of Buildings
	 */
	public ArrayList<Building> getBuildings() {
		return buildings;
	}

	public void mouseEntered(MouseEvent arg0) {		
	}

	public void mouseExited(MouseEvent arg0) {		
	}

	public void mousePressed(MouseEvent arg0) {	
	}

	public void mouseReleased(MouseEvent arg0) {		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
		
	}
}
