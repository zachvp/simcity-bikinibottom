package parser;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import CommonSimpleClasses.CityLocation.LocationTypeEnum;

/*
 * Parses the configuration files for building placement,
 * returns a list of BuildingDef with a String name and a
 * LocationTypeEnum type. The type will be None for when
 * it is specified not to have a building.
 * 
 * You pass down a stream to a file to the static function;
 * for an example, look at the first few lines of the unit test.
 * 
 * For instructions on how to build the file and an example,
 * open parser/test/BuildingPosTest.csv using Excel or your 
 * favorite text editor.
 * 
 * MAY throw exceptions for incorrectly formated files. May also ignore
 * the incorrect line, so look out.
 */

public class BuildingPosParser {	
	
	static public List<BuildingDef>
			parseBuildingPos(BufferedInputStream stream) throws Exception {
		
		Scanner scanner = new Scanner(stream);
		
		List<BuildingDef> response = new ArrayList<BuildingDef>();
		
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			if (line.charAt(0) == '#' ||
					line.charAt(0) == '"') continue;
			
			int buildingInt = Integer.parseInt(line.substring(0, 1));
			String name = line.substring(2);
			
			LocationTypeEnum buildingType;
			
			switch (buildingInt) {
			case 0:
				buildingType = LocationTypeEnum.None;
				break;
			case 1:
				buildingType = LocationTypeEnum.Restaurant;
				break;
			case 2:
				buildingType = LocationTypeEnum.Bank;
				break;
			case 3:
				buildingType = LocationTypeEnum.Market;
				break;
			case 4:
				buildingType = LocationTypeEnum.House;
				break;
			case 5:
				buildingType = LocationTypeEnum.Apartment;
				break;
			case 6:
				buildingType = LocationTypeEnum.Hospital;
				break;
			default:
				throw new Exception("Parser was given an int that does "
						+ "not correspond to any building type.");
			}
			
			response.add(new BuildingDef(name, buildingType));
			
		}
		
		if (scanner != null) scanner.close();
		
		return response;
	}
}
