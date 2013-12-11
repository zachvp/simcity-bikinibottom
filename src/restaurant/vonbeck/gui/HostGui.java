package restaurant.vonbeck.gui;


import java.awt.Color;
import java.awt.Graphics2D;

import restaurant.vonbeck.HostRole;
import restaurant.vonbeck.WaiterRole;
import restaurant.vonbeck.interfaces.Customer;
import agent.gui.Gui;

public class HostGui implements Gui {

    private HostRole agent;
    
    private final int SPAWN_POS_X = -20;
    private final int SPAWN_POS_Y = 20;
    private static final int HOSTH = LayoutGui.HOSTH;
	private static final int HOSTW = LayoutGui.HOSTW;
	private static final int HOSTY = LayoutGui.HOSTY;
	private static final int HOSTX = LayoutGui.HOSTX;
	
    private int xPos = SPAWN_POS_X, yPos = SPAWN_POS_Y;//default waiter position
    private int xDestination = SPAWN_POS_X, yDestination = SPAWN_POS_Y;//default start position

	private boolean moving;
    
    
    public HostGui(HostRole agent) {
        this.agent = agent;
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
        g.setColor(Color.RED);
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
