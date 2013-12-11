package housing.test.mock;

import mock.EventLog;
import housing.interfaces.DwellingLayoutGui;
import housing.interfaces.Resident;
import housing.interfaces.ResidentGui;

public class MockResidentGui implements ResidentGui {
	EventLog log = new EventLog();
	Resident resident;
	DwellingLayoutGui layoutGui;

	public MockResidentGui(Resident resident) {
		this.resident = resident;
	}

	@Override
	public void DoGoToStove() {
		log.add("At stove");
		resident.msgAtDestination();
	}

	@Override
	public void DoGoToTable() {
		log.add("At Table");
		resident.msgAtDestination();
	}

	@Override
	public void DoGoToRefrigerator() {
		log.add("At Refrigerator");
		resident.msgAtDestination();
	}

	@Override
	public void DoJazzercise() {
		log.add("Doing Jazzercise");
	}

	@Override
	public void setFood(boolean type) {
		log.add("Setting food to " + type);
	}

	@Override
	public void DoMoveGary() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresent(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoShowSpeech(String speech) {
		log.add("Say " + speech);
	}

	@Override
	public DwellingLayoutGui getLayoutGui() {
		return layoutGui;
	}

	@Override
	public void setLayoutGui(DwellingLayoutGui mockLayoutGui) {
		this.layoutGui = mockLayoutGui;
	}
}
