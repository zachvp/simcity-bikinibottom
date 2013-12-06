package market;


public class Item{

	public String name;
	public int amount;
	
	public Item (String NA, int am){
		name = NA;
		amount = am;
	}
	

	public void ItemConsumed(int i){
		amount = amount - i;
	}
	
	public void ItemAdded(int i){
		amount = amount + i;
	}
	
	public void ItemEqual (int i){
		amount = i;
	} 
}
