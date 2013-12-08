package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class TabbedInfoDisplay extends JTabbedPane implements ActionListener{

	JPanel staffCard;
	public TabbedInfoDisplay() {
		
	}
	
	public void showStaffTab(){
		addTab("Staff", staffCard);
	}
	
	public void hideBuildingTabs(){
		remove(2);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		
	}

	public void setStaffCard(JPanel panel) {
		staffCard = panel;
	}


}
