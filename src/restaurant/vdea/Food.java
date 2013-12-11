package restaurant.vdea;

public class Food {
	String name;
	int cookTime;
	int quantity = 0;
	int threshold = 2;
	int capacity = 10;
	int newStock;
	double price;
	
	Food(){
		
	}
	
	Food(String n){
		this(n, 0);
	}
	
	Food(String n, int initAmount){
		name = n;
		quantity = initAmount;
		newStock = initAmount;
		
		if(name.equals("Krabby Patty")){
			cookTime = 8;
			price = 5.99;
		}
		else if(name.equals("Kelp Shake")){
			cookTime = 4;
			price = 1.99;
		}
		else if(name.equals("Coral Bits")){
			cookTime = 5;
			price = 2.99;
		}
		else if(name.equals("Kelp Rings")){
			cookTime = 6;
			price = 3.99;
		}
	}
	
	public String getName(){
		return name;
	}
	
	public int getCookTime(){
		return this.cookTime;
	}
	
	public void cook(){
		quantity-=1;
	}
	
	public void sell(int num){
		quantity -= num; 
		newStock -= num;
	}
	
	public void addInventory(int num){
		quantity += num;
	}
	
	public void setInventory(int num){
		quantity = num;
	}
	
	public int getInventory(){
		return quantity;
	}
	
	public boolean equals(Food f){
		return f.getName().equals(this.name);
	}
	
	public String toString(){
		return name;
	}
	
}
