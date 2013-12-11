package restaurant.strottma.gui;

import restaurant.strottma.CustomerRole;
import restaurant.strottma.HostRole;

import java.awt.*;

import agent.gui.Gui;

public class CustomerGui implements Gui{

	private CustomerRole role = null;
	private boolean isPresent = true;
	private boolean isHungry = false;
	
	private String text = null; // the text to display

	//private HostAgent host;
	RestaurantGui gui;

	private int xPos, yPos, width, height;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant, WaitAtSpot};
	private Command command=Command.noCommand;

	public static final int xTable = 200;
	public static final int yTable = 250;
	public static final int WIDTH = 20;
	public static final int HEIGHT = 20;
	public static final int xOffScreen = -40;
	public static final int yOffScreen = -40;

	public CustomerGui(CustomerRole c, RestaurantGui gui){ //HostAgent m) {
		role = c;
		xPos = xOffScreen;
		yPos = yOffScreen;
		width = WIDTH;
		height = HEIGHT;
		xDestination = xOffScreen;
		yDestination = yOffScreen;
		//maitreD = m;
		this.gui = gui;
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos+=2;
		else if (xPos > xDestination)
			xPos-=2;

		if (yPos < yDestination)
			yPos+=2;
		else if (yPos > yDestination)
			yPos-=2;

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) role.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				role.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				//gui.setRoleEnabled(role);
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g, boolean gradingView) {
		// draw the agent
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, width, height);
		
		// draw the order
		if (text != null) {
			g.setColor(Color.BLACK);
			g.drawString(text, xPos + width, yPos + height);
		}
		
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		role.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat(int tableX, int tableY) {
		xDestination = tableX;
		yDestination = tableY;
		command = Command.GoToSeat;
	}
	
	public void DoWaitAtSpot(int wsX, int wsY) {
		xDestination = wsX;
		yDestination = wsY;
		command = Command.WaitAtSpot;
	}
	
	public void DoDecideOrder() {
		text = "...";
	}
	
	public void DoCallWaiter() {
		text = "!";
	}
	
	public void DoGiveOrder(String choice) {
		text = choice + "?";
	}
	
	public void DoEatFood(String choice) {
		text = choice;
	}

	public void DoExitRestaurant() {
		text = null;
		xDestination = xOffScreen;
		yDestination = yOffScreen;
		command = Command.LeaveRestaurant;
	}
}
