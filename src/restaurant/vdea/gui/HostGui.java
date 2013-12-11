package restaurant.vdea.gui;

import java.awt.*;

import agent.gui.Gui;
import restaurant.vdea.CustomerRole;
import restaurant.vdea.HostRole;

public class HostGui implements Gui {

    private HostRole role = null;

    private int xPos = -60, yPos = -60;//default waiter position
    private int xDestination = -60, yDestination = -60;//default start position
    RestaurantGui gui;
    private int xHome, yHome;
    
    

    public HostGui(HostRole Role) {
        this.role = Role;
         
        yHome = 150;
        xHome = 15;
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
        
    }

    public void draw(Graphics2D g, boolean gradingView) {
        g.setColor(Color.RED);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }
    
    public void DoGoToLobby(){
    	xDestination = xHome;
    	yDestination = yHome;
    }

    public void DoLeave() {
        xDestination = -60;
        yDestination = -60;
        
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
