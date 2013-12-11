package restaurant.vonbeck.gui;


import java.awt.Color;
import java.awt.Graphics2D;

import restaurant.vonbeck.CookRole;
import restaurant.vonbeck.HostRole;
import restaurant.vonbeck.WaiterRole;
import restaurant.vonbeck.interfaces.Customer;
import agent.gui.Gui;

public class CookGui implements Gui {

    private CookRole agent;
    
    private final int SPAWN_POS_X = -20;
    private final int SPAWN_POS_Y = 330;
    private static final int COOKH = 20;
	private static final int COOKW = 20;
	private static final int COOKY = 
			(int)(LayoutGui.KITCHENY+LayoutGui.KITCHENTILEH*3.5);
	private static final int COOKX = LayoutGui.KITCHENX+16-4+10;
	
    private int xPos = SPAWN_POS_X, yPos = SPAWN_POS_Y;//default waiter position
    private int xDestination = SPAWN_POS_X, yDestination = SPAWN_POS_Y;//default start position

	private boolean moving;
    
    
    public CookGui(CookRole agent) {
        this.agent = agent;
    }

    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;
        else if (xPos > xDestination)
            xPos--;

        if (xPos == xDestination && yPos == yDestination && moving) {
        	moving = false;
        }

    }

    public void draw(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.fillRect(xPos, yPos, COOKW, COOKH);
    }

    public boolean isPresent() {
        return true;
    }

   
    public void DoGoToWork() {
		xDestination = COOKX;
        yDestination = COOKY;
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
