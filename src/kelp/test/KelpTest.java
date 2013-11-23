package kelp.test;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import kelp.Kelp;
import kelp.KelpClass;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import agent.Constants;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.XYPos;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import parser.BuildingDef;
import parser.BuildingPosParser;
import parser.CornersWithBusstopsParser;
import parser.test.BuildingPosParserTest;
import parser.test.mock.MockCityBuilding;
import sun.net.www.content.text.PlainTextInputStream;
import transportation.CornerAgent.MyCorner;
import transportation.interfaces.Busstop;
import transportation.interfaces.Corner;
import transportation.mapbuilder.MapBuilder;
import transportation.mapbuilder.test.MapBuilderTest;

public class KelpTest {
	static Kelp kelp;
	static List<Corner> corners;
	static List<Corner> busRoute;
	static List<CityLocation> locations;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//Check preconditions
		assertEquals(6, Constants.MAX_BLOCK_COL);
		
		kelp = KelpClass.getKelpInstance();
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
		
		kelp.setData(locations, busRoute);
		
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testOnePlacesNearMe() {
		List<CityLocation> currentPlaces = kelp.placesNearMe
				(corners.get(0), LocationTypeEnum.Restaurant);
		
		//Check length
		assertEquals(6, currentPlaces.size());
		
		//Check objects
		assertTrue(locations.get(1) == currentPlaces.get(0)
					|| locations.get(7) == currentPlaces.get(0));
		
		assertEquals(locations.get(14), currentPlaces.get(2));
		
		
		assertEquals(locations.get(35), currentPlaces.get(5));
		
		
		
		
		currentPlaces = kelp.placesNearMe
				(corners.get(24), LocationTypeEnum.Bank);
		
		//Check length
		assertEquals(5, currentPlaces.size());
		
		//Check objects
		assertTrue(locations.get(4) == currentPlaces.get(4)
					|| locations.get(24) == currentPlaces.get(4));
		
		assertEquals(locations.get(17), currentPlaces.get(0));
		
		
		
		
		currentPlaces = kelp.placesNearMe
				(locations.get(19), LocationTypeEnum.Hospital);
		
		//Check length
		assertEquals(5, currentPlaces.size());
		
		//Check objects
		assertTrue(locations.get(12) == currentPlaces.get(1)
					|| locations.get(26) == currentPlaces.get(1));
		
		assertEquals(locations.get(19), currentPlaces.get(0));
		
		
	}
	

	@Test
	public void testTwoRouteFromAToBNoBus() {
		List<CityLocation> currentRoute = kelp.routeFromAToB(
				locations.get(7), locations.get(34), false);
		
		//Correct start corner
		assertTrue(currentRoute.get(0) == corners.get(0)
				|| currentRoute.get(0) == corners.get(1)
				|| currentRoute.get(0) == corners.get(5)
				|| currentRoute.get(0) == corners.get(6));
		
		//Assert all locations but the last one are corners
		for (int i = 0; i < currentRoute.size()-1; i++) {
			assertTrue(currentRoute.get(i) instanceof Corner);
		}
		
		//Assert that corners in the path are connected
		for (int i = 0; i < currentRoute.size()-1; i++) {
			CityLocation currLocation = currentRoute.get(i);
			CityLocation nextLocation = currentRoute.get(i+1);
			Corner currCorner, nextCorner;
			
			if (currLocation instanceof Corner
				&& nextLocation instanceof Corner) {
				currCorner = (Corner) currLocation;
				nextCorner = (Corner) nextLocation;
			} else {
				break;
			}
			
			List<MyCorner> adjCorners = currCorner.getAdjacentCorners();
			boolean found = false;
			for (int j = 0; j < adjCorners.size(); j++) {
				if (adjCorners.get(j).c == nextCorner) {
					found = true;
					break;
				}
			}
			
			assertTrue(found);
		}
		
		//Assert that the last location in the route is the destination
		assertEquals(locations.get(34), currentRoute.get(currentRoute.size()-1));
		
		
		
		
		currentRoute = kelp.routeFromAToB(
				locations.get(35), locations.get(8), false);
		
		//Correct start corner
		assertEquals(corners.get(24), currentRoute.get(0));
		
		//Assert all locations but the last one are corners
		for (int i = 0; i < currentRoute.size()-1; i++) {
			assertTrue(currentRoute.get(i) instanceof Corner);
		}
		
		//Assert that corners in the path are connected
		for (int i = 0; i < currentRoute.size()-1; i++) {
			CityLocation currLocation = currentRoute.get(i);
			CityLocation nextLocation = currentRoute.get(i+1);
			Corner currCorner, nextCorner;
			
			if (currLocation instanceof Corner
				&& nextLocation instanceof Corner) {
				currCorner = (Corner) currLocation;
				nextCorner = (Corner) nextLocation;
			} else {
				break;
			}
			
			List<MyCorner> adjCorners = currCorner.getAdjacentCorners();
			boolean found = false;
			for (int j = 0; j < adjCorners.size(); j++) {
				if (adjCorners.get(j).c == nextCorner) {
					found = true;
					break;
				}
			}
			
			assertTrue(found);
		}
		
		//Assert that the last location in the route is the destination
		assertEquals(locations.get(8), currentRoute.get(currentRoute.size()-1));
		
	}
	
	@Test
	public void testThreeRouteFromAToBWithBus() {
		List<CityLocation> currentRoute = kelp.routeFromAToB(
				locations.get(7), locations.get(35), true);

		//Correct start corner
		assertTrue(currentRoute.get(0) == corners.get(0)
				|| currentRoute.get(0) == corners.get(1)
				|| currentRoute.get(0) == corners.get(5)
				|| currentRoute.get(0) == corners.get(6));

		/*Assert that corners in the path are connected until
		 * the busstop
		 */
		int i;
		for (i = 0; i < currentRoute.size()-1; i++) {
			CityLocation currLocation = currentRoute.get(i);
			CityLocation nextLocation = currentRoute.get(i+1);
			Corner currCorner, nextCorner;

			if (currLocation instanceof Corner
					&& nextLocation instanceof Corner) {
				currCorner = (Corner) currLocation;
				nextCorner = (Corner) nextLocation;
			} else {
				break;
			}

			List<MyCorner> adjCorners = currCorner.getAdjacentCorners();
			boolean found = false;
			for (int j = 0; j < adjCorners.size(); j++) {
				if (adjCorners.get(j).c == nextCorner) {
					found = true;
					break;
				}
			}

			assertTrue(found);
		}
		i++;
		
		//Check that there is a busstop after the corners
		assertEquals(LocationTypeEnum.Busstop, 
				currentRoute.get(i).type());
		i++;
		
		/*Assert that corners in the path are connected after
		 * the busstop
		 */
		for (; i < currentRoute.size()-1; i++) {
			CityLocation currLocation = currentRoute.get(i);
			CityLocation nextLocation = currentRoute.get(i+1);
			Corner currCorner, nextCorner;

			if (currLocation instanceof Corner
					&& nextLocation instanceof Corner) {
				currCorner = (Corner) currLocation;
				nextCorner = (Corner) nextLocation;
			} else {
				break;
			}

			List<MyCorner> adjCorners = currCorner.getAdjacentCorners();
			boolean found = false;
			for (int j = 0; j < adjCorners.size(); j++) {
				if (adjCorners.get(j).c == nextCorner) {
					found = true;
					break;
				}
			}

			assertTrue(found);
		}
		
		//Assert that the last location in the route is the destination
		assertEquals(locations.get(35), currentRoute.get(currentRoute.size()-1));
	}
	
	@Test
	public void testThreeAnotherRouteFromAToBWithBus() {
		List<CityLocation> currentRoute = kelp.routeFromAToB(
				locations.get(31), locations.get(11), true);

		//Correct start corner
		assertTrue(currentRoute.get(0) == corners.get(20)
				|| currentRoute.get(0) == corners.get(21));

		/*Assert that corners in the path are connected until
		 * the busstop
		 */
		int i;
		for (i = 0; i < currentRoute.size()-1; i++) {
			CityLocation currLocation = currentRoute.get(i);
			CityLocation nextLocation = currentRoute.get(i+1);
			Corner currCorner, nextCorner;

			if (currLocation instanceof Corner
					&& nextLocation instanceof Corner) {
				currCorner = (Corner) currLocation;
				nextCorner = (Corner) nextLocation;
			} else {
				break;
			}

			List<MyCorner> adjCorners = currCorner.getAdjacentCorners();
			boolean found = false;
			for (int j = 0; j < adjCorners.size(); j++) {
				if (adjCorners.get(j).c == nextCorner) {
					found = true;
					break;
				}
			}

			assertTrue(found);
		}
		i++;
		
		//Check that there is a busstop after the corners
		assertEquals(LocationTypeEnum.Busstop, 
				currentRoute.get(i).type());
		i++;
		
		/*Assert that corners in the path are connected after
		 * the busstop
		 */
		for (; i < currentRoute.size()-1; i++) {
			CityLocation currLocation = currentRoute.get(i);
			CityLocation nextLocation = currentRoute.get(i+1);
			Corner currCorner, nextCorner;

			if (currLocation instanceof Corner
					&& nextLocation instanceof Corner) {
				currCorner = (Corner) currLocation;
				nextCorner = (Corner) nextLocation;
			} else {
				break;
			}

			List<MyCorner> adjCorners = currCorner.getAdjacentCorners();
			boolean found = false;
			for (int j = 0; j < adjCorners.size(); j++) {
				if (adjCorners.get(j).c == nextCorner) {
					found = true;
					break;
				}
			}

			assertTrue(found);
		}
		
		//Assert that the last location in the route is the destination
		assertEquals(locations.get(11), currentRoute.get(currentRoute.size()-1));
		
	}
	
	@Test
	public void testFourRouteFromAToBAllowBusButRouteTooShort() {
		List<CityLocation> currentRoute = kelp.routeFromAToB(
				locations.get(7), locations.get(10), true);
		
		//Correct start corner
		assertTrue(currentRoute.get(0) == corners.get(0)
				|| currentRoute.get(0) == corners.get(1)
				|| currentRoute.get(0) == corners.get(5)
				|| currentRoute.get(0) == corners.get(6));
		
		//Assert all locations but the last one are corners
		for (int i = 0; i < currentRoute.size()-1; i++) {
			assertTrue(currentRoute.get(i) instanceof Corner);
		}
		
		//Assert that corners in the path are connected
		for (int i = 0; i < currentRoute.size()-1; i++) {
			CityLocation currLocation = currentRoute.get(i);
			CityLocation nextLocation = currentRoute.get(i+1);
			Corner currCorner, nextCorner;
			
			if (currLocation instanceof Corner
				&& nextLocation instanceof Corner) {
				currCorner = (Corner) currLocation;
				nextCorner = (Corner) nextLocation;
			} else {
				break;
			}
			
			List<MyCorner> adjCorners = currCorner.getAdjacentCorners();
			boolean found = false;
			for (int j = 0; j < adjCorners.size(); j++) {
				if (adjCorners.get(j).c == nextCorner) {
					found = true;
					break;
				}
			}
			
			assertTrue(found);
		}
		
		//Assert that the last location in the route is the destination
		assertEquals(locations.get(10), currentRoute.get(currentRoute.size()-1));
		
	}
	
}
