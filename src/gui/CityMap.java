package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import transportation.gui.TransportationGuiController;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
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
	BufferedImage image, hospitalImage, KrustyKrabImage, snailpoNullImage, bankImage, marketImage, houseImage;
	ImageIcon icon;
	InfoPanel infoPanel;
	
	private List<Gui> guis = new ArrayList<Gui>();

	private java.util.Timer utilTimer;

	public CityMap(){
		Dimension panelDim = new Dimension(Constants.MAP_WIDTH, Constants.MAP_HEIGHT);
		setPreferredSize(panelDim);
		setMaximumSize(panelDim);
		setMinimumSize(panelDim);
		
		
		try {
			image = ImageIO.read(getClass().getResource("map_background.png"));
			hospitalImage = ImageIO.read(getClass().getResource("hospital.png"));
			KrustyKrabImage = ImageIO.read(getClass().getResource("krusty_krab.png"));
			snailpoNullImage = ImageIO.read(getClass().getResource("snailpo_sign.png"));
			bankImage = ImageIO.read(getClass().getResource("bank.png"));
			marketImage = ImageIO.read(getClass().getResource("market.png"));
			houseImage = ImageIO.read(getClass().getResource("pineapple_house.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		icon = new ImageIcon(image);
		
		// Add the transportations to the guis
		guis.add(TransportationGuiController.getInstance());

		buildings = new ArrayList<Building>();

		addMouseListener(this);
		
		javax.swing.Timer timer = new javax.swing.Timer(10, this); //call update every 1/10 sec
    	timer.start();
    	
    	utilTimer = new java.util.Timer();

    	utilTimer.scheduleAtFixedRate(new TimerTask() {
    		public void run() {
    			updatePosition();
    		}
    	}, 0, 25);
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
		Rectangle2D tr;	
		TexturePaint tp;
		for ( int i=0; i<buildings.size(); i++ ) {
			Building b = buildings.get(i);
			if(b.type() == LocationTypeEnum.Hospital){
				tr = new Rectangle2D.Double(b.x, b.y, b.getWidth(), b.getHeight());
				tp = new TexturePaint(hospitalImage, tr);
				//g2.setColor(Color.white);
				g2.setPaint(tp);
				g2.fill(b);
			}
			else if(b.type() == LocationTypeEnum.Restaurant){
				tr = new Rectangle2D.Double(b.x, b.y, b.getWidth(), b.getHeight());
				tp = new TexturePaint(KrustyKrabImage, tr);
				g2.setPaint(tp);
				g2.fill(b);
			}
			else if(b.type() == LocationTypeEnum.Apartment){
				tr = new Rectangle2D.Double(b.x, b.y, b.getWidth(), b.getHeight());
				tp = new TexturePaint(houseImage, tr);
				g2.setPaint(tp);
				g2.fill(b);
			}
			else if(b.type() == LocationTypeEnum.None){
				tr = new Rectangle2D.Double(b.x, b.y, b.getWidth(), b.getHeight());
				tp = new TexturePaint(snailpoNullImage, tr);
				g2.setPaint(tp);
				g2.fill(b);
			}
			else if(b.type() == LocationTypeEnum.Bank){
				tr = new Rectangle2D.Double(b.x, b.y, b.getWidth(), b.getHeight());
				tp = new TexturePaint(bankImage, tr);
				g2.setPaint(tp);
				g2.fill(b);
			}
			else if(b.type() == LocationTypeEnum.Market){
				tr = new Rectangle2D.Double(b.x, b.y, b.getWidth(), b.getHeight());
				tp = new TexturePaint(marketImage, tr);
				g2.setPaint(tp);
				g2.fill(b);
			}
			else{
				g2.setColor(Color.DARK_GRAY);
				g2.fill(b);
			}
		}
		
		for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
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
