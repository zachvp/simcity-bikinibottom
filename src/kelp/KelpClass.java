package kelp;

import java.awt.Point;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.management.modelmbean.InvalidTargetObjectTypeException;
import javax.naming.InitialContext;

import transportation.interfaces.Busstop;
import transportation.interfaces.Corner;

import com.sun.org.apache.xml.internal.security.utils.IgnoreAllErrorHandler;

import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.DirectionEnum;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import CommonSimpleClasses.XYPos;

// TODO Make methods return something other than maps to avoid overwriting
public class KelpClass implements Kelp {
	
	// TODO SET CORRECT CONSTANTS
	private static final double AVG_BUS_WAIT = 30;
	private static final double BUS_SPEED = 0; 
	private static final double WALKING_SPEED = 0;
	private static final int DISTANCE_THRESHOLD_FOR_SAME_COORDINATE = 0;
	List<CityLocation> locations;
	
	List<Corner> busRoute;

	@Override
	public List<CityLocation> routeFromAToB(XYPos A, CityLocation B) {
		boolean tryBus = true;  //TODO Add as argument, assuming correct input
		boolean hasCar = false;  //TODO Add as argument, assuming no car
		
		XYPos posA = A;
		XYPos posB = getEntrancePosition(B);
		
		List<CityLocation> response;
		
		List<CityLocation> pathWithoutBus = routeWithoutBus(posA,posB);
		
		if (tryBus) {
			List<CityLocation> pathWithBus = routeWithBus(posA,posB);
			response = returnFastestPath(pathWithBus,pathWithoutBus, posA);
		} else {
			response = pathWithoutBus;
		}
		
		response.add(B);
		
		return response;
	}

	private List<CityLocation> returnFastestPath(
			List<CityLocation> path1, List<CityLocation> path2, 
			XYPos initialPos) {
		
		double time1 = timeForPath(path1, initialPos);
		
		double time2 = timeForPath(path2, initialPos);
		
		if (time1 < time2) {
			return path1;
		} else return path2;
	}

	public double timeForPath(List<CityLocation> path, XYPos initialPos) {
		XYPos pastPos = initialPos;
		boolean wasBusstop = false;
		double time = 0;
		
		for (CityLocation loc : path) {
			if (loc.type() == LocationTypeEnum.Busstop) {
				time += AVG_BUS_WAIT;
				pastPos = loc.position();
				wasBusstop = true;
			} else {
				double speed;
				if (wasBusstop == true) {
					speed = BUS_SPEED;
				} else {
					speed = WALKING_SPEED;
				}
				time += distanceHeuristic(pastPos, loc.position())
						* speed;
				pastPos = loc.position();
				wasBusstop = false;
			}
		}
		return time;
	}

	private List<CityLocation> routeWithBus(XYPos posA, XYPos posB) {
		SortedMap<Integer, CityLocation> nearBusstops = placesNearMe(posA, 
				LocationTypeEnum.Busstop);
		Busstop nearestBusstop = (Busstop)nearBusstops.get(nearBusstops.firstKey());
		Corner nearestCornerWithBusstopStart = nearestBusstop.corner();
		
		nearBusstops = placesNearMe(posB, 
				LocationTypeEnum.Busstop);
		nearestBusstop = (Busstop)nearBusstops.get(nearBusstops.firstKey());
		Corner nearestCornerWithBusstopEnd = nearestBusstop.corner();
		
		List<CityLocation> path = new ArrayList<CityLocation>();
		
		Busstop busstopToTake;
		boolean busDirection;
		
		try {
			busDirection = isBusstopDistancePositive
					(nearestCornerWithBusstopStart,
					nearestCornerWithBusstopEnd);
		} catch (Exception e) {
			busDirection = false;
			e.printStackTrace();
		}
		
		busstopToTake = nearestCornerWithBusstopStart.getBusstopWithDirection
				(busDirection);
		
		
		List<CityLocation> fromAToBusstop = routeWithoutBus
				(posA, busstopToTake.position());
		List<CityLocation> fromCornerToB = routeWithoutBus
				(nearestCornerWithBusstopEnd.position(), posB);
		
		for (CityLocation location : fromAToBusstop) {
			path.add(location);
		}
		path.add(busstopToTake);
		for (CityLocation location : fromCornerToB) {
			path.add(location);
		}
		
		return path;
	}
	
	// TODO make it use actual distance?
	private boolean isBusstopDistancePositive(
			Corner nearestCornerWithBusstopStart,
			Corner nearestCornerWithBusstopEnd) throws Exception {
		int startIndex = busRoute.indexOf(nearestCornerWithBusstopStart);
		int endIndex = busRoute.indexOf(nearestCornerWithBusstopEnd);
		
		if (startIndex < 0 || endIndex < 0) {
			throw new Exception("isBusstopDistancePositiveOrNegative "
					+ "method received a corner that is not part of"
					+ "the bus route");
		}
		
		if (((endIndex - startIndex) % busRoute.size())
				<= busRoute.size()/2) {
			return true;
		} else return false;
				
	}

	private List<CityLocation> routeWithoutBus(XYPos posA, XYPos posB) {
		SortedMap<Integer, CityLocation> nearCorners = placesNearMe(posA, 
				LocationTypeEnum.Corner);
		
		//Getting the 2 corners closest to A
		List<Corner> cornersInit = new ArrayList<Corner>();
		cornersInit.add((Corner)nearCorners.get(nearCorners.firstKey()));
		nearCorners.remove(nearCorners.firstKey());
		cornersInit.add((Corner)nearCorners.get(nearCorners.firstKey()));
		
		//Getting the 2 corners closest to B
		nearCorners = placesNearMe(posB, LocationTypeEnum.Corner);
		List<Corner> cornersEnd = new ArrayList<Corner>();
		cornersEnd.add((Corner)nearCorners.get(nearCorners.firstKey()));
		nearCorners.remove(nearCorners.firstKey());
		cornersEnd.add((Corner)nearCorners.get(nearCorners.firstKey()));
		
		
		//Determining shortest path
		Corner startCorner, endCorner;
		startCorner = endCorner = null;
		int minDistance = 2147483647;
		
		for (int i = 0; i < 2; i++) for (int j = 0; j < 2; j++) {
			int distance = 0;
			distance += distanceHeuristic(posA,
					cornersInit.get(i).position());
			distance += distanceHeuristic(cornersInit.get(i).position(),
					cornersEnd.get(j).position());
			distance += distanceHeuristic(cornersEnd.get(j).position(),
					posB);
			
			if (distance < minDistance) {
				startCorner = cornersInit.get(i);
				endCorner = cornersEnd.get(j);
				minDistance = distance;
			}
		}
		
		//Determining in what direction to move;
		DirectionEnum horizontalDir, verticalDir;
		
		if (endCorner.position().x > startCorner.position().x) {
			horizontalDir = DirectionEnum.East;
		} else horizontalDir = DirectionEnum.West;
		if (endCorner.position().y > startCorner.position().y) {
			verticalDir = DirectionEnum.North;
		} else verticalDir = DirectionEnum.South;
		
		//Building path
		List<CityLocation> path = new ArrayList<CityLocation>();
		path.add(startCorner);
		Corner currentCorner = startCorner;
		while (Math.abs(currentCorner.position().x - 
				currentCorner.getCornerForDir(horizontalDir).position().x)
				> DISTANCE_THRESHOLD_FOR_SAME_COORDINATE) {
			currentCorner = currentCorner.getCornerForDir(horizontalDir);
			path.add(currentCorner);
		}
		
		
		while (Math.abs(currentCorner.position().y - 
				currentCorner.getCornerForDir(verticalDir).position().y)
				> DISTANCE_THRESHOLD_FOR_SAME_COORDINATE) {
			currentCorner = currentCorner.getCornerForDir(verticalDir);
			path.add(currentCorner);
		}
		
		
		return path;
	}

	@Override
	public List<CityLocation> routeFromAToB(CityLocation A, CityLocation B) {
		XYPos posA = getEntrancePosition(A);
		return routeFromAToB(posA, B);
	}

	@Override
	public SortedMap<Integer, CityLocation> placesNearMe
								(XYPos me, LocationTypeEnum type) {
		
		SortedMap<Integer, CityLocation> response = 
				new TreeMap<Integer, CityLocation>();
		
		for (CityLocation loc : locations) if (loc.type() == type) {
			response.put(distanceHeuristic(me, getEntrancePosition(loc)),loc);
		}
		
		return response;
	}

	@Override
	public SortedMap<Integer, CityLocation> placesNearMe(CityLocation me,
			LocationTypeEnum type) {
		return placesNearMe(me.position(), type);
	}
	
	private int distanceHeuristic(XYPos A, XYPos B) {
		return Math.abs(A.x - B.x) + Math.abs(A.y - B.y);
	}
	
	
	private XYPos getEntrancePosition(CityLocation loc) {
		if (loc.type() == LocationTypeEnum.Corner ||
			loc.type() == LocationTypeEnum.Busstop) {
			return loc.position();
		}
		CityBuilding building = (CityBuilding)loc;
		
		XYPos response = new XYPos();
		response.x = building.position().x + building.entrancePos().x;
		response.y = building.position().y + building.entrancePos().y;
		
		return response;
		
	}

}
