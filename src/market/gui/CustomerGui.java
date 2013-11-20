package market.gui;

import market.CashierAgent;
import market.interfaces.Customer;

import java.awt.*;
import java.util.List;

import javax.swing.JButton;

public class CustomerGui implements Gui{

	private Customer agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	
	public static final int OffScreenX = 340;
	public static final int OffScreenY = 50;
	public static final int CustomerWidth = 20;
	public static final int CustomerHeight = 20;
	
	public static final int xCashier =-40;
    public static final int yCashier =-40;

	//private HostAgent host;
	MarketGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant, GoToCashier};
	private Command command=Command.noCommand;

	private int Xlist[];
	private int Ylist[];
	public static final int xTable = 50;
	public static final int yTable = 50;

	public CustomerGui(Customer c, MarketGui gui){ //HostAgent m) {
		agent = c;
		xPos = OffScreenX;
		yPos = OffScreenY;
		xDestination = OffScreenX;
		yDestination = OffScreenY;
		//maitreD = m;
		this.gui = gui;
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

		/*
		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command == Command.GoToCashier){
				agent.msgAnimationFinishedGoToCashier();
			}
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			
			
			command=Command.noCommand;
		}
		*/
	}
 
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, CustomerWidth, CustomerHeight);

	}


    
	public boolean isPresent() {
		return isPresent;
	}
	/*
	public void setHungry() {
		System.out.print("SetHungry?");
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	*/
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToCashier(){
		xDestination = xCashier;
        yDestination = yCashier;
		command = Command.GoToCashier;
	}
	
	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		if (seatnumber == 1){
		xDestination = xTable;
		yDestination = yTable;
		command = Command.GoToSeat;
		}
		if (seatnumber == 2){
			xDestination = xTable+100;
			yDestination = yTable;
			command = Command.GoToSeat;
		}
		if (seatnumber == 3){
			xDestination = xTable+200;
			yDestination = yTable;
			command = Command.GoToSeat;
		}
	}

	
	public void DoExitRestaurant() {
		xDestination = 340;
		yDestination = -20;
		command = Command.LeaveRestaurant;
	}
}
