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
    
<<<<<<< HEAD
    private enum Command {noCommand, GoToCashier, GoToBench};
	private Command command=Command.noCommand;
=======
    private static final int ExitX1 = 80;
    private static final int ExitY1 = 250;
    
    private static final int ExitX = 180;
    private static final int ExitY = -50;
    
    private enum Command {noCommand, GoToCashier, GoToBench, GoToExit1, GoToExit, GoToWork};
	private Command command=Command.GoToWork;
>>>>>>> master

	private MarketPanel panel;
	
    public CashierGui(Cashier ca, MarketPanel p) {
        this.agent = ca;
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
<<<<<<< HEAD
			if (command==Command.GoToCashier) 
				agent.AtFrontDesk();
			else if (command==Command.GoToBench) {
				agent.AtBench();
			}
=======
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
				
>>>>>>> master
		command=Command.noCommand;
        }

       
<<<<<<< HEAD
=======
    }
    public void GoToWork(){
    	xDestination = ExitX1;
    	yDestination = ExitY1;
    	command = command.GoToWork;
>>>>>>> master
    }
    
    public void GoToFrontDesk(){
    	xDestination = FrontDeskX;
    	yDestination = FrontDeskY;
    	command=Command.GoToCashier;
    }
    
    public void GoToBench(){
    	System.out.println ("Going to Bench");
    	xDestination = BenchX;
    	yDestination = BenchY;
    	command=Command.GoToBench;
<<<<<<< HEAD
=======
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
>>>>>>> master
    }

    public void Update() {
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
