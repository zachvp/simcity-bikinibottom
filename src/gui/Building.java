package gui;

import java.awt.geom.*;

import javax.swing.JPanel;

import CommonSimpleClasses.CityLocation.LocationTypeEnum;

/**
 * A Rectangle2D that appears as a Building on the CityMap
 *
 */
public class Building extends Rectangle2D.Double {
	String name;
	JPanel animationPanel;	
	LocationTypeEnum type;
	JPanel controls;
	
	public Building(int x, int y, int width, int height) {
		super( x, y, width, height );
		
		//TODO link to building's animation panel ?
		
	}
	
	public void setName(String n){
		name = n;
	}
	
	public String getName(){
		return name;
	}

	public void setType(LocationTypeEnum ntype) {
		type = ntype;
	}
	
	public LocationTypeEnum getType(){
		return type;
	}
	
	public void setControlPanel(JPanel c){
		controls = c;
	}
}
