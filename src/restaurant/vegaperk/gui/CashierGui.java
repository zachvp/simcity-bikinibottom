package restaurant.vegaperk.gui;

import java.awt.*;
import restaurant.vegaperk.interfaces.Cashier;

import agent.gui.Gui;

public class CashierGui implements Gui{

	private Cashier agent = null;
	private boolean isPresent = true;
	private String holding = null;
	
	/** Will store the items cooking at each grill */
	RestaurantGui gui;

	private int xPos = 0, yPos = 0;
	private int xDestination = 160, yDestination = 310;
	private boolean canRelease = false;
	
	private static final int width = 20;
	private static final int height = 20;
	
	private static final int homeX = 160;
	private static final int homeY = 310;
	
	public CashierGui(Cashier c, RestaurantGui gui){
		this.agent = c;
		this.gui = gui;
		
		xPos = 0;
		yPos = 0;
		xDestination = 160;
		yDestination = 310;
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
		g.setColor(Color.CYAN);
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
	public Cashier getAgent(){
		return agent;
	}
}
