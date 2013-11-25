package transportation.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import transportation.BusstopAgent;
import transportation.interfaces.Corner;
import transportation.test.mock.MockCorner;

public class BusstopAgentTest {
	Corner corner;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		corner = new MockCorner("MockCorner");
	}

	@Before
	public void setUp() throws Exception {
		BusstopAgent busstopAgent = new BusstopAgent
				(corner, direction, directionInRoute);
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
