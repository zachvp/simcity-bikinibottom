package transportation.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import CommonSimpleClasses.XYPos;
import transportation.CornerAgent;
import transportation.IntersectionAction;
import transportation.interfaces.Corner;
import transportation.interfaces.Vehicle;
import transportation.test.mock.MockCorner;
import transportation.test.mock.MockVehicle;

public class CornerAgentTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	private CornerAgent corner;
	private MockCorner mockCorner;
	private MockVehicle mockVehicle;

	@Before
	public void setUp() throws Exception {
		corner = new CornerAgent("corner", new XYPos(0,0));
		mockCorner = new MockCorner("mockCorner", 100, 0);
		mockVehicle = new MockVehicle("mockVehicle");
	}

	@Test
	public void testOneDrivingThrough() {
		assertEquals(new Integer(1), corner.getCrossroadBusy());
		assertEquals(0, corner.getWaitingForBusstops().size());
		assertEquals(0, corner.getWaitingForCorners().size());
		assertEquals(0, corner.getWaitingToCross().size());
		
		corner.pickAndExecuteAnAction();
		
		assertEquals(new Integer(1), corner.getCrossroadBusy());
		assertEquals(0, corner.getWaitingForBusstops().size());
		assertEquals(0, corner.getWaitingForCorners().size());
		assertEquals(0, corner.getWaitingToCross().size());
		
		corner.msgIWantToDriveTo(new IntersectionAction
				(mockCorner, mockVehicle));
		
		assertEquals(new Integer(1), corner.getCrossroadBusy());
		assertEquals(0, corner.getWaitingForBusstops().size());
		assertEquals(0, corner.getWaitingForCorners().size());
		assertEquals(1, corner.getWaitingToCross().size());
		
		corner.pickAndExecuteAnAction();
		
		assertEquals(new Integer(0), corner.getCrossroadBusy());
		assertEquals(0, corner.getWaitingForBusstops().size());
		assertEquals(0, corner.getWaitingForCorners().size());
		assertEquals(0, corner.getWaitingToCross().size());
		
	}

}
