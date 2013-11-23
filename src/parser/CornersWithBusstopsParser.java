package parser;

import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import sun.net.www.content.text.PlainTextInputStream;

/*
 * Parses the configuration files for Busstop placement,
 * returns a list of Integers representing the Corners
 * that should have Busstops.
 * 
 * NOTE: You have to make sure to set the bus stops following
 * the rules about bus routes described in the 'Da Rules' section
 * of the wiki. Corners are counted left to right, then top to bottom,
 * and only corners that lead to 4 roads are counted.
 * 
 * You pass down a stream to a file to the static function;
 * for an example, look at the first few lines of the unit test.
 * 
 * For an example, open CornersWithBusstopsTest.txt using your favorite text
 * editor.
 * 
 * MAY throw exceptions for incorrectly formated files. May also ignore
 * the incorrect line, so look out.
 */

public class CornersWithBusstopsParser {	
	
	static public Set<Integer> parseCornersWithBusstops
			(PlainTextInputStream stream) throws Exception {
		
		Scanner scanner = new Scanner(stream);
		
		Set<Integer> response = new TreeSet<Integer>();
		
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			
			response.add(Integer.parseInt(line));
		}
		
		if (scanner != null) scanner.close();
		
		return response;
	}
}
