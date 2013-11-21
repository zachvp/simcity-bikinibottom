package parser.test;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import parser.BuildingDef;
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
				new BuildingDef("Cero", LocationTypeEnum.None),
				result.get(0));
		
		assertEquals("First line failed", 
				new BuildingDef("Uno", LocationTypeEnum.Restaurant),
				result.get(1));
		
		assertEquals("First line failed", 
				new BuildingDef("Dos", LocationTypeEnum.House),
				result.get(2));
		
		assertEquals("First line failed", 
				new BuildingDef("Tres", LocationTypeEnum.Market),
				result.get(3));
		
		assertEquals("First line failed", 
				new BuildingDef("Cuatro", LocationTypeEnum.Bank),
				result.get(4));
		
		assertEquals("First line failed", 
				new BuildingDef("Cinco", LocationTypeEnum.Hospital),
				result.get(5));
		
		assertNotEquals("Equals function returns true for non-equal objects", 
				new BuildingDef("Hola", LocationTypeEnum.None),
				result.get(0));
		
		assertNotEquals("Equals function returns true for non-equal objects", 
				new BuildingDef("Cero", LocationTypeEnum.Restaurant),
				result.get(0));
	}

}
