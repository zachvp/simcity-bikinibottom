package restaurant.vegaperk.gui;

import restaurant.vegaperk.backend.CustomerRole;

import java.awt.*;

import javax.swing.ImageIcon;

import agent.gui.Gui;

public class CustomerGui implements Gui {

	private CustomerRole agent = null;
	private boolean isPresent = true;
	private boolean isHungry = false;
	
	private String choice = "Test";
	public enum OrderState { NONE, DECIDED, SERVED };
	public OrderState orderState = OrderState.NONE;
	
    String imgSrc = "resource/gary.png";
    Image img = new ImageIcon(imgSrc).getImage();

	RestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private static final int width = 20;
	private static final int height = 20;
	
	private enum Command {noCommand, GoToSeat, LeaveRestaurant, GoWait};
	private Command command=Command.noCommand;

	private static final int outRestaurantX = -40;
	private static final int outRestaurantY = -40;
	
	public CustomerGui(CustomerRole c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
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

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g, boolean gradingView) {
		g.setColor(Color.BLUE);
    	g.fillRect(xPos, yPos, width, height);
    	g.drawImage(img, xPos, yPos, null);
    	if(orderState == OrderState.SERVED){
    		g.setColor(Color.BLACK);
    		g.drawString(choice, xPos+10, yPos+25);
    	}
    	else if(orderState == OrderState.DECIDED){
    		g.setColor(Color.BLACK);
    		g.drawString(choice+"?", xPos-15, yPos-5);
    	}
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void showLabel(){
		
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
	
	public void setChoice(String s){
		choice = s;
	}
	
	public void DoGoWait(int x, int y) {
		xDestination = x;
		yDestination = y;
		command = Command.GoWait;
	}
	
	public void DoGoToSeat(int x, int y) {//later you will map seatnumber to table coordinates.
		xDestination = x;
		yDestination = y;
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		xDestination = outRestaurantX;
		yDestination = outRestaurantY;
		orderState = OrderState.NONE;
		command = Command.LeaveRestaurant;
	}
	public CustomerRole getAgent(){
		return agent;
	}
}
