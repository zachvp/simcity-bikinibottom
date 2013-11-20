package kelp;

import java.util.List;
import CommonSimpleClasses.*;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;

// TODO Fix image link in kelp.md on my documents
// TODO add to interface new public methods
// TODO update DD
public interface Kelp {
	// TODO Update DD
	public List<CityLocation> routeFromAToB(XYPos A, CityLocation B,
			Boolean tryBus);
	
	// TODO Update DD
	public List<CityLocation> routeFromAToB(CityLocation A,
			CityLocation B, Boolean tryBus);
	
	// TODO Update DD
	public List<CityLocation> placesNearMe(XYPos me,
			LocationTypeEnum type);
	
	
	// TODO Update DD
	public List<CityLocation> placesNearMe(CityLocation me,
			LocationTypeEnum type);

}
