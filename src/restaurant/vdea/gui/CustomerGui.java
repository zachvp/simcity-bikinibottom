package restaurant.vdea.gui;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import agent.gui.Gui;
import restaurant.vdea.CustomerRole;
import restaurant.vdea.WaiterRole;

public class CustomerGui implements Gui{

	private CustomerRole role = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	private WaiterRole waiter;
	RestaurantGui gui;

	private int xPos, yPos, sXPos, sYPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;
	
	boolean ordered, recieved;
	String myChoice;
	

	public static int xTable = 150;
	public static int yTable = 150;

	public CustomerGui(CustomerRole c, RestaurantGui gui, WaiterRole m) {
		role = c;
		xPos = -40;
		yPos = -40;
		
		sXPos = -40;
		sYPos = -40;
		
		xDestination = -40;
		yDestination = -40;
		//maitreD = m;
		this.gui = gui;
		
		waiter = m;
	}
	
	public CustomerGui(CustomerRole c, RestaurantGui gui){
		role = c;
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
			if (command==Command.GoToSeat) role.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				role.msgAnimationFinishedLeaveRestaurant();
				//System.out.println("about to call gui.setCustomerEnabled(Role);");
				isHungry = false;
				//gui.setCustomerEnabled(role); //enables CB?
			}
			command=Command.noCommand;
		}
		
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);

		if(ordered){
			g.setColor(Color.BLACK);
			if(myChoice.equals("steak")){
				g.drawString("ST?", xPos+22, yPos+10);
			}
			if(myChoice.equals("chicken")){
				g.drawString("CH?", xPos+22, yPos+10);
			}
			if(myChoice.equals("salad")){
				g.drawString("SAL?", xPos+22, yPos+10);
			}
			if(myChoice.equals("pizza")){
				g.drawString("PZ?", xPos+22, yPos+10);
			}
			if(myChoice.equals("nothing")){
				g.drawString("", xPos+22, yPos+10);
			}
		}
		
		if(recieved){
			g.setColor(Color.BLACK);
    		if(myChoice.equals("steak")){
    			g.drawString("ST", xPos+22, yPos+10);
    		}
    		if(myChoice.equals("chicken")){
    			g.drawString("CH", xPos+22, yPos+10);
    		}
    		if(myChoice.equals("salad")){
    			g.drawString("SAL", xPos+22, yPos+10);
    		}
    		if(myChoice.equals("pizza")){
    			g.drawString("PZ", xPos+22, yPos+10);
    		}
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
	
	public void ordering(String c){
		ordered = true;
		myChoice = c;
	}
	
	public void gotFood(){
		ordered = false;
		recieved = true;
	}
	
	public int getPosX(){
		return xPos;
	}
	public int getPosY(){
		return yPos;
	}
	
	public void DoGoToWaitingArea(int n){
		yDestination = 50 + (21*n);
		xDestination = 15;
		xPos = xDestination;
		yPos = yDestination;
	}
	
	public void updateWait(int n){
		yDestination = 50 + (21*n);
		xDestination = 15;
	}
	
	public void DoGoToSeat(int seatnumber, int tableNumber) {//later you will map seatnumber to table coordinates.
		
		if (tableNumber == 1){
    		xTable = 150;
    	    yTable = 150;
    	}
    	if (tableNumber == 2)
    	{
    		xTable = 250;
    		yTable = 150;
    	}
    	if (tableNumber ==3){
    		xTable = 150;
    		yTable = 250;
    	}
    	if (tableNumber ==4){
    		xTable = 250;
    		yTable = 250;
    	}
		
		xDestination = xTable;
		yDestination = yTable;
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		recieved = false;
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
}
