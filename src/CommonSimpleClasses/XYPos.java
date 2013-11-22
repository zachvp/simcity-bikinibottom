package CommonSimpleClasses;

import com.sun.org.apache.xpath.internal.operations.Equals;


//Describes a position in the city map.
public class XYPos {
	public int x;
	public int y;
	
	public XYPos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public XYPos() {}
	
	public boolean equals(Object obj) {
		if (obj instanceof XYPos) {
			XYPos pos = (XYPos) obj;
			if (this.x == pos.x
				&& this.y == pos.y) {
				return true;
			} else {
				return false;
			}
		} else return false;
	};
	
	public String toString() {
		return "X=" + x + " Y=" + y;
	};
}
