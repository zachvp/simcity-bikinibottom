package restaurant.vdea;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Menu {
	private Food chicken = new Food("chicken");
	private Food salad = new Food("salad");
	private Food pizza = new Food("pizza");
	private Food steak = new Food("steak");
	public List<Food> list = new ArrayList<Food>();
	
	Menu(){
		list.add(steak);
		list.add(chicken);
		list.add(salad);
		list.add(pizza);		
	}
	
	
	public void updateMenu(Food f, boolean inStock){
		if(inStock){
			list.add(f);
		}
		else{
			list.remove(f);
		}
	}
	
	public String choose(int num){
		return list.get(num).getName();
	}
	
	public String whatCanIAfford(double cash){
		List<Food> affordable = new ArrayList<Food>();
		for (Food f: list){
			if (cash > f.price){
				affordable.add(f);
			}
		}
		if(affordable.size()==0){
			return "nothing";
		}
		else{
			Random r = new Random();
			int randChoice = r.nextInt(affordable.size());
			return affordable.get(randChoice).getName();
		}
	}
	
	private Food toFood(String s){
		if(s.equals("chicken")){
			return chicken;
		}
		if(s.equals("steak")){
			return steak;
		}
		if(s.equals("pizza")){
			return pizza;
		}
		if(s.equals("salad")){
			return salad;
		}
		return steak;
	}
	
	public String reorder(String oldChoice, double cash){
		list.remove(toFood(oldChoice));
		List<Food> affordable = new ArrayList<Food>();
		for (Food f: list){
			if (cash > f.price && !(f.getName().equals(oldChoice))){
				affordable.add(f);
			}
		}
		if(affordable.size()==0){
			return "nothing";
		}
		else{
			String newChoice = oldChoice;
			//while(newChoice.equals(oldChoice)){ //while newChoice == reorder, pick again
				Random r = new Random();
				int randChoice = r.nextInt(affordable.size());
				newChoice = affordable.get(randChoice).getName();
			//}
			
			return newChoice;
		}
		
	}
	
	public double mostExpensivePrice(){
		double max = 0;
		double test;
		for (Food f: list){
			test = f.price;
			if(test > max){
				max = test;
			}
		}
		return max;
	}
	
	public double cheapestPrice(){
		double min = 0;
		double test;
		for (Food f: list){
			test = f.price;
			if(test < min){
				min = test;
			}
		}
		return min;
	}
	
	
	
	public double getPrice(Food f){
		return f.price;
	}
	
	public double getPrice(String f){
		if(f.equals("chicken")){
			return chicken.price;
		}
		if(f.equals("steak")){
			return steak.price;
		}
		if(f.equals("pizza")){
			return pizza.price;
		}
		if(f.equals("salad")){
			return salad.price;
		}
		
		return 0;
	}
	
	public int numOfItems(){
		return list.size();
	}
	

}
