package restaurant.anthony.gui;


import restaurant.anthony.CustomerRole;
import restaurant.anthony.HostRole;
import restaurant.anthony.interfaces.Cook;

import java.awt.*;
import java.util.ArrayList;

import agent.gui.Gui;

public class CookGui implements Gui {

    private Cook agent = null;

    private int xPos = 50, yPos = -50;//default waiter position
    
    private static int xDestination = 50;//default start position
	private static int yDestination = -50;
	
	public static final int ExitX = 50;
	public static final int ExitY = -50;
	
	public static final int ExitX1 = 430;
	public static final int ExitY1 = 60;
	
	public static final int ExitX2 = 430;
	public static final int ExitY2 = 280;
	
	private static int idlePositionX = 270;
	private static int idlePositionY = 280;
    
    private static final int CookWidth = 10;
    private static final int CookHeight = 10;
    
    private static final int xFridge = 180;
    private static final int yFridge = 280;
    
    private static final int xStove1 = 203;
    private static final int yStove1 = 300;
    
    private static final int xStove2 = 275;
    private static final int yStove2 = 300;
    
    private static final int xStove3 = 347;
    private static final int yStove3 = 300;
    
    private static final int xPlatingArea1 = 203;
    private static final int yPlatingArea1 = 270;
    
    private static final int xPlatingArea2 = 275;
    private static final int yPlatingArea2 = 270;
    
    private static final int xPlatingArea3 = 347;
    private static final int yPlatingArea3 = 270;
    
    private enum Command {NotAtWork, Idle, GoToWork, GoToWork1, GoToWork2, GoToExit1, GoToExit2, GoToExit, NoCommand};
    private Command command = Command.NotAtWork;

    private static ArrayList<Boolean> Stoves = new ArrayList<Boolean>();
    {
    Stoves.add(false); // add a value;
    Stoves.add(false);
    Stoves.add(false);
    }
    
    private static ArrayList<Boolean> Plates = new ArrayList<Boolean>();
    {
    Plates.add(false); // add a value;
    Plates.add(false);
    Plates.add(false);
    }
    
    public CookGui(Cook agent) {
        this.agent = agent;
    }
    
    public void GoToWork(){
    	command=Command.GoToWork1;
    	xDestination = ExitX1;
    	yDestination = ExitY1;
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
        	
        	if (command == Command.GoToExit1){
        		ContinueToExit();
        		return;
        	}
        	else if (command == Command.GoToExit2){
        		ContinueToExit1();
        		return;
        	}
        	else if (command == Command.GoToExit){
        		agent.atExit();
        	}

        	else if (command == Command.GoToWork1){
        		ContinueToWork();
        		return;
        	}
        	else if (command == Command.GoToWork2){
        		ContinueToWork1();
        		return;
        	}
        	else if (command == Command.GoToWork){
        		agent.msgAtHome();
        	}
        
        if ((xDestination == xFridge) & (yDestination == yFridge)) {
           agent.msgAtFridge();
        }
        
        if ((xDestination == xStove1) & (yDestination == yStove1)) {
           agent.msgAtStove(1);
        }
        
        if ((xDestination == xStove2) & (yDestination == yStove2)) {
           agent.msgAtStove(2);
        }
        
        if ((xDestination == xStove3) & (yDestination == yStove3)) {
           agent.msgAtStove(3);
        }
        if ((xDestination == xPlatingArea1) & (yDestination == yPlatingArea1)) {
           agent.msgAtPlatingArea(1);
        }
        
        if ((xDestination == xPlatingArea2) & (yDestination == yPlatingArea2)) {
           agent.msgAtPlatingArea(2);
        }
        
        if ((xDestination == xPlatingArea3) & (yDestination == yPlatingArea3)) {
           agent.msgAtPlatingArea(3);
        }
        
        if ((xDestination == idlePositionX) & (yDestination == idlePositionY)) {
           agent.msgAtHome();
        }
        command = Command.NoCommand;
        }
        
        

    }
    public void ContinueToWork(){
    	command = Command.GoToWork2;
    	xDestination = ExitX2;
    	yDestination = ExitY2;
    }
    public void ContinueToWork1(){
    	command = Command.GoToWork;
    	xDestination = idlePositionX;
    	yDestination = idlePositionY;
    }

    public   void GoToFridge(){
    	xDestination = xFridge;
        yDestination = yFridge;
    }
    
    public   void GoToStove(int i){
    	if (i == 0){
            xDestination = xStove1;  //110
            yDestination = yStove1;
        	}
        	if (i == 1){
                xDestination = xStove2; //180
                yDestination = yStove2;
            	}
        	if (i == 2){
                xDestination = xStove3; //250
                yDestination = yStove3;
            	}
    }
    
    public   void GoToPlateArea(int i){
    	if (i == 0){
            xDestination = xPlatingArea1;  //110
            yDestination = yPlatingArea1;
        	}
        	if (i == 1){
                xDestination = xPlatingArea2; //180
                yDestination = yPlatingArea2;
            	}
        	if (i == 2){
                xDestination = xPlatingArea3; //250
                yDestination = yPlatingArea3;
            	}
    }
    
    public   void GoToHomePosition(){
    	xDestination = idlePositionX;
        yDestination = idlePositionY;
    }
    
    public   void AddFood (int numb, String type){
    	if (type == "Stove"){
    		for (int i=0;i<Stoves.size();i++){
    			if (numb == i){
    				Stoves.set(i,true);
    			}
    		}
    	}
    	
    	if (type == "PlatingArea"){
    		for (int i=0;i<Plates.size();i++){
    			if (numb == i){
    				Plates.set(i,true);
    			}
    		}
    	}
    }
    
    public   void RemoveFood (int numb, String type){
    	if (type == "Stove"){
    		for (int i=0;i<Stoves.size();i++){
    			if (numb == i){
    				Stoves.set(i,false);
    			}
    		}
    	}
    	if (type == "PlatingArea"){
    		for (int i=0;i<Plates.size();i++){
    			if (numb == i){
    				Plates.set(i,false);
    			}
    		}
    	}
    }
    
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(xPos, yPos, CookWidth, CookHeight);
        
        for (int i=0;i<Stoves.size();i++){
        	if (Stoves.get(i)){
        		g.setColor(Color.white);
        		if (i==0)
        			g.fillRect(xStove1, yStove1 + 15, 10, 10);
        		if (i==1)
        			g.fillRect(xStove2, yStove2 + 15, 10, 10);
        		if (i==2)
        			g.fillRect(xStove3, yStove3 + 15, 10, 10);
        	}
        }
        
        for (int i=0;i<Plates.size();i++){
        	if (Plates.get(i)){
        		g.setColor(Color.white);
        		if (i==0)
        			g.fillRect(xPlatingArea1, yPlatingArea1 - 25, 10, 10);
        		if (i==1)
        			g.fillRect(xPlatingArea2, yPlatingArea2 - 25, 10, 10);
        		if (i==2)
        			g.fillRect(xPlatingArea3, yPlatingArea3 - 25, 10, 10);
        	}
        }
    }

    public boolean isPresent() {
        return true;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

	public void GoToExit() {
		command = Command.GoToExit1;
    	xDestination = ExitX2;
    	yDestination = ExitY2;
	}
	
	public void ContinueToExit() {
		command = Command.GoToExit2;
    	xDestination = ExitX1;
    	yDestination = ExitY1;
	}
	
	public void ContinueToExit1() {
		command = Command.GoToExit;
    	xDestination = ExitX;
    	yDestination = ExitY;
	}
}
