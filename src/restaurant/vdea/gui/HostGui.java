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
    
    public int xTable = 150;
    public int yTable = 150;
    public int tableNumber = 1;
    

    public HostGui(HostRole Role) {
        this.role = Role;
         
        xHome = 130;
        yHome = 15;
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

    public void draw(Graphics2D g) {
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

    public void DoBringToTable(CustomerRole customer, int tblNum) {
    	//Role.setBusy(true);
    	tableNumber = tblNum;
    	if (tableNumber == 1){
    		xTable = 150;
    	    yTable = 150;
    	}
    	if (tableNumber == 2)
    	{
    		xTable = 250;
    		yTable = 150;
    	}
    	if (tableNumber ==3){
    		xTable = 150;
    		yTable = 250;
    	}
    	
        System.out.println("Going to TABLE " + tableNumber+ " at " + xTable + ", "+ yTable);
        
        xDestination = xTable + 20;
        yDestination = yTable - 20;
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
