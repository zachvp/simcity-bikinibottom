package housing.gui;

import housing.ResidentRole;
import housing.interfaces.MaintenanceWorkerGui;
import housing.interfaces.Resident;
import housing.interfaces.ResidentGui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import agent.gui.Gui;

/**
 * ResidentGui displays the Resident role of a PersonAgent
 * when that agent returns home. The gui moves around and performs tasks,
 * waking up the PersonAgent upon reaching a destination. 
 * @author Zach VP
 *
 */

public class MaintenanceWorkerRoleGui implements Gui, MaintenanceWorkerGui {

	private Resident worker = null;
	
	// this prevents excessive releases from occurring
	private boolean canRelease = false;
	
	// checks if worker is going to be idle
	private boolean waiting = false;
	
	// determines whether to render the gui
	private boolean present = false;
	
	// image for Resident
	private BufferedImage workerImage;
	private ImageIcon workerIcon;
	
	// image for krabby patty
	private BufferedImage tool;
	private ImageIcon toolIcon;

	// set up labels
	String guiName = "Worker";
	boolean showTool = false;

	/* --- Hardcoded Positions --- */
	// default resident position
	private int xPos = 130; 
	private int yPos = 15;
	
	private int xDestination, yDestination;//default start position
	
	// prime location for Jazzercising
	private final int JAZZER_SPOT_X = xPos;
	private final int JAZZER_SPOT_Y = yPos;
	
	/* --- Constructor --- */
	public MaintenanceWorkerRoleGui(ResidentRole role, LayoutGui gui) {
		this.worker = role;
		
		// randomize number for resident gui
		Random generator = new Random();
		int res = generator.nextInt(2);
		
		try {
			workerImage = ImageIO.read(getClass().getResource("spongebob.png"));
			tool = ImageIO.read(getClass().getResource("krabby_patty.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		workerIcon = new ImageIcon(workerImage);
		toolIcon = new ImageIcon(tool);
	}

	/**
	 * So long as the gui is not at the destination coordinates,
	 * it will move toward the destination.
	 */
	public void updatePosition() {
		if (xPos < xDestination) xPos++;
		
		else if (xPos > xDestination) xPos--;

		if (yPos < yDestination) yPos++;
		
		else if (yPos > yDestination) yPos--;

		if (xPos == xDestination && yPos == yDestination && canRelease && !waiting ) {
			canRelease = false;
			worker.msgAtDestination();
		}
	}

	/* --- Draw the Resident Graphics --- */
	public void draw(Graphics2D g) {
		// draw the worker image
//		g.drawImage(workerIcon.getImage(), xPos, yPos, null);
	    	
		g.setColor(Color.WHITE);
		g.drawRect(xPos, yPos, 20, 20);
		
//		g.drawString(eatingFood, xPos+5, yPos+15);
		if(showTool == true){
			g.drawImage(toolIcon.getImage(), xPos, yPos, null);
		}
	}

	public boolean isPresent() {
		return present;
	}

	public Resident getAgent(){
		return worker;
	}

	public void DoGoToLoc() {
		waiting = false;
		xDestination = 0;
		yDestination = 0;
		canRelease = true;
	}
	
	public void setTool(boolean state){
		showTool = state;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
}