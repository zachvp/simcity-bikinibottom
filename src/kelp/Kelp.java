package kelp;

import java.util.List;
import java.util.SortedMap;

import CommonSimpleClasses.*;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;

// TODO Fix image likn in kelp.md on my documents
// TODO add to interface new public methods
// TODO update DD
public interface Kelp {
	
	public List<CityLocation> routeFromAToB(XYPos A, CityLocation B);
	
	public List<CityLocation> routeFromAToB(CityLocation A, CityLocation B);
	
	// TODO Update DD
	public SortedMap<Integer, CityLocation> placesNearMe(XYPos me, LocationTypeEnum type);
	
	
	// TODO Update DD
	public SortedMap<Integer, CityLocation> placesNearMe(CityLocation me, LocationTypeEnum type);
}
