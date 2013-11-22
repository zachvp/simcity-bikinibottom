package restaurant.strottma.gui;


import restaurant.strottma.CookRole;
import restaurant.strottma.WaiterRole;
import restaurant.strottma.CookRole.GrillOrPlate;
import restaurant.strottma.HostRole.Table;

import java.awt.*;

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
    private static final int DEFAULT_X = 650;
    private static final int DEFAULT_Y = 80;
        
    // refrigerator location
    private static final int FRIDGE_X = 950;
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
    	// draw the grills
    	for (GrillOrPlate grill : role.getGrills()) {
    		g.setColor(Color.DARK_GRAY);
    		g.drawRect(grill.getX(), grill.getY(), 30, 30);
    		if (grill.orderVisible() && grill.getOrder() != null) {
    			g.setColor(Color.BLACK);
    			g.drawString(grill.getOrder().getChoice().substring(0, 2),
    					grill.getX()+10, grill.getY()+20);
    		}
    	}
    	
    	// draw the plating areas
    	for (GrillOrPlate plateArea : role.getPlateAreas()) {
    		g.setColor(Color.LIGHT_GRAY);
    		g.drawRect(plateArea.getX(), plateArea.getY(), 30, 30);
    		if (plateArea.orderVisible()) {
    			g.setColor(Color.BLACK);
    			g.drawString(plateArea.getOrder().getChoice().substring(0, 2),
    					plateArea.getX()+10, plateArea.getY()+20);
    		}
    	}
    	
    	// draw the fridge
    	g.setColor(Color.BLACK);
    	g.fillRect(FRIDGE_X, FRIDGE_Y, 30, 30);
    	
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
