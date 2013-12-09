package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import agent.WorkRole;
import classifieds.ClassifiedsClass;
import market.gui.MarketBuilding;
import CommonSimpleClasses.Constants;

public class StaffDisplay extends JPanel implements ActionListener{
	
	Building building;

	private Dimension d, panelDim;
	private JLabel staffLabel, unemployedLabel;
	private JPanel staffPanel, unemployedPanel;
	private JScrollPane staffPane, unemployedPane;
	private JPanel staffView, unemployedView;
	private List<JButton> unemployedList;
	private List<StaffButton> staffList;
	private JButton fireButton, hireButton;
	private JLabel msg;
	
	public StaffDisplay() {
		d = new Dimension(Constants.INFO_PANEL_WIDTH, Constants.INFO_PANEL_HEIGHT);
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);

		panelDim = new Dimension((int)(d.width*0.35), d.height);
		
		makeStaffPanel();
		makeUnemployedPanel();
		
		//Fire/Hire Button
		JPanel buttons = new JPanel();
		buttons.setPreferredSize(new Dimension((int)(d.width*0.3), d.height));
		fireButton = new JButton("Fire");
		hireButton = new JButton("Hire");
		fireButton.addActionListener(this);
		hireButton.addActionListener(this);
		msg = new JLabel("");
		buttons.add(fireButton);
		buttons.add(hireButton);
		buttons.add(msg);
		
		add(staffPanel);
		add(unemployedPanel);
		add(buttons);
		
		//test
		/*addToStaffList("jsaljdsk", "anjdg");
		addToStaffList("ajsdkgads", "afdjl");
		staffList.get(0).setAvailable();
		
		addToUnemployedList("agsd");
		addToUnemployedList("gajsldgjlskdmv,");*/
	}

	private void makeStaffPanel() {
		staffPanel = new JPanel();
		staffPanel.setPreferredSize(panelDim);
		
		staffLabel = new JLabel("Current Positions");
		//staffLabel.setPreferredSize(new Dimension(panelDim.width, (int)(panelDim.height*0.20)));
		
		staffView = new JPanel();
		staffView.setLayout(new BoxLayout((Container) staffView, BoxLayout.Y_AXIS));
		staffList = new ArrayList<StaffButton>();
		staffPane = new JScrollPane(staffView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		staffPane.setPreferredSize(new Dimension(panelDim.width, (int)(panelDim.height*0.85)));
		
		staffPanel.add(staffLabel);
		staffPanel.add(staffPane);
	}

	private void makeUnemployedPanel() {
		unemployedPanel = new JPanel();
		unemployedPanel.setPreferredSize(panelDim);
		
		unemployedLabel = new JLabel("Unemployed People");
		//unemployedLabel.setPreferredSize(new Dimension(panelDim.width, (int)(panelDim.height*0.1)));
				
		unemployedView = new JPanel();
		unemployedView.setLayout(new BoxLayout((Container) unemployedView, BoxLayout.Y_AXIS));
		unemployedList = new ArrayList<JButton>();
		unemployedPane = new JScrollPane(unemployedView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		unemployedPane.setPreferredSize(new Dimension(panelDim.width, (int)(panelDim.height*0.85)));
		
		unemployedPanel.add(unemployedLabel);
		unemployedPanel.add(unemployedPane);
	}

	
	/**
	 * Adds a new button to the List of staff
	 * @param role 
	 * @param name Name of person
	 */
	public void addToStaffList(String role, String name) {
		System.out.println(role + " - "+ name);
		if (name == null ||name.equals("Nobody")) {
			StaffButton button = new StaffButton("", role);
			button.setAvailable();
			staffList.add(button);
			staffView.add(button);
		}
		else {
			StaffButton button = new StaffButton(name, role);
			staffList.add(button);
			staffView.add(button);
		}
		validate();		
	}
	
	/**
	 *  Automatically add all the workroles to the staff list
	 */
	public void addAllWorkRolesToStaffList(){
		List<WorkRole> currentBuildingWorkRoles = ClassifiedsClass.getClassifiedsInstance().getJobsForBuilding(building, false);
		for (WorkRole r: currentBuildingWorkRoles){
			if(r.getPerson()!=null){
				addToStaffList(r.getShortName(), r.getPerson().getName());
			}else{
				addToStaffList(r.getShortName(), null);
			}
		}
	}
	
	/**
	 * Adds people to unemployed list
	 * @param name
	 */
	public void addToUnemployedList(String name) {
		if (name != null) {
			JButton button = new JButton(name);
			button.setBackground(Color.white);
			Dimension buttonSize = new Dimension(panelDim.width - 19, (int) (panelDim.height / 7));
			button.setPreferredSize(buttonSize);
			button.setMinimumSize(buttonSize);
			button.setMaximumSize(buttonSize);
			button.setBorder(BorderFactory.createLineBorder(Color.black));
			button.addActionListener(this);
			unemployedList.add(button);
			unemployedView.add(button);
		}
		validate();		
	}
	
	public void updateStaffDisplay(){
		staffView.removeAll();
		staffList.clear();
		addAllWorkRolesToStaffList();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		msg.setText("");
		
		//Fire
		if (e.getSource() == fireButton){
			boolean hasStaffSelection = false;
			boolean hasUnempSelection = false;
			for (StaffButton staff: staffList){
				if(staff.selected && !staff.available){
					hasStaffSelection = true;
					//TODO fire staff.getPersonName()
				}
			}
			if(!hasStaffSelection){
				msg.setText("Please select a person to fire");
				return;
			}
			for (JButton b: unemployedList){
				if(b.getBackground() == Color.LIGHT_GRAY){
					hasUnempSelection = true;
					//TODO hire b.getName();
				}
			}
			//TODO check if staff is important role, then need to select person to hire
			/*if(!hasUnempSelection){
				msg.setText("Please select a person to hire");
				return;
			}*/
			
			if(hasStaffSelection && hasUnempSelection){
				//TODO msg.setText()
			}
			
			return;
		}
		
		//Hire
		if (e.getSource() == hireButton){
			
			return;
		}
		
		for (JButton b: unemployedList){
			if(e.getSource() == b){
				if(b.getBackground() == Color.white){
					b.setBackground(Color.LIGHT_GRAY);
				}
				else if(b.getBackground() == Color.LIGHT_GRAY){
					b.setBackground(Color.white);
				}
			}
			else{
				b.setBackground(Color.white);
			}
		}
		
	}
	
	public void setBuilding(Building b){
		building = b;
	}
	
	class StaffButton extends JPanel implements MouseListener{
		JLabel personName;
		private JLabel roleName;
		boolean selected = false;
		boolean available = false;
		
		StaffButton(String name, String role){
			Dimension buttonSize = new Dimension(panelDim.width - 19, (int) (panelDim.height / 7));
			setPreferredSize(buttonSize);
			setMaximumSize(buttonSize);
			setMinimumSize(buttonSize);
			setBackground(Color.white);
			setBorder(BorderFactory.createLineBorder(Color.BLACK));
			setLayout(new BorderLayout());
			addMouseListener(this);
			personName = new JLabel(name, JLabel.CENTER);
			roleName = new JLabel(role, JLabel.CENTER);
			
			Dimension labelDim = new Dimension((int)(buttonSize.width*0.45), (int)(buttonSize.height));
			personName.setPreferredSize(labelDim);
			roleName.setPreferredSize(labelDim);
			JLabel dash = new JLabel("-", JLabel.CENTER);
			dash.setPreferredSize(new Dimension((int)(buttonSize.width*0.1), (int)(buttonSize.height)));
			
			add(roleName, BorderLayout.WEST);
			add(dash, BorderLayout.CENTER);
			add(personName, BorderLayout.EAST);
		}
		
		protected void setAvailable(){
			available = true;
			personName.setText("AVAILABLE");
			personName.setForeground(Color.GREEN);			
		}
		
		protected void setEmployee(String name){
			available = false;
			personName.setText(name);
			personName.setForeground(Color.black);
		}
		
		protected String getWorkRole(){
			return roleName.getText();
		}
		
		protected String getPersonName(){
			return personName.getText();
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			if(!selected){
				//deselect all		
				for (StaffButton b: staffList){
					if(b.selected){
						b.selected = false;
						b.setBackground(Color.white);
					}
				}
				setBackground(Color.LIGHT_GRAY);
				selected = true;
			}else{
				selected = false;
				setBackground(Color.white);
			}
			
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			
		}
		
	}
}
