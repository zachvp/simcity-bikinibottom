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



public class MarketInfoPanel extends JPanel implements ActionListener{
	
	MarketRecords marketRecords;
	
	private JLabel MarketLabel;
	//Market Label
	private JLabel LamboFinnyInvent = null;
	private JLabel ToyodaInvent = null;
	private JLabel KrabbyPattyInvent = null;
	private JLabel KelpShakeInvent = null;
	private JLabel CoralBitsInvent = null;
	private JLabel KelpRingsInvent = null;

	public JTextField LamboFinnyText = null;
	public JTextField ToyodaText = null;
	public JTextField KrabbyPattyText = null;
	public JTextField KelpShakeText = null;
	public JTextField CoralBitsText = null;
	public JTextField KelpRingsText = null;

	private String LamboFinnyInventoryLevel = "Current Inventory Level";
	private String ToyodaInventoryLevel = "Current Inventory Level";
	private String KrabbyPattyInventoryLevel = "Current Inventory Level";
	private String KelpShakeInventoryLevel = "Current Inventory Level";
	private String CoralBitsInventoryLevel = "Current Inventory Level";
	private String KelpRingsInventoryLevel = "Current Inventory Level";
	
	private JPanel marketLabel = new JPanel();
	private JPanel group = new JPanel();
	private JButton UpdateButton;

	public MarketInfoPanel(MarketRecords rec) {
		
		
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
		JLabel LamboFinnyLabel = new JLabel("               LamboFinny");
		JLabel ToyodaLabel = new JLabel("               Toyoda");
		JLabel KrabbyPattyLabel = new JLabel("               Krabby Patty");
		JLabel KelpShakeLabel = new JLabel("               Kelp Shake");
		JLabel CoralBitsLabel = new JLabel("               Coral Bits");
		JLabel KelpRingsLabel = new JLabel("               Kelp Rings");
		
		LamboFinnyInventoryLevel = "Current Inventory Level";
		ToyodaInventoryLevel = "Current Inventory Level";
		KrabbyPattyInventoryLevel = "Current Inventory Level";
		KelpShakeInventoryLevel = "Current Inventory Level";
		CoralBitsInventoryLevel = "Current Inventory Level";
		KelpRingsInventoryLevel = "Current Inventory Level";


		LamboFinnyInvent = new JLabel();
		LamboFinnyInvent.setText(LamboFinnyInventoryLevel);
		ToyodaInvent = new JLabel();
		ToyodaInvent.setText(ToyodaInventoryLevel);
		KrabbyPattyInvent = new JLabel();
		KrabbyPattyInvent.setText(CoralBitsInventoryLevel);
		KelpShakeInvent = new JLabel();
		KelpShakeInvent.setText(KrabbyPattyInventoryLevel);
		CoralBitsInvent = new JLabel();
		CoralBitsInvent.setText(KelpShakeInventoryLevel);
		KelpRingsInvent = new JLabel();
		KelpRingsInvent.setText(KelpRingsInventoryLevel);



		LamboFinnyText = new JTextField();
		LamboFinnyText.setEditable(true);
		ToyodaText = new JTextField("");
		ToyodaText.setEditable(true);
		KrabbyPattyText = new JTextField("");
		KrabbyPattyText.setEditable(true);
		KelpShakeText = new JTextField("");
		KelpShakeText.setEditable(true);
		CoralBitsText = new JTextField("");
		CoralBitsText.setEditable(true);
		KelpRingsText = new JTextField("");
		KelpRingsText.setEditable(true);

		JPanel LamboFinnyPanel = new JPanel();
		LamboFinnyPanel.setLayout(new GridLayout(1,0,0,0));
		LamboFinnyPanel.add(LamboFinnyLabel);
		LamboFinnyPanel.add(LamboFinnyInvent);
		LamboFinnyPanel.add(LamboFinnyText);

		JPanel ToyodaPanel = new JPanel();
		ToyodaPanel.setLayout(new GridLayout(1,0,0,0));
		ToyodaPanel.add(ToyodaLabel);
		ToyodaPanel.add(ToyodaInvent);
		ToyodaPanel.add(ToyodaText);

		JPanel KrabbyPattyPanel = new JPanel();
		KrabbyPattyPanel.setLayout(new GridLayout(1,0,0,0));
		KrabbyPattyPanel.add(KrabbyPattyLabel);
		KrabbyPattyPanel.add(KrabbyPattyInvent);
		KrabbyPattyPanel.add(KrabbyPattyText);

		JPanel KelpShakePanel = new JPanel();
		KelpShakePanel.setLayout(new GridLayout(1,0,0,0));
		KelpShakePanel.add(KelpShakeLabel);
		KelpShakePanel.add(KelpShakeInvent);
		KelpShakePanel.add(KelpShakeText);

		JPanel CoralBitsPanel = new JPanel();
		CoralBitsPanel.setLayout(new GridLayout(1,0,0,0));
		CoralBitsPanel.add(CoralBitsLabel);
		CoralBitsPanel.add(CoralBitsInvent);
		CoralBitsPanel.add(CoralBitsText);
		
		JPanel KelpRingsPanel = new JPanel();
		KelpRingsPanel.setLayout(new GridLayout(1,0,0,0));
		KelpRingsPanel.add(KelpRingsLabel);
		KelpRingsPanel.add(KelpRingsInvent);
		KelpRingsPanel.add(KelpRingsText);



		//Default Panel
		JPanel DefaultPanel = new JPanel();
		DefaultPanel.setLayout(new GridLayout(1,0,0,0));
		DefaultPanel.add(new JLabel("                     Type"));
		DefaultPanel.add(new JLabel("      Current Inventory Level"));
		DefaultPanel.add(new JLabel("      Desire Inventory Level"));

		panel.add(DefaultPanel);
		panel.add(LamboFinnyPanel);
		panel.add(ToyodaPanel);
		panel.add(KrabbyPattyPanel);
		panel.add(KelpShakePanel);
		panel.add(CoralBitsPanel);
		panel.add(KelpRingsPanel);



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

		//System.out.println("IMHERE");
		if(  isInteger(LamboFinnyText.getText())  )
			IList.put("LamboFinny", Integer.parseInt(LamboFinnyText.getText()));
		if(  isInteger(ToyodaText.getText())  )
			IList.put("Toyoda", Integer.parseInt(ToyodaText.getText()));
		if(  isInteger(KelpShakeText.getText())  )
			IList.put("Kelp Shake", Integer.parseInt(KelpShakeText.getText()));
		if(  isInteger(CoralBitsText.getText())  )
			IList.put("Coral Bits", Integer.parseInt(CoralBitsText.getText()));
		if(  isInteger(KrabbyPattyText.getText())  )
			IList.put("Krabby Patty", Integer.parseInt(KrabbyPattyText.getText()));
		if(  isInteger(KelpRingsText.getText())  )
			IList.put("KelpRings", Integer.parseInt(KelpRingsText.getText()));
		
		

		//Printing out the labels
		LamboFinnyInventoryLevel	= Integer.toString(IList.get("LamboFinny"));
		ToyodaInventoryLevel		= Integer.toString(IList.get("Toyoda"));
		KrabbyPattyInventoryLevel 		= Integer.toString(IList.get("Krabby Patty"));
		KelpShakeInventoryLevel		= Integer.toString(IList.get("Kelp Shake"));
		CoralBitsInventoryLevel 		= Integer.toString(IList.get("Coral Bits"));
		KelpRingsInventoryLevel 	= Integer.toString(IList.get("Kelp Rings"));
		LamboFinnyInvent.setText("                    " + LamboFinnyInventoryLevel);
		ToyodaInvent.setText("                    " + ToyodaInventoryLevel);
		KrabbyPattyInvent.setText("                    " + KrabbyPattyInventoryLevel);
		KelpShakeInvent.setText("                    " + KelpShakeInventoryLevel);
		CoralBitsInvent.setText("                    " + CoralBitsInventoryLevel);
		KelpRingsInvent.setText("                    " + KelpRingsInventoryLevel);
		
		LamboFinnyText.setText("");
		ToyodaText.setText("");
		KelpShakeText.setText("");
		KrabbyPattyText.setText("");
		CoralBitsText.setText("");
		KelpRingsText.setText("");
		
		marketRecords.ca.setInventoryList(IList);

		MarketLabel.setText("Today's Staff :    " + marketRecords.ca.getName() + "                     " + "Market's Current Cash :   " + marketRecords.ca.getCash());

	}

	public void UpdateInventoryLevelWithoutButton(){
		
		//System.out.println("IMHERE");
		Map<String,Integer> IList = marketRecords.ca.getInventoryList();
		LamboFinnyInventoryLevel	= Integer.toString(IList.get("LamboFinny"));
		ToyodaInventoryLevel		= Integer.toString(IList.get("Toyoda"));
		KrabbyPattyInventoryLevel 		= Integer.toString(IList.get("Krabby Patty"));
		KelpShakeInventoryLevel		= Integer.toString(IList.get("Kelp Shake"));
		CoralBitsInventoryLevel 		= Integer.toString(IList.get("Coral Bits"));
		KelpRingsInventoryLevel 	= Integer.toString(IList.get("Kelp Rings"));
		LamboFinnyInvent.setText("                    " + LamboFinnyInventoryLevel);
		ToyodaInvent.setText("                    " + ToyodaInventoryLevel);
		KrabbyPattyInvent.setText("                    " + KrabbyPattyInventoryLevel);
		KelpShakeInvent.setText("                    " + KelpShakeInventoryLevel);
		CoralBitsInvent.setText("                    " + CoralBitsInventoryLevel);
		KelpRingsInvent.setText("                    " + KelpRingsInventoryLevel);
		
		LamboFinnyText.setText("");
		ToyodaText.setText("");
		KelpShakeText.setText("");
		KrabbyPattyText.setText("");
		CoralBitsText.setText("");
		KelpRingsText.setText("");
		
		marketRecords.ca.setInventoryList(IList);

		MarketLabel.setText("Today's Staff :    " + marketRecords.ca.getName() + "                     " + "Market's Current Cash :   " + marketRecords.ca.getCash());


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
