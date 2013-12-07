package transportation.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import CommonSimpleClasses.CardinalDirectionEnum;
import transportation.BusstopAgent;
import transportation.interfaces.Corner;
import transportation.interfaces.Passenger;
import transportation.test.mock.MockBus;
import transportation.test.mock.MockCorner;
import transportation.test.mock.MockPassenger;
import transportation.test.mock.MockPassengerGui;

public class BusstopAgentTest {
	static Corner corner;
	private static MockPassenger mockPassenger1, mockPassenger2;
	private static MockBus mockBus;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		corner = new MockCorner("MockCorner",0,0);
		mockPassenger1 = new MockPassenger();
		mockPassenger2 = new MockPassenger();
		mockBus = new MockBus("mockBus");
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testOneTwoPassengers() {
		BusstopAgent busstopAgent = new BusstopAgent
				(corner, CardinalDirectionEnum.North, true);
		
		assertEquals(corner, busstopAgent.corner());
		assertEquals(0, busstopAgent.getPeopleWaiting().size());
		assertEquals(CardinalDirectionEnum.North, busstopAgent.direction());
		
		busstopAgent.msgIAmHere(mockPassenger1);
		
		assertEquals(1, busstopAgent.getPeopleWaiting().size());
		
		busstopAgent.msgIAmHere(mockPassenger2);
		
		assertEquals(2, busstopAgent.getPeopleWaiting().size());
		
		busstopAgent.pickAndExecuteAnAction();
		
		assertEquals(2, busstopAgent.getPeopleWaiting().size());
		
		busstopAgent.msgGiveMePeople(mockBus); 
		
		assertEquals(2, busstopAgent.getPeopleWaiting().size());

		busstopAgent.pickAndExecuteAnAction();
		
		assertEquals(0, busstopAgent.getPeopleWaiting().size());
		assertEquals("Received list people of size 2", 
				mockBus.log.getLastLoggedEvent().getMessage());
		
		
	}

}
