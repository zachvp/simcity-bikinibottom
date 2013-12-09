package restaurant.vegaperk.gui;

import restaurant.vegaperk.backend.CookRole;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agent.gui.Gui;

public class CookGui implements Gui{

	private CookRole agent = null;
	private boolean isPresent = true;
	private String holding = null;
	
	private List<Dimension> grillPositions = null;
	private List<Dimension> platePositions = null;
	
	/** Will store the items cooking at each grill */
	private Map<Integer, String> grillOrders =
			Collections.synchronizedMap(new HashMap<Integer, String>());
	private Map<Integer, String> plateOrders =
			Collections.synchronizedMap(new HashMap<Integer, String>());

	RestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private boolean canRelease = false;
	
	private static final int width = 20;
	private static final int height = 20;
	
	private static final int homeX = 400;
	private static final int homeY = 50;
	
	private static final int fridgeY = 200;	
	
	public CookGui(CookRole c, RestaurantGui gui){ //HostAgent m) {
		this.agent = c;
		this.gui = gui;
		
		xPos = homeX;
		yPos = homeY;
		xDestination = xPos;
		yDestination = yPos;
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

		if (xPos == xDestination && yPos == yDestination && canRelease) {
			canRelease = false;
			agent.msgAtDestination();
		}
	}

	public void draw(Graphics2D g) {
		g.drawString("Cook", 400, 40);
		
		g.setColor(Color.RED);
    	g.fillRect(xPos, yPos, width, height);
    	/** Draw the grills and plate positions */
    	g.setColor(Color.BLACK);
	    for(Dimension d : grillPositions){
	    	g.drawRect(d.width, d.height, width, height);
	    }
	    for(Dimension p : platePositions){
    		g.fillRect(p.width, p.height, width, height);
	    }
	    /** Draw the food items on the grills and plate positions */
	    int i = 0;
	    g.setColor(Color.RED);
	    synchronized(grillOrders){
		    for(Map.Entry<Integer, String> grill : grillOrders.entrySet()){
		    	int x = platePositions.get(i).width + 3;
		    	int y = platePositions.get(i).height + 15;
		    	g.drawString(grill.getValue(), x, y);
		    	i++;
		    }
	    }
	    int h = 0;
	    synchronized(plateOrders){
		    for(Map.Entry<Integer, String> plate : plateOrders.entrySet()){
		    	int x = grillPositions.get(h).width + 3;
		    	int y = grillPositions.get(h).height + 15;
		    	g.drawString(plate.getValue(), x, y);
		    	h++;
		    }
	    }
	    if(holding != null){
	    	g.drawString(holding, xPos, yPos);
	    }
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void DoGoHome() {
		yDestination = homeY;
		canRelease = true;
	}
	
	public void DoGoToFridge(){
		canRelease = true;
		yDestination = fridgeY;
	}
	
	public void DoGoToGrill(int grillPos){
		yDestination = grillPositions.get(grillPos).height;
		canRelease = true;
	}
	
	public void DoToggleItem(String item){
		holding = item;
	}
	
	public void DoPlaceFood(int grillPos, String food){
		if(food.equals("Krabby Patty")) food = "KP";
		else if(food.equals("Kelp Rings")) food = "KR";
		else if(food.equals("Coral Bits")) food = "CB";
		else if(food.equals("Kelp Shake")) food = "KS";
		
		grillOrders.put(grillPos, food);
	}
	
	public void DoPlateFood(int grillPos, String food){
		yDestination = platePositions.get(grillPos).height;
		grillOrders.remove(grillPos);
		
		if(food.equals("Krabby Patty")) food = "KP";
		else if(food.equals("Kelp Rings")) food = "KR";
		else if(food.equals("Coral Bits")) food = "CB";
		else if(food.equals("Kelp Shake")) food = "KS";
		
		plateOrders.put(grillPos, food);
		canRelease = true;
	}
	
	public void DoRemovePlateFood(int pos){
		plateOrders.remove(pos);
	}
	
	/** Utilities */
	public void setGrillDrawPositions(List<Dimension> grills, List<Dimension> plates){
		grillPositions = grills;
		platePositions = plates;
	}
	public CookRole getAgent(){
		return agent;
	}
}
