package gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


/**
 * A panel that contains the list of buttons that bring up the internal view of a building
 * @author Victoria Dea
 *
 */
public class InfoList extends JPanel implements ActionListener {

	private JScrollPane pane;
	private JPanel view = new JPanel();
	private List<JButton> list = new ArrayList<JButton>();

	public InfoList(int w, int h){
		Dimension d = new Dimension(w-40, h-40);
		view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
		pane = new JScrollPane(view, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pane.setPreferredSize(d);
		add(pane);
	}

	/**
	 * Adds a new information button to the list.
	 * @param name Name on the button
	 */
	public void addToList(String name) {
		//TODO modify to add more building details to button? (picture, etc.)
		if (name != null) {
			JButton button = new JButton(name);
			button.setBackground(Color.white);

			Dimension paneSize = pane.getSize();
			Dimension buttonSize = new Dimension(paneSize.width - 19, (int) (paneSize.height / 5));
			button.setPreferredSize(buttonSize);
			button.setMinimumSize(buttonSize);
			button.setMaximumSize(buttonSize);
			button.addActionListener(this);
			list.add(button);
			view.add(button);
			validate();
		}
	}

	/**
	 * Returns the list of information buttons
	 * @return list of buttons
	 */
	public List<JButton> getBuildingButtonList(){
		return list;
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
