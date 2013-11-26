package housing.interfaces;

public interface MaintenanceWorkerGui {
	public void setTool(boolean b);

	public void DoGoToDwelling(int unitNumber);

	public void DoFixProblem();

	public void DoReturnHome(int unit);
}
