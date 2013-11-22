package parser.test;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import parser.CornersWithBusstopsParser;
import sun.net.www.content.text.PlainTextInputStream;

public class CornersWithBusstopsParserTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Set<Integer> result;
		try {
			PlainTextInputStream stream = (PlainTextInputStream)getClass()
					.getResource("CornersWithBusstopsTest.txt").getContent();
			result = CornersWithBusstopsParser
					.parseCornersWithBusstops(stream);
		} catch (Exception x) {
			fail(x.toString());
			return;
		}
		
		assertTrue(result.contains(2));
		assertTrue(result.contains(3));
		assertTrue(result.contains(4));
		assertTrue(result.contains(6));
		assertTrue(result.contains(11));
		assertTrue(result.contains(17));
		assertTrue(result.contains(19));
		assertTrue(result.contains(10));
		
		assertFalse(result.contains(1));
		assertFalse(result.contains(5));
		assertFalse(result.contains(35));
		assertFalse(result.contains(16));
		assertFalse(result.contains(7));
		assertFalse(result.contains(-2));
		
	}

}
