package CommonSimpleClasses;

public interface CityLocation {
	
	//TODO update DD
	public enum LocationTypeEnum {
		Restaurant,
		Bank,
		Market,
		House,
		Apartment,
		Hospital,
		Busstop,
		Corner,
		None
	}
	
	//Returns what type of location it is.
	public LocationTypeEnum type();
	
	/* Returns the position of the location (center of
	 * the crossroad for corners, sign for bus stops, 
	 * upper left corner for buildings).
	 */
	public XYPos position();
}
