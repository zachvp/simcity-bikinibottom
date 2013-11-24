package market.test.mock;


import java.awt.Graphics2D;
import java.util.List;









import market.CashierRole;
import market.Item;
import market.gui.MarketInfoPanel;
import market.interfaces.Cashier;
import market.interfaces.CashierGuiInterfaces;
import market.interfaces.Customer;

public class MockCashierGui extends Mock implements CashierGuiInterfaces {

	Cashier cashier;
	public MockCashierGui(CashierRole ca) {
		super("Mock CashierGui");
		cashier = ca;
		// TODO Auto-generated constructor stub
	}

	public EventLog log = new EventLog();

	@Override
	public void setMarketInfoPanel(MarketInfoPanel p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GoToFrontDesk() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Got the message and go to Front Desk now "));
		cashier.AtFrontDesk();
	}

	@Override
	public void GoToBench() {
		// TODO Auto-generated method stub
		
		log.add(new LoggedEvent("Got the message and go to Bench now "));
		cashier.AtBench();
	}

	@Override
	public void OffWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ContinueOffWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Update() {
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
	

	

	

}
