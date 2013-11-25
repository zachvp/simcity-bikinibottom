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
import javax.swing.border.LineBorder;


/**
 * A panel that contains the list of buttons that bring up the internal view of a building
 * @author Victoria Dea
 *
 */
public class InfoList extends JPanel implements ActionListener {

	private JScrollPane pane;
	private JPanel view;
	private List<JButton> list;
	private InfoPanel infoPanel;
	private Dimension d;
	private BuildingView buildingView;
	private boolean isBuildingList = false;
	CitizenRecords citizenRecords;
	private ArrayList<Building>buildingList;
	InfoList otherTab;
	

	public InfoList(int w, int h){
		view = new JPanel();
		list = new ArrayList<JButton>();
		d = new Dimension(w-40, h-40);
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
		if (name != null) {
			//System.out.println("adding "+name+" to list");
			JButton button = new JButton(name);
			button.setBackground(Color.white);
			Dimension buttonSize = new Dimension(d.width - 19, (int) (d.height / 5));
			button.setPreferredSize(buttonSize);
			button.setMinimumSize(buttonSize);
			button.setMaximumSize(buttonSize);
			button.addActionListener(this);
			list.add(button);
			view.add(button);
		}
		validate();		
	}
	
	public void actionPerformed(ActionEvent e) {
		if (isBuildingList){
			for (JButton b: list){
				if(e.getSource() == b){					
					//System.out.println("button press " + b.getText());					
					for(Building a: buildingList){
						if(a.getName() == b.getText())
							infoPanel.updateBuildingInfoPanel(a);	
					}
					buildingView.showCard(b.getText());									
				}
			}
		}
		else{//it is a PersonAgent
			for (JButton b: list){
				if(e.getSource() == b)
					citizenRecords.showInfo(b.getText());
			}
		}
		/*for (JButton b: list){
			if(e.getSource() == b){
				b.setBackground(Color.LIGHT_GRAY);
			}
			else{
				b.setBackground(Color.white);
			}
		}
		otherTab.deselectOtherTab();*/
	}
	
	public void deselectOtherTab(){
		for (JButton b: list){
			b.setBackground(Color.white);
		}
	}
	
	/** Utilities **/
	
	/**
	 * Sets reference to BuildingView Panel
	 * @param v BuildingView Panel
	 */
	public void setBuildingView(BuildingView v){
		buildingView = v;
		isBuildingList = true;
	}
	/**
	 * Sets reference to Info Panel
	 * @param p InfoPanel
	 */
	public void setInfoPanel(InfoPanel p){
		infoPanel = p;
	}

	/**
	 * Returns the list of information buttons
	 * @return list of buttons
	 */
	public List<JButton> getBuildingButtonList(){
		return list;
	}
	
	/**
	 * Sets Reference to the CitizenRecords
	 * @param r CitizenRecords
	 */
	public void setCitizenRecords(CitizenRecords r) {
		citizenRecords = r;
	}

	/**
	 * Sets Reference to the list of Buildings in the City
	 * @param buildings An ArrayList of Buildings
	 */
	public void setBuildingList(ArrayList<Building> buildings) {
		buildingList = buildings;
	}
	public void setOtherTab(InfoList t){
		otherTab = t;
	}

	
}
