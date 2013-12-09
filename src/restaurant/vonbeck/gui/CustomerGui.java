package restaurant.vonbeck.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import restaurant.vonbeck.CustomerRole;
import agent.gui.Gui;

public class CustomerGui implements Gui{

	private CustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	RestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, GoToCashier, LeaveRestaurant, GoToWaitZone};
	private Command command=Command.noCommand;
	private static final int HOME_POS_X = 55;
	private static final int HOME_POS_Y = 55;
	private static final int CUSTOMER_INIT_POS_X = -40;
	private static final int CUSTOMER_INIT_POS_Y = -40;
	private static final int CUSTOMERW = 20;
	private static final int CUSTOMERH = 20;
    private static final int CASH_POS_X = 85;
    private static final int CASH_POS_Y = 380;
    private static final int xTable = LayoutGui.TABLEX;
	private static final int yTable = LayoutGui.TABLEY
								+ LayoutGui.TABLEH - CUSTOMERH;
	
	private String foodLabel = "";
	
	public CustomerGui(CustomerRole c, RestaurantGui gui){ 
		agent = c;
		setxPos(CUSTOMER_INIT_POS_X);
		setyPos(CUSTOMER_INIT_POS_Y);
		xDestination = CUSTOMER_INIT_POS_X;
		yDestination = CUSTOMER_INIT_POS_Y;
		this.gui = gui;
	}

	public void updatePosition() {
		if (getxPos() < xDestination)
			setxPos(getxPos() + 1);
		else if (getxPos() > xDestination)
			setxPos(getxPos() - 1);

		if (getyPos() < yDestination)
			setyPos(getyPos() + 1);
		else if (getyPos() > yDestination)
			setyPos(getyPos() - 1);

		if (getxPos() == xDestination && getyPos() == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				gui.setCustomerEnabled(agent);
			} else if (command==Command.GoToCashier) {
				agent.msgAnimationFinishedGoToCashier();
			} 
			command=Command.noCommand;
		}
	}
	

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(getxPos(), getyPos(), CUSTOMERW, CUSTOMERH);
		//foodLabel.setLocation(xPos, yPos-10);
		g.setColor(Color.BLACK);
		g.drawString(foodLabel,xPos,yPos-5);
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat(int tableNumber) {//later you will map seatnumber to table coordinates.
		xDestination = xTable + (tableNumber-1)*75;
		yDestination = yTable;
		command = Command.GoToSeat;
	}

	public void DoGoToCashier() {
		xDestination = CASH_POS_X;
		yDestination = CASH_POS_Y;
		command = Command.GoToCashier;
	}
	
	public void DoGoToWaitZone() {
		xDestination = (int) (HOME_POS_X + Math.random()*70);
		yDestination = (int) (HOME_POS_Y + Math.random()*70);
		//command = Command.GoToWaitZone;
	}

	public void DoExitRestaurant() {
		xDestination = CUSTOMER_INIT_POS_X;
		yDestination = CUSTOMER_INIT_POS_Y;
		command = Command.LeaveRestaurant;
	}
	
	public void DoDisplayFood(String food, Boolean isOrder) {
		foodLabel = new String(food);
		if (isOrder) foodLabel = foodLabel + "?";
		
	}
	
	public void DoClearFood() {
		foodLabel="";
	}

	/**
	 * @return the xPos
	 */
	public int getxPos() {
		return xPos;
	}

	/**
	 * @param xPos the xPos to set
	 */
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	/**
	 * @return the yPos
	 */
	public int getyPos() {
		return yPos;
	}

	/**
	 * @param yPos the yPos to set
	 */
	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

}
