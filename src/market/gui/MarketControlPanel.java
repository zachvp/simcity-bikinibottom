package market.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import market.Item;



public class MarketControlPanel extends JPanel implements ActionListener{
	
	MarketRecords marketRecords;
	
	private JLabel MarketLabel;
	//Market Label
	private JLabel ExpensiveCarInvent = null;
	private JLabel CheapCarInvent = null;
	private JLabel ChickenInvent = null;
	private JLabel PizzaInvent = null;
	private JLabel SandwichInvent = null;

	public JTextField ExpensiveCarText = null;
	public JTextField CheapCarText = null;
	public JTextField ChickenText = null;
	public JTextField PizzaText = null;
	public JTextField SandwichText = null;

	private String ExpensiveCarInventoryLevel = "Current Inventory Level";
	private String CheapCarInventoryLevel = "Current Inventory Level";
	private String PizzaInventoryLevel = "Current Inventory Level";
	private String SandwichInventoryLevel = "Current Inventory Level";
	private String ChickenInventoryLevel = "Current Inventory Level";
	
	private JPanel marketLabel = new JPanel();
	private JPanel group = new JPanel();
	private JButton UpdateButton;

	public MarketControlPanel(MarketRecords rec) {
		
		
		marketRecords = rec;
		group.setLayout(new GridLayout(2, 2, 10, 10));
		initMarketLabel();
		add(MarketLabel);
		
		JPanel UpdatePanel = new JPanel();
        UpdatePanel.setLayout(new GridLayout(1,0));
        UpdateButton = new JButton("Update");
        UpdateButton.addActionListener(this);
        UpdatePanel.add(UpdateButton);
        add(UpdatePanel);
        add(new JLabel("All non-Integers input will be ignored"));
        
       

	}

	/**
	 * Sets up the restaurant label that includes the menu,
	 * and host and cook information
	 */
	private void initMarketLabel() {
		
		
		//Creating the header of the UI
		double cash = marketRecords.ca.getCash();
		JPanel NorthPanel = new JPanel();
		NorthPanel.setLayout(new FlowLayout());
		MarketLabel = new JLabel();
		MarketLabel.setText("Today's Staff :    " + marketRecords.ca.getName() + "                     " + "Market's Current Cash :   " + cash);
		NorthPanel.add(new JLabel("               "));
		NorthPanel.add(MarketLabel);
		NorthPanel.add(new JLabel("               "));

		//Creating the UI
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1,0,0));
		JLabel ExpensiveCarLabel = new JLabel("               ExpensiveCar");
		JLabel CheapCarLabel = new JLabel("               CheapCar");
		JLabel ChickenLabel = new JLabel("               Chicken");
		JLabel PizzaLabel = new JLabel("               Pizza");
		JLabel SandwichLabel = new JLabel("               Sandwich");

		ExpensiveCarInventoryLevel = "Current Inventory Level";
		CheapCarInventoryLevel = "Current Inventory Level";
		PizzaInventoryLevel = "Current Inventory Level";
		SandwichInventoryLevel = "Current Inventory Level";
		ChickenInventoryLevel = "Current Inventory Level";



		ExpensiveCarInvent = new JLabel();
		ExpensiveCarInvent.setText(ExpensiveCarInventoryLevel);
		CheapCarInvent = new JLabel();
		CheapCarInvent.setText(CheapCarInventoryLevel);
		ChickenInvent = new JLabel();
		ChickenInvent.setText(ChickenInventoryLevel);
		PizzaInvent = new JLabel();
		PizzaInvent.setText(PizzaInventoryLevel);
		SandwichInvent = new JLabel();
		SandwichInvent.setText(SandwichInventoryLevel);



		ExpensiveCarText = new JTextField();
		ExpensiveCarText.setEditable(true);
		CheapCarText = new JTextField("");
		CheapCarText.setEditable(true);
		ChickenText = new JTextField("");
		ChickenText.setEditable(true);
		PizzaText = new JTextField("");
		PizzaText.setEditable(true);
		SandwichText = new JTextField("");
		SandwichText.setEditable(true);

		JPanel ExpensiveCarPanel = new JPanel();
		ExpensiveCarPanel.setLayout(new GridLayout(1,0,0,0));
		ExpensiveCarPanel.add(ExpensiveCarLabel);
		ExpensiveCarPanel.add(ExpensiveCarInvent);
		ExpensiveCarPanel.add(ExpensiveCarText);

		JPanel CheapCarPanel = new JPanel();
		CheapCarPanel.setLayout(new GridLayout(1,0,0,0));
		CheapCarPanel.add(CheapCarLabel);
		CheapCarPanel.add(CheapCarInvent);
		CheapCarPanel.add(CheapCarText);

		JPanel ChickenPanel = new JPanel();
		ChickenPanel.setLayout(new GridLayout(1,0,0,0));
		ChickenPanel.add(ChickenLabel);
		ChickenPanel.add(ChickenInvent);
		ChickenPanel.add(ChickenText);

		JPanel PizzaPanel = new JPanel();
		PizzaPanel.setLayout(new GridLayout(1,0,0,0));
		PizzaPanel.add(PizzaLabel);
		PizzaPanel.add(PizzaInvent);
		PizzaPanel.add(PizzaText);

		JPanel SandwichPanel = new JPanel();
		SandwichPanel.setLayout(new GridLayout(1,0,0,0));
		SandwichPanel.add(SandwichLabel);
		SandwichPanel.add(SandwichInvent);
		SandwichPanel.add(SandwichText);


		//Default Panel
		JPanel DefaultPanel = new JPanel();
		DefaultPanel.setLayout(new GridLayout(1,0,0,0));
		DefaultPanel.add(new JLabel("                     Type"));
		DefaultPanel.add(new JLabel("      Current Inventory Level"));
		DefaultPanel.add(new JLabel("      Desire Inventory Level"));

		panel.add(DefaultPanel);
		panel.add(ExpensiveCarPanel);
		panel.add(CheapCarPanel);
		panel.add(ChickenPanel);
		panel.add(PizzaPanel);
		panel.add(SandwichPanel);



		marketLabel.setLayout(new BorderLayout());
		marketLabel.add(NorthPanel, BorderLayout.NORTH);
		marketLabel.add(panel, BorderLayout.CENTER);

		UpdateInventoryLevelWithoutButton();
		
		add(panel);

		/*
        JLabel label = new JLabel();
        marketLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Today's Staff</u></h3><table><tr><td>host:</td><td>" + ca.getName());

        marketLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        marketLabel.add(label, BorderLayout.CENTER);
        marketLabel.add(new JLabel("               "), BorderLayout.EAST);
        marketLabel.add(new JLabel("               "), BorderLayout.WEST);
		 */
	}

	public void UpdateInventoryLevelWithButton(){
		Map<String,Integer> IList = marketRecords.ca.getInventoryList();

		System.out.println("IMHERE");
		if(  isInteger(ExpensiveCarText.getText())  )
			IList.put("ExpensiveCar", Integer.parseInt(ExpensiveCarText.getText()));
		if(  isInteger(CheapCarText.getText())  )
			IList.put("CheapCar", Integer.parseInt(CheapCarText.getText()));
		if(  isInteger(PizzaText.getText())  )
			IList.put("Pizza", Integer.parseInt(PizzaText.getText()));
		if(  isInteger(SandwichText.getText())  )
			IList.put("Sandwich", Integer.parseInt(SandwichText.getText()));
		if(  isInteger(ChickenText.getText())  )
			IList.put("Chicken", Integer.parseInt(ChickenText.getText()));
		
		

		//Printing out the labels
		ExpensiveCarInventoryLevel	= Integer.toString(IList.get("ExpensiveCar"));
		CheapCarInventoryLevel		= Integer.toString(IList.get("CheapCar"));
		PizzaInventoryLevel 		= Integer.toString(IList.get("Pizza"));
		SandwichInventoryLevel		= Integer.toString(IList.get("Sandwich"));
		ChickenInventoryLevel 		= Integer.toString(IList.get("Chicken"));
		ExpensiveCarInvent.setText("                    " + ExpensiveCarInventoryLevel);
		CheapCarInvent.setText("                    " + CheapCarInventoryLevel);
		ChickenInvent.setText("                    " + ChickenInventoryLevel);
		PizzaInvent.setText("                    " + PizzaInventoryLevel);
		SandwichInvent.setText("                    " + SandwichInventoryLevel);

		ExpensiveCarText.setText("");
		CheapCarText.setText("");
		PizzaText.setText("");
		ChickenText.setText("");
		SandwichText.setText("");
		
		marketRecords.ca.setInventoryList(IList);

		MarketLabel.setText("Today's Staff :    " + marketRecords.ca.getName() + "                     " + "Market's Current Cash :   " + marketRecords.ca.getCash());

	}

	public void UpdateInventoryLevelWithoutButton(){
		Map<String,Integer> IList = marketRecords.ca.getInventoryList();
		ExpensiveCarInventoryLevel	= Integer.toString(IList.get("ExpensiveCar"));
		CheapCarInventoryLevel		= Integer.toString(IList.get("CheapCar"));
		PizzaInventoryLevel 		= Integer.toString(IList.get("Pizza"));
		SandwichInventoryLevel		= Integer.toString(IList.get("Sandwich"));
		ChickenInventoryLevel 		= Integer.toString(IList.get("Chicken"));
		ExpensiveCarInvent.setText("                    " + ExpensiveCarInventoryLevel);
		CheapCarInvent.setText("                    " + CheapCarInventoryLevel);
		ChickenInvent.setText("                    " + ChickenInventoryLevel);
		PizzaInvent.setText("                    " + PizzaInventoryLevel);
		SandwichInvent.setText("                    " + SandwichInventoryLevel);

		MarketLabel.setText("Today's Staff :    " + marketRecords.ca.getName() + "                     " + "Market's Current Cash :   " + marketRecords.ca.getCash());
		System.out.println (marketRecords.ca.getCash());

	}

	public boolean isInteger(String str) {
		int size = str.length();

		for (int i = 0; i < size; i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}

		return size > 0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == UpdateButton) {
        	UpdateInventoryLevelWithButton();
        }

	}
}
