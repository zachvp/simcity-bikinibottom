package classifieds;

import housing.interfaces.Dwelling;

import java.util.List;

import CommonSimpleClasses.CityBuilding;
import agent.Role;
import agent.WorkRole;

public interface Classifieds {
	
	/** Will return a list of WorkRoles. If building != null, 
	 * returns only jobs for that building, else it returns 
	 * jobs far all buildings. If returnOnlyOpenPositions
	 * is true, it returns only open positions, else it 
	 * returns both open and closed positions.
	 */
	List<WorkRole> getJobsForBuilding(CityBuilding building, 
			boolean returnOnlyOpenPositions);
	
	/** Adds a role to the register. ALL WorkRoles must do
	 * this.
	 */
	void addWorkRole(WorkRole role);
	
	List<Dwelling> getRooms(boolean returnOnlyOpenRooms);
	
	void addDwelling(Dwelling dwelling);
	
}
