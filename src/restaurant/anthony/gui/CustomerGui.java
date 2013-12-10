package restaurant.anthony.gui;

import restaurant.anthony.HostRole;
import restaurant.anthony.interfaces.Customer;

import java.awt.*;
import java.util.List;

import javax.swing.JButton;

import agent.gui.Gui;

public class CustomerGui implements Gui{

	private Customer agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	
	private String Food = null;
	
	public static final int OffScreenX = 50;
	public static final int OffScreenY = -50;
	
	public static final int WaitingLineX = 50;
	public static final int WaitingLineY = 200;
	
	public static final int CustomerWidth = 15;
	public static final int CustomerHeight = 15;
	
	public static final int xCashier = 410;
    public static final int yCashier = 160;
    
    public static final int xTable1 = 160;
    public static final int yTable1 = 80;
    
    public static final int xTable2 = 260;
    public static final int yTable2 = 80;
    
    public static final int xTable3 = 360;
    public static final int yTable3 = 80;
    
    public static final int xTable1Cont = 200;
    public static final int yTable1Cont = 105;
    
    public static final int xTable2Cont = 300;
    public static final int yTable2Cont = 105;
    
    public static final int xTable3Cont = 400;
    public static final int yTable3Cont = 105;

    public static final int xExit1 = 410;
    public static final int yExit1 = 65;
    
    public static final int xExit = 260;
    public static final int yExit = -50;
    
    
	//private HostAgent host;

	private int seatNumber= 0;
	private int xPos, yPos;
	private int inLineNumber = 0;
	private int AssignedSpaceNumber = 0;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, GoToSeat1, LeaveRestaurant, LeaveRestaurant1, GoToCashier, GoToCashier1, GoToWaitingLine};
	private Command command=Command.noCommand;

	private int Xlist[];
	private int Ylist[];


	public CustomerGui(Customer c){ //HostAgent m) {
		agent = c;
		xPos = OffScreenX;
		yPos = OffScreenY;
		xDestination = OffScreenX;
		yDestination = OffScreenY;
		//maitreD = m;
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
			if (command==Command.GoToSeat1){
				ContinueToSeat(seatNumber);
				return;
			}
			else if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.GoToCashier1){
				ContinueToCashier();
				return;
			}
			else if (command == Command.GoToCashier){
				agent.msgAnimationFinishedGoToCashier();
			}
			else if (command==Command.LeaveRestaurant1){
				ContinueToExitRestaurant();
				return;
			}
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
			}
			else if (command == Command.GoToWaitingLine) {
				agent.atWaitingLine();
			}
			
			command=Command.noCommand;
		}
	}
 
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, CustomerWidth, CustomerHeight);
		if (Food == null){
			g.drawString("?", xPos, yPos);
		}
		if (Food != null){
			g.drawString(Food, xPos, yPos);
		}
	}

	public void Food(String s){
    	Food = s;
    }
    
    public void FinishFood(){
    	Food = "!!";
    }
    
    public void FinishCheck(){
    	Food = null;
    }
    
	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		System.out.print("SetHungry?");
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

	public void DoGoToCashier(){
		if (seatNumber == 1){
			xDestination = xTable1Cont;
			yDestination = yTable1Cont;
		}
		if (seatNumber == 2){
			xDestination = xTable2Cont;
			yDestination = yTable2Cont;
		}
		if (seatNumber == 3){
			xDestination = xTable3Cont;
			yDestination = yTable3Cont;
		}
		command = Command.GoToCashier1;
	}
	
	public void ContinueToCashier(){
		xDestination = xCashier;
        yDestination = yCashier;
		command = Command.GoToCashier;
	}
	
	public void GoToWaitingLine(){
		xDestination = 10;
		yDestination = WaitingLineY/2;
		command = Command.GoToWaitingLine;
	}
	
	public void GoToAssignedSpace(int x){
		xDestination = WaitingLineX + 20 - x*20;
		yDestination = WaitingLineY + 20;
	}
	
	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		command = Command.GoToSeat1;
		seatNumber = seatnumber;
		if (seatnumber == 1){
		xDestination = xTable1Cont;
		yDestination = yTable1Cont;
		}
		if (seatnumber == 2){
			xDestination = xTable2Cont;
			yDestination = yTable2Cont;
		}
		if (seatnumber == 3){
			xDestination = xTable3Cont;
			yDestination = yTable3Cont;
		}
	}
	
	public void ContinueToSeat(int seatnumber){
		command = Command.GoToSeat;
		if (seatnumber == 1){
		xDestination = xTable1;
		yDestination = yTable1;
		}
		if (seatnumber == 2){
			xDestination = xTable2;
			yDestination = yTable2;
		}
		if (seatnumber == 3){
			xDestination = xTable3;
			yDestination = yTable3;
		}
	}

	public void InLine (int x){
		inLineNumber = x;
		
		xDestination = WaitingLineX;
		yDestination = WaitingLineY - 20*x;
	}
	
	public void DoExitRestaurant() {
		xDestination = xExit1;
		yDestination = yExit1;
		command = Command.LeaveRestaurant1;
	}
	
	public void ContinueToExitRestaurant() {
		xDestination = xExit;
		yDestination = yExit;
		command = Command.LeaveRestaurant;
	}
}
