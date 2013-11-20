package gui;

import java.awt.geom.*;

import javax.swing.JPanel;


public class Building extends Rectangle2D.Double {
	String name;
	JPanel animationPanel;

	public Building(int x, int y, int width, int height) {
		super( x, y, width, height );
		
		//TODO add name
		//TODO link to building's animation panel 
		
	}
	
	public void setName(String n){
		name = n;
	}
	
	public String getName(){
		return name;
	}
}
