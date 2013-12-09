package housing.test.mock;

import java.awt.Dimension;

import housing.interfaces.DwellingLayoutGui;
import mock.EventLog;

public class MockDwellingGui implements DwellingLayoutGui {
	EventLog log = new EventLog();

	public MockDwellingGui() { }

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

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

}
