package market;


public class Item{

	public String name;
	public int amount;
	
	public void ItemConsumed(int i){
		amount = amount - i;
	}
	
	public void ItemAdded(int i){
		amount = amount + i;
	}
}
