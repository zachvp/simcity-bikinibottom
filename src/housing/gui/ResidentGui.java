package housing.gui;

import housing.interfaces.Resident;

import java.awt.Color;
import java.awt.Graphics2D;

import agent.gui.Gui;

public class ResidentGui implements Gui {

	private Resident resident = null;
	private boolean canRelease = false;//this prevents excessive releases from occurring
	private boolean waiting = false;//checks if resident is going to be idle

	String guiName = "Resident";//text of customer order

	private int xPos = 20, yPos = 20;//default resident position
	private int xDestination = 50, yDestination = 50;//default start position

	public ResidentGui(Resident resident) {
		this.resident = resident;
	}

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
//			agent.msgAtDest();
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

	public void DoGoToSomeWhere(int x, int y) {
		waiting = false;
		xDestination = x + 20;
		yDestination = y - 20;
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