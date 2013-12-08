package restaurant.vonbeck.gui;


import java.awt.Color;
import java.awt.Graphics2D;

import restaurant.vonbeck.WaiterAgent;
import restaurant.vonbeck.interfaces.Customer;
import agent.gui.Gui;

public class WaiterGui implements Gui {

    private WaiterAgent agent = null;
    
    private final int CUST_WAIT_POS_X = 75;
    private final int CUST_WAIT_POS_Y = 75;
    private final int SPAWN_POS_X = 20;
    private final int SPAWN_POS_Y = 20;
    private final int COOK_POS_X = 430;
    private final int COOK_POS_Y = 120;
    private final int CASH_POS_X = 85;
    private final int CASH_POS_Y = 400;

    private int xPos = SPAWN_POS_X, yPos = SPAWN_POS_Y;//default waiter position
    private int xDestination = CUST_WAIT_POS_X, yDestination = CUST_WAIT_POS_Y;//default start position

    private static final int xTable = 200;
    private static final int yTable = 250;

	private static final int TABLE_SEPARATION = 75;

	private static final int CENTER_POS_X = 200;
	private static final int CENTER_POS_Y = 130;
	
    private final int HOSTW = 20;
    private final int HOSTH = 20;
    
    private String foodLabel = "";

	private boolean moving;

	private int waiterNum;
    
    
    public WaiterGui(WaiterAgent agent, int waiterNum) {
        this.agent = agent;
        this.waiterNum = waiterNum;
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

        if (xPos == xDestination && yPos == yDestination && moving /*&& xPos != HOST_INIT_POS_X && yPos != HOST_INIT_POS_Y*/
        		/*& (xDestination == xTable + 20) & (yDestination == yTable - 20)*/) {
        	//returning = true;
        	moving = false;
        	agent.msgAnimationDone();
        }
        /*
        if (returning && xPos == HOST_INIT_POS_X && yPos == HOST_INIT_POS_Y) {
        	returning = false;
            agent.msgAtDoor();
        }
        */
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, HOSTW, HOSTH);
        g.setColor(Color.BLACK);
		g.drawString(foodLabel,xPos,yPos-5);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(Customer cust, int tableNumber) {
        xDestination = xTable + (tableNumber-1)*TABLE_SEPARATION  + 20;
        yDestination = yTable   - 20;
        moving = true;
    }

    public void DoGoToHost() { DoLeaveCustomer(); }
    
    public void DoGoToCashier() {
		xDestination = CASH_POS_X;
	    yDestination = CASH_POS_Y;
	    moving = true;
	}

	public void DoDisplayFood(String food) {
		foodLabel = food;
	}

	public void DoGoToCenter() {
		xDestination = CENTER_POS_X + 40*waiterNum;
	    yDestination = CENTER_POS_Y;
	}

	public void DoGoToCook() {
    	xDestination = COOK_POS_X;
        yDestination = COOK_POS_Y;
        moving = true;
    }
    
    public void DoLeaveCustomer() {
        xDestination = CUST_WAIT_POS_X;
        yDestination = CUST_WAIT_POS_Y;
        moving = true;
    }
    
    public void DoGoToCustomer(Customer customer) {
    	xDestination = customer.getCustomerGui().getxPos() - 20;
    	yDestination = customer.getCustomerGui().getyPos() - 20;
    	moving = true;
		
	}

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

}
