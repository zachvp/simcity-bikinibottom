package parser.test.mock;

import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.XYPos;
import agent.mock.Mock;

public class MockCityBuilding extends Mock implements CityBuilding {

	private LocationTypeEnum type;
	private XYPos position;
	private XYPos entrancePos;

	public MockCityBuilding(String name, LocationTypeEnum type,
			XYPos position, XYPos entrancePos) {
		super(name);
		this.type = type;
		this.position = position;
		this.entrancePos = entrancePos;
	}

	@Override
	public LocationTypeEnum type() {
		return type;
	}

	@Override
	public XYPos position() {
		return position;
	}

	@Override
	public XYPos entrancePos() {
		return entrancePos;
	}

}
