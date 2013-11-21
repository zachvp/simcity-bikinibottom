package market.gui;

import market.CashierRole;
import market.CustomerRole;
import market.interfaces.DeliveryGuy;

import java.awt.*;

public class DeliveryGuyGui implements Gui {

    private DeliveryGuy agent = null;

    private int xPos = 40, yPos = 140;//default cashier position
    private int xDestination = 40, yDestination = 140;//default start position
    
    private static final int DeliveryGuyWidth = 10;
    private static final int DeliveryGuyHeight = 10;
    
    private static final int ExitX = 10;
    private static final int ExitY = 10;

    private static final int HomePosX = 10;
    private static final int HomePosY = 10;
    
    public DeliveryGuyGui(DeliveryGuy dg) {
        this.agent = dg;
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
        
        if (xPos == HomePosX && yPos == HomePosY){
        	agent.Ready();
        }
        
        if (xPos == ExitX && yPos == ExitY){
        	agent.AtExit();
        }


    }
    public void BackReadyStation(){
    	xDestination = HomePosX;
    	yDestination = HomePosY;
    }
    
    public void GoDeliver(){
    	xDestination = ExitX;
    	yDestination = ExitY;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.ORANGE);
        g.fillRect(xPos, yPos, DeliveryGuyWidth, DeliveryGuyHeight);
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
