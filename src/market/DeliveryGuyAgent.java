package market;

import java.util.List;

import market.interfaces.Cashier;
import market.interfaces.CityBuilding;
import market.interfaces.Customer;
import market.interfaces.DeliveryGuy;
import agent.Agent;

public class DeliveryGuyAgent extends Agent implements DeliveryGuy{

	private String name;
	private boolean Available;
	private Cashier cashier;
	private CityBuilding Market;
	
	DeliveryGuyAgent(String NA, CityBuilding MA){
		name = NA;
		Market = MA;
	}
	
	//Scheduler
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	//Messages
	public boolean msgAreYouAvailable() {
		return Available;
	}

	
	public void msgDeliverIt(List<Item> DeliveryList , Customer OrderPerson , CityBuilding building) {
		
		 Available = false;
		
		// animation to go to the location (Building)
		
		 OrderPerson.msgHereisYourItem(DeliveryList);
		
		// animation go back to the market
		
		 Available = true;
	}

	@Override
	public String getMaitreDName() {
		// TODO Auto-generated method stub
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setCashier(Cashier ca){
		cashier = ca;
	}


}