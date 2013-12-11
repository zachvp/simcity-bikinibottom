package restaurant.vonbeck;

public class Table {
	CustomerRole occupiedBy;
	public int tableNumber;

	public Table(int tableNumber) {
		this.tableNumber = tableNumber;
	}

	public void setOccupant(CustomerRole cust) {
		occupiedBy = cust;
	}

	public void setUnoccupied() {
		occupiedBy = null;
	}

	public CustomerRole getOccupant() {
		return occupiedBy;
	}

	public boolean isOccupied() {
		return occupiedBy != null;
	}

	public String toString() {
		return "table " + tableNumber;
	}


}
