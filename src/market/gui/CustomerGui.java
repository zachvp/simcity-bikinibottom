package market.gui;

import market.CashierRole;
import market.interfaces.Customer;
import market.interfaces.CustomerGuiInterfaces;

import java.awt.*;
import java.util.List;

import javax.swing.JButton;

import agent.gui.Gui;

public class CustomerGui implements Gui, CustomerGuiInterfaces{

	private Customer agent = null;
	private boolean isPresent = false;
	private boolean isBuying = false;
	
	private String currentTask;
	private double currentMoney = -1;
	
	MarketGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToCashier, LeaveMarket};
	private Command command=Command.noCommand;

	public CustomerGui(Customer c){ //HostAgent m) {
		agent = c;
		xPos = OffScreenX;
		yPos = OffScreenY;
		xDestination = xFrontDesk;
		yDestination = yFrontDesk;
		//maitreD = m;
		//this.gui = gui;
		isBuying = true;
		agent.goingToBuy();
		setPresent(true);
	}

	/* (non-Javadoc)
	 * @see market.gui.CustomerGuiInterfaces#updatePosition()
	 */
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
			if (command==Command.GoToCashier) {
				agent.msgAnimationFinishedGoToCashier();
				currentTask = "AtFrontDesk";
			}
			else if (command==Command.LeaveMarket) {
				agent.msgAnimationFinishedLeaveMarket();
				currentTask = "AtExit";
				isBuying = false;
			}
			
			
			command=Command.noCommand;
		}
		
	}
 
	/* (non-Javadoc)
	 * @see market.gui.CustomerGuiInterfaces#draw(java.awt.Graphics2D)
	 */
	@Override
	public void draw(Graphics2D g, boolean gradingView) {
		g.setColor(Color.YELLOW);
		g.fillRect(xPos, yPos, CustomerWidth, CustomerHeight);
		
		g.drawString(currentTask, xPos, yPos);
		g.drawString("Cash on hand: " + currentMoney, xPos + 15, yPos + 10);
		
		for (int i=0;i<agent.getShoppingList().size();i++){
			if (currentTask != "Leaving Market" && currentTask != "AtExit"){
			//g.drawString("Inventory has : " + agent.getShoppingList().get(i).name + "  " + agent.getPerson().getInventory().get(agent.getShoppingList().get(i).name), xPos + 15, yPos + 20 + i*10);
			g.drawString("Going To Buy : " + agent.getShoppingList().get(i).name + "  " + agent.getShoppingList().get(i).amount, xPos + 15, yPos + 20 + i*10);
			}
		}
		
		for (int i=0;i<agent.getPerson().getInventory().size();i++){
			if (currentTask == "Leaving Market" && currentTask != "AtExit" ){
				g.drawString("Inventory has : " + agent.getShoppingList().get(i).name + "  " + agent.getPerson().getInventory().get(agent.getShoppingList().get(i).name), xPos + 15, yPos + 20 + i*10);
			}
		}

	}


    
	/* (non-Javadoc)
	 * @see market.gui.CustomerGuiInterfaces#isPresent()
	 */
	@Override
	public boolean isPresent() {
		return isPresent;
	}
	
	/* (non-Javadoc)
	 * @see market.gui.CustomerGuiInterfaces#setBuying()
	 */
	@Override
	public void setBuying() {
		isBuying = true;
		agent.goingToBuy();
		setPresent(true);
		
	}
	
	
	/* (non-Javadoc)
	 * @see market.gui.CustomerGuiInterfaces#isBuying()
	 */
	@Override
	public boolean isBuying() {
		return isBuying;
	}

	/* (non-Javadoc)
	 * @see market.gui.CustomerGuiInterfaces#setPresent(boolean)
	 */
	@Override
	public void setPresent(boolean p) {
		isPresent = p;
	}

	/* (non-Javadoc)
	 * @see market.gui.CustomerGuiInterfaces#DoGoToFrontDesk()
	 */
	@Override
	public void DoGoToFrontDesk(){
		xDestination = xFrontDesk;
        yDestination = yFrontDesk;
		command = Command.GoToCashier;
		currentTask = "GoingToFrontDesk";
		currentMoney = agent.getCash();
	}
	
	
	/* (non-Javadoc)
	 * @see market.gui.CustomerGuiInterfaces#DoExitMarket()
	 */
	@Override
	public void DoExitMarket() {
		xDestination = 220;
		yDestination = -20;
		command = Command.LeaveMarket;
		currentTask = "Leaving Market";
		currentMoney = agent.getCash();
	}
}
