package restaurant.anthony.gui;


import restaurant.anthony.interfaces.Customer;
import restaurant.anthony.interfaces.Waiter;

import java.awt.*;

import agent.gui.Gui;

public class WaiterGui implements Gui {

    private Waiter agent = null;
    
    private int myNumber = -1;
    
    private int idlePositionX = 150;
    private int idlePositionY = 150;

    private int xPos = 50, yPos = -50;//default waiter position
    private int xDestination = 50, yDestination = -50;//default start position
    
    public static final int ExitX = 50;
	public static final int ExitY = -50;
	
	public  int ExitX1 = 50;
	public  int ExitY1 = 150;



    private String Serving=null;
    
    public static final int xTable1 = 180;
    public static final int yTable1 = 60;
    
    public static final int xTable2 = 280;
    public static final int yTable2 = 60;
    
    public static final int xTable3 = 380;
    public static final int yTable3 = 60;
    
    
    public static final int xTable1Cont = 180;
    public static final int yTable1Cont = 105;
    
    public static final int xTable2Cont = 280;
    public static final int yTable2Cont = 105;
    
    public static final int xTable3Cont = 380;
    public static final int yTable3Cont = 105;
    
    
    public int xCook =203;
    public int yCook =220;

    public static final int xCashier = 410;
    public static final int yCashier = 160;
    
    private int WaitingLineX = 80;
    private int WaitingLineY = 200;
    
    private static final int WaiterWidth = 15;
    private static final int WaiterHeight = 15;
    
    private enum Command {NotAtWork, GoToWork1, GoToWork, noCommand, GoToIdle, GoToIdle1, GoToTable, GoToTable1, GoToCook, GoToCook1, GoToHost, GoToCashier, GoToExit, GoToExit1};
    private Command command = Command.NotAtWork;
    
    public WaiterGui(Waiter agent, int x) {
        this.agent = agent;
        SetWaiterNumber(x);
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
        
        if ((xPos == xDestination && yPos == yDestination) && command != Command.noCommand) {
			
        	if (command == Command.GoToExit1){
        		ContinueToExit();
        		return;
        	}
        	else if (command == Command.GoToExit){
        		agent.AtExit();
        	}
        	else if (command == Command.GoToWork1){
        		ContinueToWork();
        		return;
        	}
        	else if (command == Command.GoToWork){
        		agent.msgAtHome();
        	}
        	else if (command == Command.GoToTable1){
        		if (xPos == xTable1Cont && yPos == yTable1Cont) {
            		ContinueToTable(1);
            		return;
            	}
        		if (xPos == xTable2Cont && yPos == yTable2Cont) {
        			ContinueToTable(2);
            		return;
            	}

        		if (xPos == xTable3Cont && yPos == yTable3Cont) {
        			ContinueToTable(3);
            		return;
        		}
        	}
        	else if (command == Command.GoToIdle1){
        		if (xPos == xTable1Cont && yPos == yTable1Cont) {
            		ContinueToIdle();
            		return;
            	}
        		if (xPos == xTable2Cont && yPos == yTable2Cont) {
        			ContinueToIdle();
            		return;
            	}

        		if (xPos == xTable3Cont && yPos == yTable3Cont) {
        			ContinueToIdle();
            		return;
        		}
        	}
        	else if (command == Command.GoToCook1){
        		if (xPos == xTable1Cont && yPos == yTable1Cont) {
            		GoToCook();
            		return;
            	}
        		if (xPos == xTable2Cont && yPos == yTable2Cont) {
        			GoToCook();
            		return;
            	}

        		if (xPos == xTable3Cont && yPos == yTable3Cont) {
        			GoToCook();
            		return;
        		}
        	}
        	
        	else if (command == Command.GoToTable){
        		if (xPos == xTable1 && yPos == yTable1) {
            		agent.msgAtTable(1);
            		return;
            	}
        		if (xPos == xTable2 && yPos == yTable2) {
            		agent.msgAtTable(2);
            		return;
            	}

        		if (xPos == xTable3 && yPos == yTable3) {
            		agent.msgAtTable(3);
            		return;
        		}
        	}
        	else if (command == Command.GoToCook){
        		if (xPos == xCook && yPos == yCook) {
            		agent.msgAtCook();
            		return;
            	}
        	}
        	else if (command == Command.GoToCashier){
        		if (xPos == xCashier && yPos == yCashier) {
            		agent.msgAtCashier();
            		return;
            	}
        	}
        	else if(command == Command.GoToHost){
        		if (xPos == WaitingLineX && yPos == WaitingLineY) {
            		agent.msgAtWaitingLine();
            		return;
            	}
        	}
        	else if (command == Command.GoToIdle){
        		if (xPos == idlePositionX && yPos == idlePositionY){
            		agent.msgIdle();
            		return;
            	}
        	}
        	
        	command=Command.noCommand;
			
		}
        
       
    }

    public void draw(Graphics2D g, boolean gradingView) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, WaiterWidth, WaiterHeight);
        if (Serving != null){
        	g.drawString(Serving, xPos, yPos);
        }
    }
    
    public void ServeOrder(String s){
    	Serving = s;
    }
    
    public void DoneServing(){
    	Serving = null;
    }

    public boolean isPresent() {
        return true;
    }
    
    public void GoToWork(){
    	command=Command.GoToWork1;
    	xDestination = ExitX1;
    	yDestination = ExitY1;
    }
    
    public void ContinueToWork(){
    	command = Command.GoToWork;
    	xDestination = idlePositionX;
        yDestination = idlePositionY;
    }
    
    public void DoBringToTable(Customer customer, int i) {
    	command = Command.GoToTable1;
    	if (i == 1){
        xDestination = xTable1Cont;
        yDestination = yTable1Cont;
    	}
    	if (i == 2){
            xDestination = xTable2Cont;
            yDestination = yTable2Cont;
        	}
    	if (i == 3){
            xDestination = xTable3Cont;
            yDestination = yTable3Cont;
        	}
    }
    
    public void GoToCookFromTable(int i){
    	command = Command.GoToCook1;
    	if (i == 1){
        xDestination = xTable1Cont;
        yDestination = yTable1Cont;
    	}
    	if (i == 2){
            xDestination = xTable2Cont;
            yDestination = yTable2Cont;
        	}
    	if (i == 3){
            xDestination = xTable3Cont;
            yDestination = yTable3Cont;
        	}
    }
    
    public void GoToCook(){
    	command = Command.GoToCook;
    	xDestination = xCook;
        yDestination = yCook;
    }

    
    public void GoToWaitingLine(){
    	command = Command.GoToHost;
    	xDestination = WaitingLineX;
        yDestination = WaitingLineY;
    }
    
    public void GoToTable (int i){
    	command = Command.GoToTable1;
    	if (i == 1){
            xDestination = xTable1Cont;
            yDestination = yTable1Cont;
        	}
        if (i == 2){
            xDestination = xTable2Cont;
            yDestination = yTable2Cont;
           	}
       	if (i == 3){
            xDestination = xTable3Cont;
            yDestination = yTable3Cont;
            }
    }
    
    public void ContinueToTable (int i){
    	command = Command.GoToTable;
    	if (i == 1){
            xDestination = xTable1;
            yDestination = yTable1;
        	}
        if (i == 2){
            xDestination = xTable2;
            yDestination = yTable2;
           	}
       	if (i == 3){
            xDestination = xTable3;
            yDestination = yTable3;
            }
    }
    
    public void GoToCashier(){
    	command = Command.GoToCashier;
    	xDestination = xCashier;
        yDestination = yCashier;
    }
    
    public void ContinueToIdle(){
    	command = Command.GoToIdle;
    	xDestination = idlePositionX;
        yDestination = idlePositionY;
    }
    
    public void DoLeaveCustomer(int i) {
    	command = Command.GoToIdle1;
    	if (i == 1){
            xDestination = xTable1Cont;
            yDestination = yTable1Cont;
        	}
        if (i == 2){
            xDestination = xTable2Cont;
            yDestination = yTable2Cont;
           	}
       	if (i == 3){
            xDestination = xTable3Cont;
            yDestination = yTable3Cont;
            }
    }

    public void DoLeaveCook() {
    	command = Command.GoToIdle;
        xDestination = idlePositionX;
        yDestination = idlePositionY;
    }
    
    public void DoLeaveCashier() {
    	command = Command.GoToIdle;
    	 xDestination = idlePositionX;
         yDestination = idlePositionY;
    }
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void SetWaiterNumber (int x){
    	myNumber = x;
    	
    			
    	xCook = xCook + x*72;
    	//yCook = yCook + x*100;
    	idlePositionX = idlePositionX + x*100;
    	//idlePositionY = idlePositionY ;
    }

	public void GoToExit() {
		command = Command.GoToExit1;
		xDestination = ExitX1;
        yDestination = ExitY1;
	}
	
	public void ContinueToExit(){
		command = Command.GoToExit;
		xDestination = ExitX;
        yDestination = ExitY;
	}
}
