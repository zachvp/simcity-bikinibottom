package transportation.mapbuilder.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import kelp.KelpClass;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import parser.CornersWithBusstopsParser;
import sun.net.www.content.text.PlainTextInputStream;
import transportation.interfaces.Busstop;
import transportation.interfaces.Corner;
import transportation.mapbuilder.MapBuilder;
import CommonSimpleClasses.DirectionEnum;
import CommonSimpleClasses.XYPos;
import agent.Constants;


// TODO update to test busstop orientations
public class MapBuilderTest {
	
	static List<Corner> corners;
	static List<Corner> busRoute;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			PlainTextInputStream stream = (PlainTextInputStream)MapBuilderTest.class
					.getResource("CornersWithBusstops.txt").getContent();
			Set<Integer> cornersWithBusstops = 
				CornersWithBusstopsParser.parseCornersWithBusstops(stream);
			assertFalse(cornersWithBusstops.isEmpty());
			MapBuilder.createMap(36, cornersWithBusstops);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
		corners = MapBuilder.getCreatedCorners();
		busRoute = MapBuilder.getBusRoute();
	}

	@Test
	public void testOneAssertPreconditions() {
		assertEquals("Map preconditions not met", 6, Constants.MAX_BLOCK_COL);
	}
	
	@Test
	public void testTwoCheckCorners() {
		//Checking corner amount
		assertEquals(25, corners.size());
		
		//Checking position of random corners
		assertEquals(new XYPos(
				(int)(Constants.MAP_MARGIN_X + 3*Constants.BUILDING_WIDTH
				+ 2.5*Constants.SPACE_BETWEEN_BUILDINGS),
				(int)(Constants.MAP_MARGIN_Y + 3*Constants.BUILDING_HEIGHT
				+ 2.5*Constants.SPACE_BETWEEN_BUILDINGS)),
				corners.get(12).position());
		
		assertEquals(new XYPos(
				(int)(Constants.MAP_MARGIN_X + 5*Constants.BUILDING_WIDTH
				+ 4.5*Constants.SPACE_BETWEEN_BUILDINGS),
				(int)(Constants.MAP_MARGIN_Y + 2*Constants.BUILDING_HEIGHT
				+ 1.5*Constants.SPACE_BETWEEN_BUILDINGS)),
				corners.get(9).position());
		
		assertEquals(new XYPos(
				(int)(Constants.MAP_MARGIN_X + 1*Constants.BUILDING_WIDTH
				+ 0.5*Constants.SPACE_BETWEEN_BUILDINGS),
				(int)(Constants.MAP_MARGIN_Y + 4*Constants.BUILDING_HEIGHT
				+ 3.5*Constants.SPACE_BETWEEN_BUILDINGS)),
				corners.get(15).position());
		
		//Checking neighbors
		try {
			assertEquals(corners.get(10),
					corners.get(5).getCornerForDir(DirectionEnum.South));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
		try {
			assertEquals(corners.get(13),
					corners.get(14).getCornerForDir(DirectionEnum.West));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
		try {
			corners.get(5).getCornerForDir(DirectionEnum.West);
			fail("Should have thrown exception but didn't");
		} catch (Exception e) {}
		
	}
	
	@Test
	public void testThreeCheckBusstopAssignment() {
		//Checking corners that should have busstops
		assertEquals(2,corners.get(3).getBusstops().size());
		assertEquals(2,corners.get(9).getBusstops().size());
		assertEquals(2,corners.get(21).getBusstops().size());
		
		//Checking corners that should not have busstops
		assertEquals(0,corners.get(0).getBusstops().size());
		assertEquals(0,corners.get(6).getBusstops().size());
		assertEquals(0,corners.get(20).getBusstops().size());
		
		//Checking correct busstop directions
		List <Busstop> currBusstops = corners.get(1).getBusstops();
		
		for (Busstop busstop : currBusstops) {
			if (busstop.direction() == DirectionEnum.West
				|| busstop.direction() == DirectionEnum.East) {
			} else {
				fail("Busstop with wrong direction");
			}
		}
		
		currBusstops = corners.get(23).getBusstops();
		
		for (Busstop busstop : currBusstops) {
			if (busstop.direction() == DirectionEnum.West
				|| busstop.direction() == DirectionEnum.East) {
			} else {
				fail("Busstop with wrong direction");
			}
		}
		
		currBusstops = corners.get(5).getBusstops();
		
		for (Busstop busstop : currBusstops) {
			if (busstop.direction() == DirectionEnum.West
				|| busstop.direction() == DirectionEnum.East) {
				fail("Busstop with wrong direction");
			} else {}
		}
		
		currBusstops = corners.get(9).getBusstops();
		
		for (Busstop busstop : currBusstops) {
			if (busstop.direction() == DirectionEnum.West
				|| busstop.direction() == DirectionEnum.East) {
				fail("Busstop with wrong direction");
			} else {}
		}
	}
	
	@Test
	public void testFourCheckBusRoute() {
		//Checking for length
		assertEquals(16, busRoute.size());
		
		//Checking for correct positions
		assertEquals(corners.get(0), busRoute.get(0));
		assertEquals(corners.get(5), busRoute.get(1));
		assertEquals(corners.get(10), busRoute.get(2));
		assertEquals(corners.get(20), busRoute.get(4));
		assertEquals(corners.get(21), busRoute.get(5));
		assertEquals(corners.get(24), busRoute.get(8));
		assertEquals(corners.get(14), busRoute.get(10));
		assertEquals(corners.get(9), busRoute.get(11));
		assertEquals(corners.get(1), busRoute.get(15));
	}
	

}
