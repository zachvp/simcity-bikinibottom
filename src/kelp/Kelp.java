package kelp;

import java.util.List;

import CommonSimpleClasses.*;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;

public interface Kelp {
	
	public List<CityLocation> routeFromAToB(XYPos A, CityLocation B);
	
	public List<CityLocation> routeFromAToB(CityLocation A, CityLocation B);
	
	public List<CityLocation> placesNearMe(XYPos me, LocationTypeEnum type);
	
	public List<CityLocation> placesNearMe(CityLocation me, LocationTypeEnum type);
}
