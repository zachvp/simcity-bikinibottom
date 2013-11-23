package bank.gui;


import java.awt.*;
import java.util.concurrent.Semaphore;

import bank.gui.Gui;
import bank.BankCustomerRole;


public class BankCustomerGui implements Gui{

	private BankCustomerRole agent = null;
	private boolean isPresent = true;
	private boolean isHungry = false;

	//private HostAgent host;
	BankGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;
	
	String choiceDisplay = "";
	
	private Semaphore active = new Semaphore(0);

	public static final int xTable = 200;
	public static final int yTable = 250;
	public static final int agentDim = 20;
	
	String name;
	double money = -1;
	double cashInAccount = -1;
	
	int entranceX = 275;
	int entranceY = 320;
	
	int guardX =322;
	int guardY = 360;
	
	boolean canRelease = false;

	public BankCustomerGui(BankCustomerRole c){ //HostAgent m) {
		agent = c;
		name = c.getName();
		xPos = entranceX;
		yPos = entranceY;
		xDestination = entranceX;
		yDestination = entranceY;
		c.msgGotToTeller();
	
//		DoGoToTeller(200);
		//maitreD = m;
//		this.gui = gui;
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
			if(canRelease) {
				atDestination();
			}
			
			}
			command=Command.noCommand;
		}
	

	public void draw(Graphics2D g) {//abstract definition, needed for Graphics
		
		g.setColor(Color.RED);
		g.fillRect(xPos, yPos, agentDim, agentDim);
		
		drawLetter(g, name, money, cashInAccount);
				

	}
	
	
	public void drawLetter(Graphics2D g, String name, double money, double cashInAccount) {
		g.setColor(Color.black);
		g.drawString("name: " + name, xPos+20, yPos);
		g.drawString("money: " + money, xPos+20, yPos+10);
		g.drawString("Account amount: " + cashInAccount, xPos+20, yPos + 20);
	}

	public boolean isPresent() {
		return isPresent;
	}


	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoGoToTeller(int xLoc, double money, double account) {
		canRelease = true;
		this.money = money;
		this.cashInAccount = account;
		xDestination = xLoc;
		yDestination = 170;
	}
	
	public void DoLeaveBank(double money, double account) {
		canRelease = true;
		this.money = money;
		this.cashInAccount = account;
		xDestination = 500;
		yDestination = 500;
	}
	
	public void DoGoToLoanManager(int x, double money, double account) {
		canRelease = true;
		this.money = money;
		this.cashInAccount = account;
		xDestination = x;
	}

	public void DoGoToSecurityGuard(double money, double account) {
		canRelease = true;
		this.money = money;
		this.cashInAccount = account;
		xDestination = guardX;
		yDestination = guardY;
	}
	
    public void DoEndWorkDay(){
    	canRelease = true;
    	xDestination = 300;
    	yDestination = 500;
    }
	
    public void atDestination() {
//    	System.out.println("Made it");
    	canRelease = false;
    	agent.msgAtDestination();
    	
    }
    

	
}

