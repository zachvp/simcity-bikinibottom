package housing.interfaces;

import housing.gui.LayoutGui;

public interface ResidentGui {
	public void DoGoToStove();
	
	public void DoGoToTable();
	
	public void DoGoToRefrigerator();
	
	public void DoJazzercise();

	public void setFood(String type);

	public void setLayoutGui(LayoutGui layoutGui);
}
