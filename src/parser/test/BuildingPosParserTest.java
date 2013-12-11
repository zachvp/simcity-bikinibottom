package parser.test;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import parser.BuildingDef;
import parser.BuildingDef.CreatorEnum;
import parser.BuildingPosParser;

public class BuildingPosParserTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		List<BuildingDef> result;
		try {
			BufferedInputStream stream = (BufferedInputStream)getClass()
					.getResource("BuildingPosTest.csv").getContent();
			result = BuildingPosParser
					.parseBuildingPos(stream);
		} catch (Exception x) {
			fail(x.toString());
			return;
		}
		assertEquals("First line failed", 
				new BuildingDef("Cero", LocationTypeEnum.None, CreatorEnum.UNKNOWN),
				result.get(0));
		
		assertEquals("Second line failed", 
				new BuildingDef("Uno", LocationTypeEnum.Restaurant, CreatorEnum.ERIK),
				result.get(1));
		
		assertEquals("Third line failed", 
				new BuildingDef("Dos", LocationTypeEnum.House, CreatorEnum.ZACH),
				result.get(2));
		
		assertEquals("Fourth line failed", 
				new BuildingDef("Tres", LocationTypeEnum.Market, CreatorEnum.ANTHONY),
				result.get(3));
		
		assertEquals("Fifth line failed", 
				new BuildingDef("Cuatro", LocationTypeEnum.Bank, CreatorEnum.JACK),
				result.get(4));
		
		assertEquals("Sixth line failed", 
				new BuildingDef("Cinco", LocationTypeEnum.Hospital, CreatorEnum.VICTORIA),
				result.get(5));
		
		assertNotEquals("Equals function returns true for non-equal objects", 
				new BuildingDef("Hola", LocationTypeEnum.None, CreatorEnum.UNKNOWN),
				result.get(0));
		
		assertNotEquals("Equals function returns true for non-equal objects", 
				new BuildingDef("Cero", LocationTypeEnum.Restaurant, CreatorEnum.ERIK),
				result.get(0));
	}

}
