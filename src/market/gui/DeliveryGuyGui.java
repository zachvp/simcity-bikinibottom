package market.gui;

import market.CashierRole;
import market.CustomerRole;
import market.interfaces.DeliveryGuy;

import java.awt.*;

import agent.gui.Gui;

public class DeliveryGuyGui implements Gui {

    private DeliveryGuy agent = null;

    private int xPos = 130, yPos = -30;//default cashier position
    private int xDestination = 130, yDestination = -30;//default start position
    
    private static final int DeliveryGuyWidth = 10;
    private static final int DeliveryGuyHeight = 10;
    
    private static final int DeliverExitX = -10;
    private static final int DeliverExitY = -10;
    
    private static final int ExitX = 130;
    private static final int ExitY = -50;

    private static final int HomePosX = 125;
    private static final int HomePosY = 150;
    
    private enum Command {noCommand, GoHome, GoDeliver, GoToExit};
	private Command command=Command.noCommand;
    
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
        
        if (xPos == xDestination && yPos == yDestination){
        	if (command==Command.GoHome) 
				agent.Ready();
			else if (command==Command.GoDeliver) {
				agent.AtDeliverExit();
			}
			else if (command==Command.GoToExit){
				agent.AtExit();
			}
        	command=Command.noCommand;
        }
        


    }
    public void BackReadyStation(){
    	xDestination = HomePosX;
    	yDestination = HomePosY;
    	command = command.GoHome;
    }
    
    public void GoDeliver(){
    	xDestination = DeliverExitX;
    	yDestination = DeliverExitY;
    	command = command.GoDeliver;
    }
    
    public void OffWork(){
    	xDestination = ExitX;
    	yDestination = ExitY;
    	command = command.GoToExit;
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
