package housing.test.mock;

import java.awt.Dimension;

import housing.interfaces.DwellingLayoutGui;
import mock.EventLog;

public class MockLayoutGui implements DwellingLayoutGui {
	EventLog log = new EventLog();

	public MockLayoutGui() { }

	@Override
	public void DoMoveGary() {
		log.add("Moving Gary.");
	}

	@Override
	public Dimension getTablePosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dimension getStovePosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dimension getRefrigeratorPosition() {
		// TODO Auto-generated method stub
		return null;
	}

}
