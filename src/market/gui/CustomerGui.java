package market.gui;

import market.CashierRole;
import market.interfaces.Customer;

import java.awt.*;
import java.util.List;

import javax.swing.JButton;

public class CustomerGui implements Gui{

	private Customer agent = null;
	private boolean isPresent = false;
	private boolean isBuying = false;
	
	public static final int OffScreenX = 180;
	public static final int OffScreenY = 50;
	public static final int CustomerWidth = 15;
	public static final int CustomerHeight = 15;
	
<<<<<<< HEAD
	public static final int xFrontDesk =170;
    public static final int yFrontDesk =210;
=======
	public static final int xFrontDesk =180;
    public static final int yFrontDesk =200;
>>>>>>> master

	MarketGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToCashier, LeaveMarket};
	private Command command=Command.noCommand;

	public static final int xTable = 50;
	public static final int yTable = 50;

	public CustomerGui(Customer c){ //HostAgent m) {
		agent = c;
		xPos = OffScreenX;
		yPos = OffScreenY;
		xDestination = OffScreenX;
		yDestination = OffScreenY;
		//maitreD = m;
		//this.gui = gui;
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
				agent.msgAnimationFinishedGoToCashier();
			else if (command==Command.LeaveMarket) {
				agent.msgAnimationFinishedLeaveMarket();
				isBuying = false;
			}
			
			
			command=Command.noCommand;
		}
		
	}
 
	public void draw(Graphics2D g) {
		g.setColor(Color.YELLOW);
		g.fillRect(xPos, yPos, CustomerWidth, CustomerHeight);

	}


    
	public boolean isPresent() {
		return isPresent;
	}
	
	public void setBuying() {
<<<<<<< HEAD
		System.out.print("SetBuying?");
=======
>>>>>>> master
		isBuying = true;
		agent.goingToBuy();
		setPresent(true);
	}
	
	
	public boolean isBuying() {
		return isBuying;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToFrontDesk(){
		xDestination = xFrontDesk;
        yDestination = yFrontDesk;
		command = Command.GoToCashier;
	}
	
	
	public void DoExitMarket() {
		xDestination = 340;
		yDestination = -20;
		command = Command.LeaveMarket;
	}
}
