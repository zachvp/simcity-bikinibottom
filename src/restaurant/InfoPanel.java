package restaurant;

import gui.Building;
import gui.trace.AlertTag;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import market.Item;
import restaurant.anthony.Food;
import CommonSimpleClasses.Constants;

public class InfoPanel extends JPanel{

	private Dimension d, panelDim, innerPanelDim;
	private JLabel buildingName, menuLabel, inventoryLabel, 
					krabbyPatty, kelpShake, coralBits, kelpRings,
					krabbyPattyPrice, kelpShakePrice, coralBitsPrice, kelpRingsPrice,
					krabbyPatty1, kelpShake1, coralBits1, kelpRings1,
					krabbyPattyInventory, kelpShakeInventory, coralBitsInventory, kelpRingsInventory;
	JPanel menu, inventory, menuP, inventoryP;
	
	
	public InfoPanel(Building b) {
		d = new Dimension(Constants.INFO_PANEL_WIDTH, Constants.INFO_PANEL_HEIGHT);
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		setLayout(new BorderLayout());
		
		JPanel south = new JPanel();
		south.setLayout(new BorderLayout());
		south.setPreferredSize(new Dimension(d.width, (int)(d.height*0.8)));

		buildingName = new JLabel("Welcome to " + b.getName(), JLabel.CENTER);
		Font font = new Font("Serif", Font.BOLD, 18);
		buildingName.setFont(font);
		
		panelDim = new Dimension((int)(d.width*0.5), (int)(d.height*0.8));
		innerPanelDim = new Dimension(panelDim.width, (int)(panelDim.height*0.8));
		
		makeMenuPanel();
		makeInventoryPanel();
		
		south.add(menuP, BorderLayout.WEST);
		south.add(inventoryP, BorderLayout.EAST);
		
		add(buildingName, BorderLayout.NORTH);
		add(south, BorderLayout.SOUTH);
	}
	
	private void makeMenuPanel() {
		menuP = new JPanel();
		menuP.setPreferredSize(panelDim);
		menuP.setLayout(new BorderLayout());
		menuP.setBorder(BorderFactory.createLineBorder(Color.black));
		
		menuLabel = new JLabel("Menu", JLabel.CENTER);
		Font font = new Font("Serif", Font.BOLD, 16);
		menuLabel.setFont(font);
		
		menu = new JPanel();
		menu.setPreferredSize(innerPanelDim);
		menu.setLayout(new GridLayout(4,2,1,1));
		krabbyPatty = new JLabel("Krabby Patty", JLabel.CENTER);
		kelpShake = new JLabel("Kelp Shake", JLabel.CENTER);
		coralBits = new JLabel("Coral Bits", JLabel.CENTER);
		kelpRings = new JLabel("Kelp Rings", JLabel.CENTER);
		krabbyPattyPrice = new JLabel("", JLabel.CENTER);
		kelpShakePrice = new JLabel("", JLabel.CENTER);
		coralBitsPrice = new JLabel("", JLabel.CENTER);
		kelpRingsPrice = new JLabel("", JLabel.CENTER);
		
		menu.add(krabbyPatty);
		menu.add(krabbyPattyPrice);
		menu.add(kelpShake);
		menu.add(kelpShakePrice);
		menu.add(coralBits);
		menu.add(coralBitsPrice);
		menu.add(kelpRings);
		menu.add(kelpRingsPrice);
		
		menuP.add(menuLabel, BorderLayout.NORTH);
		menuP.add(menu, BorderLayout.SOUTH);		
	}

	private void makeInventoryPanel() {
		inventoryP = new JPanel();
		inventoryP.setPreferredSize(panelDim);
		inventoryP.setLayout(new BorderLayout());
		inventoryP.setBorder(BorderFactory.createLineBorder(Color.black));
		
		inventoryLabel = new JLabel("Inventory", JLabel.CENTER);
		Font font = new Font("Serif", Font.BOLD, 16);
		inventoryLabel.setFont(font);
		
		inventory = new JPanel();
		inventory.setPreferredSize(innerPanelDim);
		inventory.setLayout(new GridLayout(4,2,1,1));
		
		krabbyPatty1 = new JLabel("Krabby Patty", JLabel.CENTER);
		kelpShake1 = new JLabel("Kelp Shake", JLabel.CENTER);
		coralBits1 = new JLabel("Coral Bits", JLabel.CENTER);
		kelpRings1 = new JLabel("Kelp Rings", JLabel.CENTER);
		krabbyPattyInventory = new JLabel("", JLabel.CENTER);
		kelpShakeInventory = new JLabel("", JLabel.CENTER);
		coralBitsInventory = new JLabel("", JLabel.CENTER);
		kelpRingsInventory = new JLabel("", JLabel.CENTER);
		
		inventory.add(krabbyPatty1);
		inventory.add(krabbyPattyInventory);
		inventory.add(kelpShake1);
		inventory.add(kelpShakeInventory);
		inventory.add(coralBits1);
		inventory.add(coralBitsInventory);
		inventory.add(kelpRings1);
		inventory.add(kelpRingsInventory);
		
		inventoryP.add(inventoryLabel, BorderLayout.NORTH);
		inventoryP.add(inventory, BorderLayout.SOUTH);
		
	}

	public void setBuildingName(String name){
		buildingName.setText("Welcome to " + name);
	}

	public void setKrabbyPattyPrice(String price){
		krabbyPattyPrice.setText("$" + price);
	}
	public void setKelpShakePrice(String price){
		kelpShakePrice.setText("$" + price);
	}
	public void setCoralBitsPrice(String price){
		coralBitsPrice.setText("$" + price);
	}
	public void setKelpRingsPrice(String price){
		kelpRingsPrice.setText("$" + price);
	}
	
	public void setKrabbyPattyInventory(int n){
		krabbyPattyInventory.setText("" + n);
	}
	public void setKelpShakeInventory(int n){
		kelpShakeInventory.setText("" + n);
	}
	public void setCoralBitsInventory(int n){
		coralBitsInventory.setText("" + n);
	}
	public void setKelpRingsInventory(int n){
		kelpRingsInventory.setText("" + n);
	}
	
	public void UpdateInfoPanel(Map<String, Integer> inventoryList){
		for (Map.Entry<String, Integer> entry : inventoryList.entrySet()) {
			int currentAmount = entry.getValue();
			String currentName = entry.getKey();
			if (currentName == "Krabby Patty"){
				setKrabbyPattyInventory(currentAmount);
			}
			else if (currentName == "Kelp Shake"){
				setKelpShakeInventory(currentAmount);
			}
			else if (currentName == "Coral Bits"){
				setCoralBitsInventory(currentAmount);
			}
			else if (currentName == "Kelp Rings"){
				setKelpRingsInventory(currentAmount);
			}
		}
		return;
	}
}
