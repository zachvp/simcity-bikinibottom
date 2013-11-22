package restaurant.strottma.gui;


import restaurant.strottma.HostRole;
import restaurant.strottma.WaiterRole;
import restaurant.strottma.HostRole.Table;

import java.awt.*;

public class HostGui implements Gui {

    private HostRole role = null;
    
    // dimensions
//    private static final int WIDTH = 20;
//    private static final int HEIGHT = 20;
        
    // host location
    private static final int HOST_X = 0;
    private static final int HOST_Y = 0;
    
    public static final int TABLE_WIDTH = 50;
    public static final int TABLE_HEIGHT = 50;

    public HostGui(HostRole role) {
        this.role = role;
    }
    
    public void updatePosition() {
        /*
    	if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

        if (shouldMsg && xPos == xDestination && yPos == yDestination) {
        	agent.msgAtDestination();
        	shouldMsg = false;
        }
        */
    }

    public void draw(Graphics2D g) {
    	// draw the tables
        g.setColor(Color.YELLOW);
        for (Table t : role.getTables()) {
        	g.fillRect(t.getX(), t.getY(), TABLE_WIDTH, TABLE_HEIGHT);
        }
    	
    	// draw the agent
//        g.setColor(Color.MAGENTA);
//        g.fillRect(xPos, yPos, WIDTH, HEIGHT);
    }

    public boolean isPresent() {
        return true;
    }
    
    public int getXPos() {
        return HOST_X;
    }

    public int getYPos() {
        return HOST_Y;
    }
}
