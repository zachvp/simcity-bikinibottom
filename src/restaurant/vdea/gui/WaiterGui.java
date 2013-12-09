package restaurant.vdea.gui;


import restaurant.CustomerAgent;
import restaurant.WaiterAgent;

import java.awt.*;

public class WaiterGui implements Gui {

	private WaiterAgent agent = null;
	RestaurantGui gui;
	private boolean isOnBreak = false;
	private boolean isPresent = false;

	private int xPos = -20, yPos = -20;//default waiter position
	private int xDestination = -20, yDestination = -20;//default start position
	private int xHome, yHome;

	private enum Command {noCommand, GoToSeat, LeaveRestaurant, WaitingArea, cust};
	private Command command=Command.noCommand;

	public int xTable = 150;
	public int yTable = 150;
	public int tableNumber = 1;

	boolean delivering;
	String order;


	public WaiterGui(WaiterAgent agent) {
		this.agent = agent;

	}

	public WaiterGui(WaiterAgent agent, RestaurantGui gui, int pos){
		this.agent = agent;
		this.gui = gui;

		xHome = 40 + (pos*21);
		yHome = 15;

		xPos = xHome;
		yPos = yHome;
		xDestination = xHome;
		yDestination = yHome;
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

		if (xPos == xDestination && yPos == yDestination
				&(xDestination == xTable + 20) & (yDestination == yTable - 20)) {
			agent.msgAtTable();
		}

		if (xPos == xDestination && yPos == yDestination){
			if (command == Command.cust){
				agent.atDest();
				agent.getHost().atDest();
				command = Command.noCommand;
			}
		}

		if (xPos == 455 && yPos== 80){
			agent.msgAtKitchen();
		}


		if(xPos == xHome && yPos== yHome && agent.isOnBreak()){
			isOnBreak = true;
		}

	}

	public void draw(Graphics2D g) {
		g.setColor(Color.MAGENTA);
		g.fillRect(xPos, yPos, 20, 20);
		
		g.setColor(Color.BLACK);
		if(isOnBreak){
			g.drawString("BR", xPos, yPos+10);
		}
		if(delivering){
			if(order.equals("steak")){
				g.drawString("ST", xPos, yPos);
			}
			if(order.equals("chicken")){
				g.drawString("CH", xPos, yPos);
			}
			if(order.equals("salad")){
				g.drawString("SAL", xPos, yPos);
			}
			if(order.equals("pizza")){
				g.drawString("PZ", xPos, yPos);
			}
		}
	}

	public boolean isPresent() {
		return true;
	}

	public void setOnBreak() {
		agent.goOnBreak(); //starts process of going On Break
		setPresent(true);
	}
	public void setOffBreak(){
		isOnBreak = false;
		agent.goOffBreak();
	}
	public boolean isOnBreak() {
		return isOnBreak;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void delivering(String food){
		delivering = true;
		order = food;
	}

	public void delivered(){
		delivering = false;
	}

	public void DoGoToCust(int x, int y){
		xDestination = x+20;
		yDestination = y+20;
		command=Command.cust;

	}

	public void DoGoToTable(int tblNum) {
		//agent.setBusy(true);
		tableNumber = tblNum;
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

		xDestination = xTable + 20;
		yDestination = yTable - 20;
	}



	public void DoGoToKitchen(){ //TODO add specific plate areas
		xDestination = 455;
		yDestination = 80;
	}

	public void DoServe(CustomerAgent customer, int tblNum){
		DoGoToTable(tblNum);
	}


	public void DoLeaveCustomer() {
		xDestination = xHome;
		yDestination = yHome;

	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}

	public void setXPos(int x){
		xPos = x;
	}
	public void setYPos(int y){
		yPos = y;
	}
}
