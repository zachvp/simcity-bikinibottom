package housing.gui;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import housing.ResidentialBuilding;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import agent.Role;
import agent.interfaces.Person;

public class HousingInfoPanel extends JPanel {
	List<JLabel> labels = new ArrayList<JLabel>();
	ResidentialBuilding building;

	public HousingInfoPanel(ResidentialBuilding building) {
		GridLayout grid = new GridLayout(1,1);
		this.setLayout(grid);
		building.setName("Residence");
		this.building = building;
		
		for(Map.Entry<Person, Role> entry : building.getPopulation().entrySet()){
			JLabel label = new JLabel(entry.getValue().getName());
			labels.add(label);
			this.add(label);
		}
		
//		JLabel label = new JLabel();
//        this.setLayout(new BorderLayout());
//        label.setText(
//                "<html><h3>" + building.getName() + "</h3><table>"
//                		+ "<tr><td>Owner: </td><td>" + building.getGreeter() + "</td></tr>"
//                		+ "<tr><td> Staff: </td> <td>" + building.getWorker() + "</td>"
//                		+ "<tr>$1.25</td></tr><tr><td>Kelp Rings with Salty Sauce</td><td>$2.00</td></tr><tr><td>Coral Bits</td><td>$1.50</td></tr><tr><td>Kelp Shake</td><td>$2.00</td></tr></table><br></html>");
//
//        this.setBorder(BorderFactory.createRaisedBevelBorder());
//        this.add(label, BorderLayout.CENTER);
//        this.add(new JLabel("         "), BorderLayout.EAST);
//        this.add(new JLabel("         "), BorderLayout.WEST);
		
	}
	
	public void updatePanel(){
		for(Map.Entry<Person, Role> entry : building.getPopulation().entrySet()){
			JLabel label = new JLabel(entry.getValue().getName());
			labels.add(label);
			this.add(label);
		}
	}
}
