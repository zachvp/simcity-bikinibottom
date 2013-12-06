package restaurant.strottma.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import CommonSimpleClasses.Constants;

public class InfoPanel extends JPanel{

	public InfoPanel() {
		Dimension d = new Dimension(Constants.INFO_PANEL_WIDTH, Constants.INFO_PANEL_HEIGHT);
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		
		Dimension half = new Dimension((int)(d.width*0.5), d.height);
		JPanel staff = new JPanel();
		staff.setPreferredSize(half);
		JPanel menu = new JPanel();
		menu.setPreferredSize(half);

		//TODO Update with Restaurant Staff
		JLabel staffLabel = new JLabel("<html><h3><u>Tonight's Staff</u></h3><table>"
				+ "<tr><td>Host:</td><td>" + "" + "</td></tr>"
				+ "</table></html>");
		JLabel menuLabel = new JLabel("<html><h3><u> Menu</u></h3><table>"
				+ "<tr><td>Steak</td><td>$15.99</td></tr>"
				+ "<tr><td>Chicken</td><td>$10.99</td></tr>"
				+ "<tr><td>Salad</td><td>$5.99</td></tr>"
				+ "<tr><td>Pizza</td><td>$8.99</td></tr>"
				+ "</table><br></html>");
		setLayout(new BorderLayout());

		staff.add(staffLabel);
		menu.add(menuLabel);
		
		add(staff, BorderLayout.WEST);
		add(menu, BorderLayout.EAST);
	}


}
