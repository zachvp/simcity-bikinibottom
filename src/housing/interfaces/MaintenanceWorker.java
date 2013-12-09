package housing.interfaces;

public interface MaintenanceWorker {
	public void msgFileWorkOrder(Dwelling dwelling);

	public void msgAtDestination();
	
	public void msgHereIsPayment(double payment);

	public void setDwelling(Dwelling dwelling);

}
