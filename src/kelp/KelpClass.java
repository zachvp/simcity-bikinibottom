package kelp;

import java.util.ArrayList;
import java.util.List;

import transportation.interfaces.Busstop;
import transportation.interfaces.Corner;
import CommonSimpleClasses.CityBuilding;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.DirectionEnum;
import CommonSimpleClasses.CityLocation.LocationTypeEnum;
import CommonSimpleClasses.XYPos;

public class KelpClass implements Kelp {
	
	// TODO SET CORRECT CONSTANTS
	private static final double AVG_BUS_WAIT = 15;
	private static final double BUS_SPEED = 80; 
	private static final double WALKING_SPEED = 30;
	private static final int DISTANCE_THRESHOLD_FOR_SAME_COORDINATE = 8;
	List<CityLocation> locations;
	
	List<Corner> busRoute;
	
	static KelpClass instance = null;
	
	static KelpClass getKelpInstance() throws Exception {
		if (instance != null) return instance;
		else throw new Exception("Tried to grab a Kelp instance "
				+ "without it having been instantiated.");
	}
	
	public KelpClass(List<CityLocation> locations, 
			List<Corner> busRoute) throws Exception {
		
		if (instance == null) {
			this.locations = new ArrayList<CityLocation> (locations);
			this.busRoute = new ArrayList<Corner> (busRoute);
			instance = this;
		} else {
			throw new Exception("Tried to create a second instance of Kelp, "
					+ "Kelp is a singleton.");
		}
	}

	@Override
	public List<CityLocation> placesNearMe
								(XYPos me, LocationTypeEnum type) {
		
		List<CityLocation> response = 
				new ArrayList<CityLocation>();
		
		for (CityLocation loc : locations) if (loc.type() == type) {
			if (response.size() == 0) {
				response.add(loc);
			} else {
				boolean inserted = false;
				for (int i = 0; i < response.size(); i++){
					if (distanceHeuristic(me, response.get(i).position())
							> distanceHeuristic(me, loc.position()) ) {
						response.add(i, loc);
						inserted = true;
						break;
					}
				}
				if (!inserted) response.add(loc);
			}
		}
		
		return response;
	}

	@Override
	public List<CityLocation> placesNearMe(CityLocation me,
			LocationTypeEnum type) {
		return placesNearMe(me.position(), type);
	}

	@Override
	public List<CityLocation> routeFromAToB(XYPos A, 
			CityLocation B, Boolean tryBus) {
		
		XYPos posA = A;
		XYPos posB = getAbsoluteEntrancePosition(B);
		
		List<CityLocation> response;
		
		List<CityLocation> pathWithoutBus = routeWithoutBus(posA,posB);
		
		if (tryBus) {
			try {
				List<CityLocation> pathWithBus = routeWithBus(posA,posB);
				response = returnFastestPath(pathWithBus,pathWithoutBus, posA);
			} catch (Exception e){
				e.printStackTrace();
				System.out.println("Exception when routing with bus, "
						+ "will walk instead.");
				response = pathWithoutBus;
			}
		} else {
			response = pathWithoutBus;
		}
		
		response.add(B);
		
		return response;
	}

	@Override
	public List<CityLocation> routeFromAToB(CityLocation A, 
			CityLocation B, Boolean tryBus){
		XYPos posA = getAbsoluteEntrancePosition(A);
		return routeFromAToB(posA, B, tryBus);
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

	// TODO Make accurate when car?
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
						/ speed;
				pastPos = loc.position();
				wasBusstop = false;
			}
		}
		return time;
	}

	private List<CityLocation> routeWithBus(XYPos posA, XYPos posB) throws Exception {
		List<CityLocation> nearBusstops = placesNearMe(posA, 
				LocationTypeEnum.Busstop);
		Busstop nearestBusstop = 
				(Busstop)nearBusstops.get(0);
		Corner nearestCornerWithBusstopStart = nearestBusstop.corner();
		
		nearBusstops = placesNearMe(posB, 
				LocationTypeEnum.Busstop);
		nearestBusstop = (Busstop)nearBusstops.get(0);
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
	
	private List<CityLocation> routeWithoutBus(XYPos posA, XYPos posB) {
		List<CityLocation> nearCorners = placesNearMe(posA, 
				LocationTypeEnum.Corner);
		
		//Getting the 2 corners closest to A
		List<Corner> cornersInit = new ArrayList<Corner>();
		cornersInit.add((Corner)nearCorners.get(0));
		cornersInit.add((Corner)nearCorners.get(1));
		
		//Getting the 2 corners closest to B
		nearCorners = placesNearMe(posB, LocationTypeEnum.Corner);
		List<Corner> cornersEnd = new ArrayList<Corner>();
		cornersEnd.add((Corner)nearCorners.get(0));
		cornersEnd.add((Corner)nearCorners.get(1));
		
		
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
		
		try {
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
		} catch (Exception x) {
			x.printStackTrace();
			System.out.println("Exception will cause an empty path "
					+ "to be returned now, a passenger might get lost.");
			return new ArrayList<CityLocation>();
		}
		
		
		return path;
	}

	// TODO make it use actual distance instead of manhattan?
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

	private int distanceHeuristic(XYPos A, XYPos B) {
		return Math.abs(A.x - B.x) + Math.abs(A.y - B.y);
	}
	
	
	private XYPos getAbsoluteEntrancePosition(CityLocation loc) {
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

	@Override
	public List<Corner> busRoute() {
		return new ArrayList<Corner>(busRoute);
	}

}
