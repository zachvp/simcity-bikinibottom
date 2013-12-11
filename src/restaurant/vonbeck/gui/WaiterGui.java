package restaurant.vonbeck.gui;


import java.awt.Color;
import java.awt.Graphics2D;

import restaurant.vonbeck.WaiterRole;
import restaurant.vonbeck.interfaces.Customer;
import agent.gui.Gui;

public class WaiterGui implements Gui {

    private WaiterRole agent = null;
    
    private final int CUST_WAIT_POS_X = 75;
    private final int CUST_WAIT_POS_Y = 75;
    private final int SPAWN_POS_X = -20;
    private final int SPAWN_POS_Y = 80;
    private final int COOK_POS_X = LayoutGui.KITCHENX-20-32;
    private final int COOK_POS_Y = LayoutGui.KITCHENY+32;
    final static int CASH_POS_X = 85;
    final static int CASH_POS_Y = 380;

    private int xPos = SPAWN_POS_X, yPos = SPAWN_POS_Y;//default waiter position
    private int xDestination = SPAWN_POS_X, yDestination = SPAWN_POS_X;//default start position

    private static final int xTable = LayoutGui.TABLEX;
    private static final int yTable = (int) (LayoutGui.TABLEY+1.5*LayoutGui.TABLEH);

	private static final int TABLE_SEPARATION = 75;

	private static final int CENTER_POS_X = 200;
	private static final int CENTER_POS_Y = 230;

	private static final int OUT_POS_X = -40;
	private static final int OUT_POS_Y = 100;
	
    private final int HOSTW = 20;
    private final int HOSTH = 20;
    
    private String foodLabel = "";

	private boolean moving;

	private int waiterNum;
    
    
    public WaiterGui(WaiterRole agent, int waiterNum) {
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
        yDestination = yTable + 20;
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
    	yDestination = customer.getCustomerGui().getyPos() + 20;
    	moving = true;
		
	}
    
    public void DoLeaveWork() {
		xDestination = OUT_POS_X;
        yDestination = OUT_POS_Y;
        moving = true;
	}

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

}
