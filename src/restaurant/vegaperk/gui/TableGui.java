package restaurant.vegaperk.gui;

import java.awt.*;
import java.util.Map;

import agent.gui.Gui;

public class TableGui implements Gui{
	public Map<Integer, Dimension> tableMap;
	
	private static final int TABLE_WIDTH = 50;
	private static final int TABLE_HEIGHT = 50;

	public TableGui(Map<Integer, Dimension> map){ //HostAgent m) {
		tableMap = map;
	}

	public void draw(Graphics2D g, boolean gradingView) {
		//draw the tables
    	g.setColor(Color.ORANGE);
    	for(int i = 0; i < tableMap.size(); i++){
    	    g.fillRect(tableMap.get(i).width, tableMap.get(i).height, TABLE_WIDTH, TABLE_HEIGHT);
    	}
    	
    	g.drawString("Cashier", 250, 20);
    	g.drawString("Refrigerator", 480, 240);
	}

	@Override
	public void updatePosition() {
		// Don't need because tables are static
		
	}

	@Override
	public boolean isPresent() {
		return true;
	}
}
