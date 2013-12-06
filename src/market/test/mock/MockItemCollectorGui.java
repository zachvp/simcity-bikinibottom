package market.test.mock;


import java.awt.Graphics2D;
import java.util.List;



import market.gui.MarketInfoPanel;
import market.interfaces.ItemCollector;
import market.interfaces.ItemCollectorGuiInterfaces;

public class MockItemCollectorGui extends Mock implements ItemCollectorGuiInterfaces {

	public EventLog log = new EventLog();
	ItemCollector itemCollector;
	public MockItemCollectorGui(ItemCollector ic) {
		super("Mock ItemCollector Gui");
		itemCollector = ic;
		// TODO Auto-generated constructor stub
	}
	@Override
	public void setItemCollectorNumber(int i) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setMarketControlPanel(MarketInfoPanel p) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void UpdateInventoryLevel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void BackReadyStation() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Got the message and go to Home position now "));
		itemCollector.Ready();
	}
	@Override
	public void CollectItems() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Got the message and go to Bench now "));
		itemCollector.AtCollectStation();
	}
	@Override
	public void OffWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public int getXPos() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getYPos() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void GoToWork() {
		// TODO Auto-generated method stub
		
	}
	

}