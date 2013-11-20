package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import agent.PersonAgent;

public class InfoPanel extends JPanel implements ActionListener{
	
	private Dimension d;
	private JLabel info;
	private JPanel textPanel;
	private JPanel controlPanel;

	public InfoPanel(int w, int h){
		d = new Dimension(w-10, h-25);
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		setLayout(new BorderLayout());
		
		textPanel = new JPanel();
		Dimension textDim = new Dimension(w/5, h-25);
		textPanel.setPreferredSize(textDim);
		textPanel.setMaximumSize(textDim);
		textPanel.setMinimumSize(textDim);
		
		info = new JLabel();
		//TODO test text
		info.setText("<html><div>&nbsp;</div><div> "
				+ "Name: "+ "Spongebob Squarepants" +"</div><div>&nbsp;</div>"
				+ "<div> Job: "+"Chef" +"</div><div>&nbsp;</div>"
				+ "<div> Residence: "+ "Pineapple" + "</div><div>&nbsp;</div>"
				+ "<div> Money: $"+ "500" +"</div><div>&nbsp;</div>"
				+ "<div> Hunger Level: "+"2" +"</div></html>"
		);
		textPanel.add(info);
		add(textPanel, BorderLayout.WEST);
	}

	public void updateInfoPanel(PersonAgent p){
		//or get from Kelp? if E PersonAgent per c per==p
		
		PersonAgent person = p;
		info.setText("<html><div>&nbsp;</div><div> "
						+ "Name: "+ person.getName() +"</div><div>&nbsp;</div>"
						//+ "<div> Job: "+ person.getJob() +"</div><div>&nbsp;</div>"
						//+ "<div> Residence: "+ person.getResidence + "</div><div>&nbsp;</div>"
						//+ "<div> Money: $"+ person.getMoney() +"</div><div>&nbsp;</div>"
						//+ "<div> Hunger Level: "+ person.getHungerLevel +"</div></html>"
				);



		validate();
	}
	
	public void updateInfoPanel(Building b){
		Building building = b;
		
	}
	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
