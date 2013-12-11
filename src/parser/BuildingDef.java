package parser;

import CommonSimpleClasses.CityLocation.LocationTypeEnum;

public class BuildingDef {
	public BuildingDef(String name, LocationTypeEnum type, CreatorEnum creator) {
		this.name = name;
		this.type = type;
		this.creator = creator;
	}
	
	String name;
	LocationTypeEnum type;
	CreatorEnum creator;
	
	public enum CreatorEnum {ANTHONY, DIEGO, ERIK, JACK, VICTORIA, ZACH, UNKNOWN};
	
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
	
	public String getName(){
		return name;
	}
	public LocationTypeEnum getType(){
		return type;
	}
	public CreatorEnum getCreator() {
		return creator;
	}
}