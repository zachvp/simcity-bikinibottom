package restaurant.strottma.gui;


import restaurant.strottma.WaiterRole;
import restaurant.strottma.HostRole.Table;

import java.awt.*;

import agent.gui.Gui;

public class WaiterGui implements Gui {

    private WaiterRole role = null;
    private RestaurantGui gui = null;
    
    // dimensions
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;
    
    // screen dimensions
    private static final int SCREEN_X = 450;
    private static final int SCREEN_Y = 350;

    // waiter position - default to host location
    private int xPos = HOST_X;
    private int yPos = HOST_Y;
    
    // destination
    private int xDestination = xPos;
    private int yDestination = yPos;
    
    // host location
    private static final int HOST_X = -WIDTH;
    private static final int HOST_Y = -HEIGHT;
    
    // default location
    private static final int DEFAULT_X = -WIDTH;
    private static final int DEFAULT_Y = (int) SCREEN_Y / 2;
    
    // cook location
    private static final int COOK_X = 220;//650;
    private static final int COOK_Y = 80;
    
    // cashier location
    private static final int CASHIER_X = 500-400;
    private static final int CASHIER_Y = -HEIGHT;
    
    // customer waiting area
    private static final int CUST_WAIT_X = 130-100;
    private static final int CUST_WAIT_Y = 80;
    
    private boolean shouldMsg;
    private String orderText = null; // used to display the customer's food

    public WaiterGui(WaiterRole role, RestaurantGui gui) {
        this.role = role;
        this.gui = gui;
        shouldMsg = false;
    }

    public void setFatigued() {
        this.role.msgGotFatigued();
    }
    
    public void setEndBreak() {
    	this.role.msgEndBreak();
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
    	// draw the Role
    	g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, WIDTH, HEIGHT);
        
        // draw the order
     	if (orderText != null) {
     		g.setColor(Color.BLACK);
     		g.drawString(orderText, xPos + WIDTH, yPos + HEIGHT);
     	}
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoToTable(Table t) {
        xDestination = t.getX() + WIDTH;
        yDestination = t.getY() - HEIGHT;
        shouldMsg = true;
        this.orderText = null;
    }
    
    // bringing food to table
    public void DoGoToTable(Table t, String orderText) {
    	DoGoToTable(t);
    	this.orderText = orderText;
    }
    
    public void DoGoToHost() {
    	xDestination = HOST_X;
    	yDestination = HOST_Y;
    	shouldMsg = true;
    	this.orderText = null;
    }
    
    public void DoGoToCustWaitArea() {
    	xDestination = CUST_WAIT_X;
    	yDestination = CUST_WAIT_Y;
    	shouldMsg = true;
    	this.orderText = null;
    }
    
    public void DoGoToCook() {
    	xDestination = COOK_X;
        yDestination = COOK_Y;
        shouldMsg = true;
        this.orderText = null;
    }
    
    public void DoGoToCashier() {
    	// xDestination = CASHIER_X;
    	yDestination = CASHIER_Y;
    	shouldMsg = true;
    	this.orderText = null;
    }
    
    // bringing order to cook
    public void DoGoToCook(String orderText) {
    	DoGoToCook();
    	this.orderText = orderText + "?";
    }

    public void DoLeaveCustomer() {
        xDestination = role.getWaitX();
        yDestination = role.getWaitY();
        shouldMsg = false;
        this.orderText = null;
    }
    
    public void DoGoOnBreak() {
    	DoLeaveCustomer();
    }
    
    public void DoGoOffBreak() {
    	//gui.setRoleEnabled(role);
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
