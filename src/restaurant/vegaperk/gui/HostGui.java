package restaurant.vegaperk.gui;

import java.awt.*;
import restaurant.vegaperk.backend.HostRole;

import agent.gui.Gui;

public class HostGui implements Gui{

	private HostRole agent = null;
	private boolean isPresent = false;
	private String holding = null;
	
	/** Will store the items cooking at each grill */
	RestaurantGui gui;

	private int xPos = 0, yPos = 0;
	private int xDestination = 10, yDestination = 10;
	private boolean canRelease = false;
	
	private static final int width = 20;
	private static final int height = 20;
	
	private static final int homeX = 10;
	private static final int homeY = 10;
	
	public HostGui(HostRole c, RestaurantGui gui){
		this.agent = c;
		this.gui = gui;
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

		if (xPos == xDestination && yPos == yDestination && canRelease) {
			canRelease = false;
			agent.msgAtDestination();
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
    	g.fillRect(xPos, yPos, width, height);

	    if(holding != null){
		    g.setColor(Color.BLACK);
	    	g.drawString(holding, xPos, yPos);
	    }
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoGoHome() {
		xDestination = homeX;
		yDestination = homeY;
		canRelease = true;
	}
	
	public void DoLeaveWork() {
		xDestination = -20;
		yDestination = -20;
		canRelease = true;
	}
	
	/** Utilities */
	public HostRole getAgent(){
		return agent;
	}
}
