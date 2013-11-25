package transportation.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import CommonSimpleClasses.DirectionEnum;
import transportation.BusstopAgent;
import transportation.interfaces.Corner;
import transportation.test.mock.MockCorner;

public class BusstopAgentTest {
	static Corner corner;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		corner = new MockCorner("MockCorner",0,0);
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void test() {
		BusstopAgent busstopAgent = new BusstopAgent
				(corner, DirectionEnum.North, true);
		
	}

}
