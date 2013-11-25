/*

Copyright (c) 2000-2003 Board of Trustees of Leland Stanford Jr. University,
all rights reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL
STANFORD UNIVERSITY BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

Except as contained in this notice, the name of Stanford University shall not
be used in advertising or otherwise to promote the sale, use or other dealings
in this Software without prior written authorization from Stanford University.

*/
package agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Constants of general use
 */
public interface Constants {
	/**
	 * True only for development versions of this program.
	 */
	// TODO disable DEBUG constant before final release
	public static final boolean DEBUG = true;
	
	/**
	 * Whether EventLog should log events.
	 */
	public static final boolean LOG = false;
	
	/**
	 * Whether print statements should execute.
	 */
	public static final boolean PRINT = false;
	
    /**
     * The number of milliseconds in a second
     */
    public static final long SECOND = 1000;
    /**
     * The number of milliseconds in a minute
     */
    public static final long MINUTE = 60 * SECOND;
    /**
     * The number of milliseconds in an hour
     */
    public static final long HOUR = 60 * MINUTE;
    /**
     * The number of milliseconds in a day
     */
    public static final long DAY = 24 * HOUR;
    /**
     * The number of milliseconds in a week
     */
    public static final long WEEK = 7 * DAY;

    /**
     * The line separator string on this system
     */
    public static String EOL = System.getProperty("line.separator");

    /**
     * The default encoding used when none is detected
     */
    public static String DEFAULT_ENCODING = "ISO-8859-1";
    
    /**
     * Dimensions of a building on a map
     */
    public static final int BUILDING_WIDTH = 56;
	public static final int BUILDING_HEIGHT = 56;
	
	/**
	 * Dimensions of the City Map
	 */
	public static final int MAP_WIDTH = 600-35;
	public static final int MAP_HEIGHT = 490;
	
	/**
	 * Margins of the City Map
	 */
	public static final int MAP_MARGIN_Y = 10;
	public static final int MAP_MARGIN_X = 30;
	/**
	 * Maximum number of Columns in the Map
	 */
	public static final int MAX_BLOCK_COL = 6;

	/** Space between 2 building on the city map, a.k.a. 
	 *  the width of the road and sidewalks.
	 */
	public static final int SPACE_BETWEEN_BUILDINGS = 34;

	public static final int BUSSTOP_OFFESET_PERPENDICULAR = (SPACE_BETWEEN_BUILDINGS/2);

	public static final int BUSSTOP_OFFESET_PARALLEL = (SPACE_BETWEEN_BUILDINGS/2);
	
	/**
	 * The width of the info panel on the main GUI window.
	 */
	public static final int INFO_PANEL_WIDTH = 700;
	
	/**
	 * The height of the info panel on the main GUI window.
	 */
	public static final int INFO_PANEL_HEIGHT = 185;
	
	/**
	 * The initial amount of money in the market
	 */
	public static final int MarketInitialMoney = 100;
	
	/**
	 * The initial amount of Inventory levels of all the items
	 */
	public static final int LamboFinnyInitialAmount  = 100;
	public static final int ToyodaInitialAmount      = 100;
	public static final int KrabbyPattyInitialAmount = 100;
	public static final int KelpShakeInitialAmount   = 100;
	public static final int CoralBitsInitialAmount   = 100;
	public static final int KelpRingsInitialAmount   = 100;
	
	/**
	 * The PriceList of the market
	 */
	public static HashMap<String, Double> MarketPriceList = new HashMap<String,Double>(){
		{
		double Toyoda = 100;
		double LamboFinny = 300;
		double KrabbyPatty = 20;
		double KelpShake = 10;
		double CoralBits = 15;
		double KelpRings = 5;
		put("Krabby Patty", KrabbyPatty);
		put("Kelp Shake", KelpShake);
		put("Coral Bits", CoralBits);
		put("Kelp Rings", KelpRings);
		put("LamboFinny", LamboFinny);
		put("Toyoda", Toyoda);	
		}
	};
		
	/**
	 * Types of food in the city
	 */
	public static final List<String> FOODS = new ArrayList<String>() {
		{
			add("Krabby Patty");
			add("Kelp Shake");
			add("Coral Bits");
			add("Kelp Rings");
		}
	};
	
	/**
	 * Conditions for Dwellings
	 */
	enum Condition { GOOD, FAIR, POOR, BROKEN, BEING_FIXED }
	
	/**
	 * Types of Car in the city
	 */
	public static final List<String> CARS = new ArrayList<String>() {
		{
			add("LamboFinny");
			add("Toyoda");
		}
	};
}
