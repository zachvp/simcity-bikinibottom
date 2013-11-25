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
    public static final int BUILDING_WIDTH = 46;
	public static final int BUILDING_HEIGHT = 46;
	
	/**
	 * Dimensions of the City Map
	 */
	public static final int MAP_WIDTH = 900;
	public static final int MAP_HEIGHT = 900;
	
	/**
	 * Margins of the City Map
	 */
	public static final int MAP_MARGIN_Y = 20;
	public static final int MAP_MARGIN_X = 60;
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

}
