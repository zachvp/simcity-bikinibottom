package housing.interfaces;

import housing.backend.HousingComplex;

public interface MaintenanceWorkerGui {
	public void setTool(boolean b);

	public void DoGoToDwelling(int unitNumber);

	public void DoFixProblem();

	public void DoReturnHome(int unit);

	public void setComplex(HousingComplex complex);
}
