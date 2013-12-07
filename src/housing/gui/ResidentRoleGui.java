package housing.gui;

import housing.backend.ResidentRole;
import housing.interfaces.Resident;
import housing.interfaces.ResidentGui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

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

public class ResidentRoleGui implements Gui, ResidentGui {

	private Resident resident = null;
	
	// this prevents excessive releases from occurring
	private boolean canRelease = false;
	
	// checks if resident is going to be idle
	private boolean waiting = false;
	
	// determines whether to render the gui
	private boolean present = false;
	
	private String speech = "";
	
	// used as reference for furniture and appliance positions
	LayoutGui layoutGui;
	
	// image for Resident
	private BufferedImage resImage;
	private ImageIcon residentIcon;
	
	// image for krabby patty
	private BufferedImage krabbyPatty;
	private ImageIcon krabbyPattyIcon;

	// set up labels
	String guiName = "Resident";
	boolean eatingFood = false;

	/* --- Hardcoded Positions --- */
	// default resident position
	private int xPos = 130; 
	private int yPos = 15;
	
	private int xDestination = xPos, yDestination = yPos;//default start position
	
	// prime location for Jazzercising
	private final int JAZZER_SPOT_X = 130;
	private final int JAZZER_SPOT_Y = 15;
	
	/* --- Constructor --- */
	public ResidentRoleGui(ResidentRole role, LayoutGui gui) {
		this.resident = role;
		this.layoutGui = gui;
		
		// randomize number for resident gui
//		Random generator = new Random();
//		int res = generator.nextInt(2);
		
		try {
			resImage = ImageIO.read(getClass().getResource("spongebob.png"));
//			else if (res == 1) resImage = ImageIO.read(getClass().getResource("squidward.png"));
			
			krabbyPatty = ImageIO.read(getClass().getResource("krabby_patty.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		residentIcon = new ImageIcon(resImage);
		krabbyPattyIcon = new ImageIcon(krabbyPatty);
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
			resident.msgAtDestination();
		}
	}

	/* --- Draw the Resident Graphics --- */
	public void draw(Graphics2D g) {
		// draw the residence image
		g.drawImage(residentIcon.getImage(), xPos, yPos, null);
	    	
		g.setColor(Color.BLACK);
		
		g.drawString(speech, xPos+5, yPos+10);
		if(eatingFood == true){
			g.drawImage(krabbyPattyIcon.getImage(), xPos, yPos, null);
		}
	}

	public boolean isPresent() {
		return present;
	}

	public Resident getAgent(){
		return resident;
	}
	
	public void DoMoveGary(){
		layoutGui.DoMoveGary();
	}

	public void DoGoToStove() {
		waiting = false;
		xDestination = layoutGui.getStovePosition().width;
		yDestination = layoutGui.getStovePosition().height;
		canRelease = true;
	}
	
	public void DoGoToTable(){
		waiting = false;
		xDestination = layoutGui.getTablePosition().width;
		yDestination = layoutGui.getTablePosition().height;
		canRelease = true;
	}
	
	public void DoGoToRefrigerator(){
		waiting = false;
		xDestination = layoutGui.getRefrigeratorPosition().width;
		yDestination = layoutGui.getRefrigeratorPosition().height;
		canRelease = true;
	}
	
	public void DoJazzercise(){
		waiting = true;
		xDestination = JAZZER_SPOT_X;
		yDestination = JAZZER_SPOT_Y;
	}
	
	public void setFood(boolean state){
		eatingFood = state;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	@Override
	public void setPresent(boolean b) {
		present = b;
	}

	@Override
	public void DoShowSpeech(String speech) {
		this.speech = speech;
	}
}