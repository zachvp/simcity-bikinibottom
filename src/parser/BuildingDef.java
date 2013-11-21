package parser;

import CommonSimpleClasses.CityLocation.LocationTypeEnum;

public class BuildingDef {
	public BuildingDef(String name, LocationTypeEnum type) {
		this.name = name;
		this.type = type;
	}
	
	String name;
	LocationTypeEnum type;
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BuildingDef) {
			BuildingDef castObj = (BuildingDef) obj;
			if (this.name.equals(castObj.name) 
				&& this.type.equals(castObj.type)) {
				return true;
			}
		}
		return false;
	}
}