package restaurant.vonbeck.gui;


import java.awt.Color;
import java.awt.Graphics2D;

import restaurant.vonbeck.CashierRole;
import restaurant.vonbeck.HostRole;
import restaurant.vonbeck.WaiterRole;
import restaurant.vonbeck.interfaces.Customer;
import agent.gui.Gui;

public class CashierGui implements Gui {

    private CashierRole agent;
    
    private final int SPAWN_POS_X = -20;
    private final int SPAWN_POS_Y = 20;
    private static final int HOSTH = LayoutGui.HOSTH;
	private static final int HOSTW = LayoutGui.HOSTW;
	private static final int HOSTY = WaiterGui.CASH_POS_Y+HOSTH;
	private static final int HOSTX = WaiterGui.CASH_POS_X;
	
    private int xPos = SPAWN_POS_X, yPos = SPAWN_POS_Y;//default waiter position
    private int xDestination = SPAWN_POS_X, yDestination = SPAWN_POS_Y;//default start position

	private boolean moving;
    
    
    public CashierGui(CashierRole cashierRole) {
        this.agent = cashierRole;
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

        if (xPos == xDestination && yPos == yDestination && moving) {
        	moving = false;
        }

    }

    public void draw(Graphics2D g) {
        g.setColor(Color.CYAN);
        g.fillRect(xPos, yPos, HOSTW, HOSTH);
    }

    public boolean isPresent() {
        return true;
    }

   
    public void DoGoToWork() {
		xDestination = HOSTX;
        yDestination = HOSTY;
        moving = true;
	}
    
    public void DoLeaveWork() {
		xDestination = SPAWN_POS_X;
        yDestination = SPAWN_POS_Y;
        moving = true;
	}

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

}
