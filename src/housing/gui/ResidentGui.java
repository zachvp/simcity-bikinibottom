package housing.gui;

import housing.interfaces.Resident;

import java.awt.Color;
import java.awt.Graphics2D;

import agent.gui.Gui;

/**
 * ResidentGui displays the Resident role of a PersonAgent
 * when that agent returns home. The gui moves around and performs tasks,
 * waking up the PersonAgent upon reaching a destination. 
 * @author Zach VP
 *
 */
public class ResidentGui implements Gui {

	private Resident resident = null;
	private boolean canRelease = false;//this prevents excessive releases from occurring
	private boolean waiting = false;//checks if resident is going to be idle

	String guiName = "Resident";//text of customer order

	/* --- Hardcoded Positions --- */
	private int xPos = 20, yPos = 20;//default resident position
	private int xDestination = 20, yDestination = 20;//default start position
	
	private final int STOVE_X = 120;
	private final int STOVE_Y = 200;

	public ResidentGui(Resident resident) {
		this.resident = resident;
	}

	/**
	 * So long as the gui is not at the destination coordinates,
	 * it will move toward the destination.
	 */
	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		if (xPos == xDestination && yPos == yDestination && canRelease && !waiting ) {
			canRelease = false;
			resident.msgAtDest();
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GRAY);
		g.fillRect(xPos, yPos, 20, 20);
	    	
		g.setColor(Color.BLACK);
		g.drawString(guiName, xPos, yPos);
	}

	public boolean isPresent() {
		return true;
	}

	public Resident getAgent(){
		return resident;
	}

	public void DoGoToStove() {
		waiting = false;
		xDestination = STOVE_X;
		yDestination = STOVE_Y;
		canRelease = true;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

//	public void pause(){
//		agent.pause();
//	}
}