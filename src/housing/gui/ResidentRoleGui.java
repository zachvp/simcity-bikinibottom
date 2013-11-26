package housing.gui;

import housing.ResidentRole;
import housing.interfaces.Resident;
import housing.interfaces.ResidentGui;

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
public class ResidentRoleGui implements Gui, ResidentGui {

	private Resident resident = null;
	private boolean canRelease = false;// this prevents excessive releases from occurring
	private boolean waiting = false;// checks if resident is going to be idle
	
	// used as reference for furniture and appliance positions
	LayoutGui layoutGui;

	// set up labels
	String guiName = "Resident";
	String eatingFood = "null";

	/* --- Hardcoded Positions --- */
	// default resident position
	private int xPos, yPos;
	private int xDestination = 50, yDestination = 50;//default start position
	private final int JAZZER_SPOT_X = 0;
	private final int JAZZER_SPOT_Y = 0;
	
	/* --- Constructor --- */
	public ResidentRoleGui(ResidentRole role) {
		this.resident = role;
		
		// set start position of resident
		xPos = 350;
		yPos = 25;
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
			resident.msgAtDestination();
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GRAY);
		g.fillRect(xPos, yPos, 20, 20);
	    	
		g.setColor(Color.BLACK);
		g.drawString(guiName, xPos, yPos);
		
		g.drawString(eatingFood, xPos+5, yPos+15);
	}

	public boolean isPresent() {
		return true;
	}

	public Resident getAgent(){
		return resident;
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
		if(layoutGui == null) System.out.println("Gui is null");
		xDestination = layoutGui.getRefrigeratorPosition().width;
		yDestination = layoutGui.getRefrigeratorPosition().height;
		canRelease = true;
	}
	
	public void DoJazzercise(){
		waiting = true;
		xDestination = JAZZER_SPOT_X;
		yDestination = JAZZER_SPOT_Y;
	}
	
	public void setFood(String foodType){
		eatingFood = foodType;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
	
//	public void setLayoutGui(LayoutGui gui){
//		layoutGui = gui;
//	}

//	public void pause(){
//		agent.pause();
//	}
}