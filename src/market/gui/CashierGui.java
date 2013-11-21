package market.gui;


import market.CashierRole;
import market.CustomerRole;
import market.interfaces.Cashier;

import java.awt.*;

public class CashierGui implements Gui {

    private Cashier agent = null;

    private int xPos = 170, yPos = 250;//default cashier position
    private int xDestination = 170, yDestination = 250;//default start position
    
    private static final int CashierWidth = 15;
    private static final int CashierHeight = 15;
    
    private static final int FrontDeskX = 170;
    private static final int FrontDeskY = 250;
    
    private static final int BenchX = 170;
    private static final int BenchY = 270;
    
    private enum Command {noCommand, GoToCashier, GoToBench};
	private Command command=Command.noCommand;

    public CashierGui(Cashier ca) {
        this.agent = ca;
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
        
        if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToCashier) 
				agent.AtFrontDesk();
			else if (command==Command.GoToBench) {
				agent.AtBench();
			}
		command=Command.noCommand;
        }

       
    }
    public void GoToFrontDesk(){
    	xDestination = FrontDeskX;
    	yDestination = FrontDeskY;
    	command=Command.GoToCashier;
    }
    
    public void GoToBench(){
    	xDestination = BenchX;
    	yDestination = BenchY;
    	command=Command.GoToBench;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        g.fillRect(xPos, yPos, CashierWidth, CashierHeight);
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
