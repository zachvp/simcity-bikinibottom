package transportation.test;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import kelp.KelpClass;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import parser.BuildingDef;
import parser.BuildingPosParser;
import parser.CornersWithBusstopsParser;
import parser.test.BuildingPosParserTest;
import parser.test.mock.MockCityBuilding;
import sun.net.www.content.text.PlainTextInputStream;
import transportation.PassengerRole;
import transportation.gui.interfaces.PassengerGui;
import transportation.interfaces.Busstop;
import transportation.interfaces.Corner;
import transportation.interfaces.Passenger;
import transportation.mapbuilder.MapBuilder;
import transportation.mapbuilder.test.MapBuilderTest;
import transportation.test.mock.MockPassengerGui;
import transportation.test.mock.MockPerson;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.XYPos;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import agent.Constants;

public class PassengerRoleTest {
	
	PassengerRole passenger;
	MockPerson person;
	static List<CityLocation> locations;
	private static List<Corner> busRoute;
	private static List<Corner> corners;
	private PassengerGui mockGui;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//Check preconditions
		assertEquals(6, Constants.MAX_BLOCK_COL);

		KelpClass.getKelpInstance();
		List<BuildingDef> buildings;
		try {
			BufferedInputStream stream = (BufferedInputStream)
					BuildingPosParserTest.class
					.getResource("BuildingPosTest.csv").getContent();
			buildings = BuildingPosParser
					.parseBuildingPos(stream);
		} catch (Exception x) {
			fail(x.toString());
			return;
		}

		try {
			PlainTextInputStream stream = 
					(PlainTextInputStream)MapBuilderTest.class
					.getResource("CornersWithBusstops.txt").getContent();
			Set<Integer> cornersWithBusstops = 
					CornersWithBusstopsParser.parseCornersWithBusstops(stream);
			assertFalse(cornersWithBusstops.isEmpty());
			MapBuilder.createMap(buildings.size(), cornersWithBusstops);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}

		corners = MapBuilder.getCreatedCorners();
		busRoute = MapBuilder.getBusRoute();

		locations = new ArrayList<CityLocation>();

		for (int j = 0; j < (buildings.size() 
				/ Constants.MAX_BLOCK_COL); j++) {
			for (int i = 0; i < Constants.MAX_BLOCK_COL; i++) {
				int index = j*Constants.MAX_BLOCK_COL + i;
				locations.add(new MockCityBuilding(
						buildings.get(index).getName(),
						buildings.get(index).getType(),
						new XYPos(i*(Constants.BUILDING_WIDTH 
								+ Constants.SPACE_BETWEEN_BUILDINGS)
								+ Constants.MAP_MARGIN_X,
								j*(Constants.BUILDING_HEIGHT
								+ Constants.SPACE_BETWEEN_BUILDINGS)
								+ Constants.MAP_MARGIN_Y),
								new XYPos(0, 0)));
			}
		}

		assertEquals(36, locations.size());

		for (Corner corner : corners) {
			locations.add(corner);
			List<Busstop> busstops = corner.getBusstops();
			for (Busstop busstop : busstops) {
				locations.add(busstop);
			}
		}

		KelpClass.getKelpInstance().setData(locations, busRoute);

	}

	@Before
	public void setUp() throws Exception {
		person = new MockPerson("MockPerson");
		mockGui = new MockPassengerGui();
		passenger = new PassengerRole(person,
				KelpClass.getKelpInstance().placesNearMe(
						new XYPos(0,0), LocationTypeEnum.Hospital)
						.get(0), mockGui);
	}

	@Test
	public void testOneWalk() {
		assertEquals(locations.get(12), passenger.getLocation());
		
		passenger.msgGoToLocation(locations.get(21));
		
		passenger.pickAndExecuteAnAction();
		
		
	}

}
