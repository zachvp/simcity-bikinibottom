package restaurant.strottma.gui;


import restaurant.strottma.CookRole;
import restaurant.strottma.WaiterRole;
import restaurant.strottma.CookRole.GrillOrPlate;
import restaurant.strottma.HostRole.Table;

import java.awt.*;

import agent.gui.Gui;

public class CookGui implements Gui {

    private CookRole role = null;
    
    // dimensions
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;
    
    // cook position - default to home location
    private int xPos = DEFAULT_X;
    private int yPos = DEFAULT_Y;
    
    // destination
    private int xDestination = xPos;
    private int yDestination = yPos;
        
    // default location
    private static final int DEFAULT_X = 650-400;
    private static final int DEFAULT_Y = 180;
        
    // refrigerator location
    private static final int FRIDGE_X = 950-400;
    private static final int FRIDGE_Y = 74;
        
    private boolean shouldMsg; // should we message the gui?
    private String orderText = null; // used to display the customer's food

    public CookGui(CookRole role) {
        this.role = role;
        shouldMsg = false;
    }
    
    public void updatePosition() {
        if (xPos < xDestination)
            xPos+=2;
        else if (xPos > xDestination)
            xPos-=2;

        if (yPos < yDestination)
            yPos+=2;
        else if (yPos > yDestination)
            yPos-=2;

        if (shouldMsg && xPos == xDestination && yPos == yDestination) {
        	role.msgAtDestination();
        	shouldMsg = false;
        }
        
    }

    public void draw(Graphics2D g) {
    	// draw the agent
    	g.setColor(Color.BLUE);
        g.fillRect(xPos, yPos, WIDTH, HEIGHT);
        
        // draw the order
     	if (orderText != null) {
     		g.setColor(Color.BLACK);
     		g.drawString(orderText, xPos + WIDTH, yPos + HEIGHT);
     	}
    }
    
    public void DoGoHome() {
    	xDestination = DEFAULT_X;
    	yDestination = DEFAULT_Y;
    	shouldMsg = false;
    	this.orderText = null;
    }
    
    public void DoCook(int x, int y, String orderText) {
    	xDestination = x;
    	yDestination = y;
    	shouldMsg = true;
    	this.orderText = orderText;
    }
    
    public void DoPlate(int x, int y, String orderText) {
    	xDestination = x;
    	yDestination = y;
    	shouldMsg = true;
    	this.orderText = orderText;
    }
    
    public void DoGoToFridge() {
    	xDestination = FRIDGE_X;
    	yDestination = FRIDGE_Y;
    	shouldMsg = true;
    	this.orderText = null;
    }
        
    public boolean isPresent() {
        return true;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
