package transportation.mapbuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import CommonSimpleClasses.DirectionEnum;
import CommonSimpleClasses.XYPos;
import agent.Constants;
import transportation.BusstopAgent;
import transportation.CornerAgent;
import transportation.interfaces.Corner;

/* Handles the construction of the underlying 
 * Corner node map.
 */

// TODO add comments
public class MapBuilder {
	static boolean hasBeenCreated = false;
	static int numberOfBlocks = 0;
	static int buildingRows = 0;
	static List<Corner> createdCorners = new ArrayList<Corner>();
	static List<Corner> busRoute = new ArrayList<Corner>();
	private static Set<Integer> cornersWithBusstops;
	private static int numCornerRows = 0;
	private static int numCornerCols = 0;
	private static List<XYPos> cornersWithBusstopsGrid =
			new ArrayList<XYPos>();
	
	// Check bus stop rules before calling!
	static void createMap(int numberOfBlocks, Set<Integer> cornersWithBusstops)
			throws Exception {
		if (numberOfBlocks % Constants.MAX_BLOCK_COL != 0) {
			throw new Exception("numberOfBlocks should be multiple "
					+ "of MAX_BLOCK_COL");
		}
		
		if(!hasBeenCreated) {
			hasBeenCreated = true;
			
			MapBuilder.numberOfBlocks = numberOfBlocks;
			MapBuilder.buildingRows = numberOfBlocks / Constants.MAX_BLOCK_COL;
			MapBuilder.numCornerRows = buildingRows-1;
			MapBuilder.numCornerCols = Constants.MAX_BLOCK_COL-1;
			MapBuilder.cornersWithBusstops = cornersWithBusstops;
			
			createCorners();
			connectCorners();
			addBusstops();
			buildBusroute();
			startThreads();
			
		} else {
			throw new Exception("Tried to create a second map");
		}
	}

	private static List<Corner> createCorners() {
		
		
		for (int curRow = 1; curRow < buildingRows; curRow++) {
			for (int curCol = 1; curCol < Constants.MAX_BLOCK_COL;
					curCol++) {
	
				int cornerX = Constants.MAP_MARGIN_X 
						+ curRow*Constants.BUILDING_WIDTH
						+ (curRow-1)*Constants.SPACE_BETWEEN_BUILDINGS
						+ Constants.SPACE_BETWEEN_BUILDINGS/2;
				int cornerY = Constants.MAP_MARGIN_Y 
						+ curRow*Constants.BUILDING_HEIGHT
						+ (curRow-1)*Constants.SPACE_BETWEEN_BUILDINGS
						+ Constants.SPACE_BETWEEN_BUILDINGS/2;
	
				createdCorners.add(
						new CornerAgent( new XYPos(cornerX, cornerY)) );
			}
		}
		return createdCorners;
	}

	private static void connectCorners() {
		for (int j = 0; j < numCornerRows; j++) {
			for (int i = 0; i < numCornerCols; i++) {
				Corner currentCorner = 
						getCorner2D(i, j);
				
				//Adding north corner
				if (j != 0) {
					currentCorner.addAdjacentCorner
						(getCorner2D(i, j-1),
							DirectionEnum.North);
				}
				
				//Adding south corner
				if (j != numCornerRows-1) {
					currentCorner.addAdjacentCorner
						(getCorner2D(i, j+1),
						DirectionEnum.South);
				}
				
				//Adding west corner
				if (i != 0) {
					currentCorner.addAdjacentCorner
						(getCorner2D(i-1, j),
						DirectionEnum.West);
				}
				
				//Adding east corner
				if (i != numCornerCols-1) {
					currentCorner.addAdjacentCorner
						(getCorner2D(i+1, j),
						DirectionEnum.East);
				}
			}
		}
	}
	
	/* Returns the corner that is ith from the left 
	 * and jth from the top, starting with 0.
	 */
	private static Corner getCorner2D(int i, int j) {
		return createdCorners.get(getIndex2D(i, j));
	}

	/* Returns the index that is ith from the left 
	 * and jth from the top, starting with 0.
	 */
	private static int getIndex2D(int i, int j) {
		return j*numCornerCols + i;
	}
	

	/* Adds bus stops to the corresponding corners, creating a 
	 * counterclockwise route that starts with the first leftmost,
	 * then uppermost bus stop.
	 */
	private static void addBusstops() throws Exception {
		for (Integer num : cornersWithBusstops) {
			if (num > createdCorners.size()) {
				throw new Exception("Tried to add corner to "
						+ "non-existant corner #" + num);
			}
		}
		
		
		
		
		
		
		//Checking for first corner needing busstop on left edge
		int i, j;
		i = j = 0;
		for (i = 0; i < numCornerCols; i++) {
			for (j = 0; j < numCornerRows; j++) {
				if (cornersWithBusstops.contains(getIndex2D(i, j))){
					break;
				}
			}
		}
		
		if (i == numCornerCols || j == numCornerRows) {
			throw new Exception("Couldn't find first bus stop"
					+ " for some reason.");
		}
		
		//Adding busstops to corner
		for(;j < numCornerRows; j++) {
			if(cornersWithBusstops.contains(getIndex2D(i, j)))
				addBusstopsToCorner(i, j, true);
		}
		
		
		
		
		
		
		
		
		//Checking for first corner needing busstop on bottom edge
		for (j = numCornerRows-1; j > 0; j--) {
			for (i = 0; i < numCornerCols; i++) {
				if (cornersWithBusstops.contains(getIndex2D(i, j))){
					break;
				}
			}
		}

		if (i == numCornerCols || j == numCornerRows) {
			throw new Exception("Couldn't find first bus stop"
					+ " for some reason.");
		}

		//Adding busstops to corner
		for(;i < numCornerCols; i++) {
			if(cornersWithBusstops.contains(getIndex2D(i, j)))
				addBusstopsToCorner(i, j, false);
		}
		
		
		
		
		
		
		
		//Checking for first corner needing busstop on right edge
		for (i = numCornerCols-1; i > 0; i--) {
			for (j = numCornerRows-1; j > 0; j--) {
				if (cornersWithBusstops.contains(getIndex2D(i, j))){
					break;
				}
			}
		}
		
		if (i == numCornerCols || j == numCornerRows) {
			throw new Exception("Couldn't find first bus stop"
					+ " for some reason.");
		}
		
		//Adding busstops to corner
		for(; j >= 0; j--) {
			if(cornersWithBusstops.contains(getIndex2D(i, j)))
				addBusstopsToCorner(i, j, true);
		}
		
		
		
		
		
		
		//Checking for first corner needing busstop on top edge
		
		for (j = 0; j < numCornerRows; j++){
			for (i = numCornerCols-1; i > 0; i--) {
				if (cornersWithBusstops.contains(getIndex2D(i, j))){
					break;
				}
			}
		}

		if (i == numCornerCols || j == numCornerRows) {
			throw new Exception("Couldn't find first bus stop"
					+ " for some reason.");
		}

		//Adding busstops to corner
		for(; i >= 0; i--) {
			if(cornersWithBusstops.contains(getIndex2D(i, j)))
				addBusstopsToCorner(i, j, true);
		}

	}

	private static void addBusstopsToCorner(int i, int j,
			boolean isVertical) {
		
		cornersWithBusstopsGrid.add(new XYPos(i,j));
		
		DirectionEnum d1;
		DirectionEnum d2;
		
		if (isVertical) {
			d1 = DirectionEnum.North;
			d2 = DirectionEnum.South;
		} else {
			d1 = DirectionEnum.West;
			d2 = DirectionEnum.East;
		}
		
		
		getCorner2D(i, j).addBusstop(
				new BusstopAgent(getCorner2D(i, j), d1));
		getCorner2D(i, j).addBusstop(
				new BusstopAgent(getCorner2D(i, j), d2));
	}

	private static void buildBusroute() {
		int minI, maxI, minJ, maxJ;
		minI = numCornerCols;
		minJ = numCornerRows;
		maxI = 0;
		maxJ = 0;
		
		for (XYPos gridPos : cornersWithBusstopsGrid) {
			if (gridPos.x < minI) {
				minI = gridPos.x;
			}
			
			if (maxI < gridPos.x) {
				maxI = gridPos.x;
			}
			
			if (gridPos.y < minJ) {
				minJ = gridPos.y;
			}
			
			if (maxJ < gridPos.y) {
				maxJ = gridPos.y;
			}
		}
		
		//creating left edge
		for (int j = minJ; j < maxJ; j++) {
			busRoute.add(getCorner2D(minI, j));
		}
		
		//creating bottom edge
		for (int i = minI; i < maxI; i++) {
			busRoute.add(getCorner2D(i, maxJ));
		}
		
		//creating right edge
		for (int j = maxJ; j > minJ; j--) {
			busRoute.add(getCorner2D(maxI, j));
		}
		
		//creating top edge
		for (int i = maxI; i > minI; i--) {
			busRoute.add(getCorner2D(i, minJ));
		}
	}

	private static void startThreads() {
		for (Corner corner : createdCorners) {
			corner.startThreads();
		}
	}

}
