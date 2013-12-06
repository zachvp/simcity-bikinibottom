package market.gui;


import market.CashierRole;
import market.CustomerRole;
import market.interfaces.Cashier;
import market.interfaces.CashierGuiInterfaces;
//import market.interfaces.MarketInfoPanel;



import java.awt.*;

import agent.gui.Gui;

public class CashierGui implements Gui, CashierGuiInterfaces {

    private Cashier agent = null;
    
    private String currentTask = "??";

    private int xPos = 130, yPos = -50;//default cashier position
    private int xDestination = ExitX, yDestination = ExitY;//default start position
    
    private static final int CashierWidth = 15;
    private static final int CashierHeight = 15;
    
    private static final int FrontDeskX = 220;
    private static final int FrontDeskY = 150;
    
    private static final int BenchX = 220;
    private static final int BenchY = 170;
    

    private static final int ExitX1 = 130;
    private static final int ExitY1 = 150;
    
    private static final int ExitX = 130;
    private static final int ExitY = -50;
    
    private enum Command {noCommand, GoToCashier, GoToBench, GoToExit1, GoToExit, GoToWork, NotAtWork, GoToWork1};
	private Command command=Command.NotAtWork;


	private MarketInfoPanel panel;
	
    public CashierGui(Cashier ca) {
        this.agent = ca;
        
    }
    
    public void GoToWork(){
    	command=Command.GoToWork;
    }
    
    @Override
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
        		//System.out.println("Im GoToCashier");
        		WalkToWork();
        		return;
        	}
        	else if (command==Command.GoToWork1){
        		GoToFrontDesk();
        		return;
        	}
        	else if (command==Command.GoToCashier) {
				agent.AtFrontDesk();
				currentTask = "AtFrontDesk";
			}
			else if (command==Command.GoToBench) {
				agent.AtBench();
				currentTask = "AtBench";
			}
			else if (command==Command.GoToExit1){
				ContinueOffWork();
			}
			else if (command == Command.GoToExit){
				agent.AtExit();
				currentTask = "AtExit";
				command = Command.NotAtWork;
				return;
			}
				
		command=Command.noCommand;
        }
    }
    
    /* (non-Javadoc)
	 * @see market.gui.MockCashierGui#GoToFrontDesk()
	 */
    @Override
    public void WalkToWork(){
    	xDestination = ExitX1;
    	yDestination = ExitY1;
    	command=Command.GoToWork1;
    	currentTask = "GoingToWork";
    }
    
	public void GoToFrontDesk(){
    	xDestination = FrontDeskX;
    	yDestination = FrontDeskY;
    	command=Command.GoToCashier;
    	currentTask = "GoingToFrontDesk";
    }
    
    
    
    /* (non-Javadoc)
	 * @see market.gui.MockCashierGui#GoToBench()
	 */
    @Override
	public void GoToBench(){
    	xDestination = BenchX;
    	yDestination = BenchY;
    	command=Command.GoToBench;
    	currentTask = "GoingToBench";

    }
    
    /* (non-Javadoc)
	 * @see market.gui.MockCashierGui#OffWork()
	 */
    @Override
	public void OffWork(){
    	xDestination = ExitX1;
    	yDestination = ExitY1;
    	command = command.GoToExit1;
    	currentTask = "GoingToExit";
    }
    
    /* (non-Javadoc)
	 * @see market.gui.MockCashierGui#ContinueOffWork()
	 */
    @Override
	public void ContinueOffWork(){
    	xDestination = ExitX;
    	yDestination = ExitY;
    	command = command.GoToExit;
    	currentTask = "GoingToExit";

    }

    /* (non-Javadoc)
	 * @see market.gui.MockCashierGui#Update()
	 */
    @Override
	public void Update() {
    	if (panel == null)
    		return;
		panel.UpdateInventoryLevelWithoutButton();
	}
    
    /* (non-Javadoc)
	 * @see market.gui.MockCashierGui#draw(java.awt.Graphics2D)
	 */
    @Override
	public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        g.fillRect(xPos, yPos, CashierWidth, CashierHeight);
        
        g.drawString(currentTask, xPos, yPos);
        
    }

    /* (non-Javadoc)
	 * @see market.gui.MockCashierGui#isPresent()
	 */
    @Override
	public boolean isPresent() {
        return true;
    }

    /* (non-Javadoc)
	 * @see market.gui.MockCashierGui#getXPos()
	 */
    @Override
	public int getXPos() {
        return xPos;
    }

    /* (non-Javadoc)
	 * @see market.gui.MockCashierGui#getYPos()
	 */
    @Override
	public int getYPos() {
        return yPos;
    }

	@Override
	public void setMarketInfoPanel(MarketInfoPanel p) {
		panel = p;
		
	}

	
	
}
