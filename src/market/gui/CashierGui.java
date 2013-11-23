package market.gui;


import market.CashierRole;
import market.CustomerRole;
import market.interfaces.Cashier;

import java.awt.*;

public class CashierGui implements Gui {

    private Cashier agent = null;

    private int xPos = 180, yPos = -30;//default cashier position
    private int xDestination = ExitX1, yDestination = ExitY1;//default start position
    
    private static final int CashierWidth = 15;
    private static final int CashierHeight = 15;
    
    private static final int FrontDeskX = 170;
    private static final int FrontDeskY = 250;
    
    private static final int BenchX = 170;
    private static final int BenchY = 270;
    

    private static final int ExitX1 = 80;
    private static final int ExitY1 = 250;
    
    private static final int ExitX = 180;
    private static final int ExitY = -50;
    
    private enum Command {noCommand, GoToCashier, GoToBench, GoToExit1, GoToExit, GoToWork};
	private Command command=Command.GoToWork;


	private MarketControlPanel panel;
	
    public CashierGui(Cashier ca) {
        this.agent = ca;
        
    }
    
    public void setMarketControlPanel(MarketControlPanel p){
    	panel = p;
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

        	if (command==Command.GoToWork){
        		GoToFrontDesk();
        	}
			if (command==Command.GoToCashier) {
				agent.AtFrontDesk();
			}
			else if (command==Command.GoToBench) {
				agent.AtBench();
			}
			else if (command==Command.GoToExit1){
				ContinueOffWork();
			}
			if (command == Command.GoToExit){
				agent.AtExit();
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
    
    public void OffWork(){
    	xDestination = ExitX1;
    	yDestination = ExitY1;
    	command = command.GoToExit1;
    }
    
    public void ContinueOffWork(){
    	xDestination = ExitX;
    	yDestination = ExitY;
    	command = command.GoToExit;

    }

    public void Update() {
    	if (panel == null)
    		return;
		panel.UpdateInventoryLevelWithoutButton();
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
