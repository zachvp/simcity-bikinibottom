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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import agent.PersonAgent;
import agent.WorkRole;
import agent.interfaces.Person;
import classifieds.ClassifiedsClass;
import market.gui.MarketBuilding;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.ScheduleTask;

public class StaffDisplay extends JPanel implements ActionListener{
	
	Building building;
	CitizenRecords citizenRecords;

	private Dimension d, panelDim;
	private JLabel staffLabel, unemployedLabel;
	private JPanel staffPanel, unemployedPanel;
	private JScrollPane staffPane, unemployedPane;
	private JPanel staffView, unemployedView;
	private List<UnemployedButton> unemployedList;
	private List<StaffButton> staffList;
	private JButton fireButton, hireButton, replaceButton;
	private JLabel msg;
	private ScheduleTask task = ScheduleTask.getInstance();
	private Map<WorkRole, Person> FutureCareerMap = new HashMap<WorkRole, Person>();
	private Person firePerson = new PersonAgent("FIREPERSON");
	
	public StaffDisplay() {
		d = new Dimension(Constants.INFO_PANEL_WIDTH, Constants.INFO_PANEL_HEIGHT);
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		setLayout(new BorderLayout());

		panelDim = new Dimension((int)(d.width*0.35), d.height);
		
		makeStaffPanel();
		makeUnemployedPanel();
		
		//Fire/Hire Button
		JPanel buttons = new JPanel();
		buttons.setPreferredSize(new Dimension((int)(d.width*0.3), d.height));
		fireButton = new JButton("Fire");
		replaceButton = new JButton("Replace");
		hireButton = new JButton("Hire");
		fireButton.addActionListener(this);
		hireButton.addActionListener(this);
		replaceButton.addActionListener(this);
		msg = new JLabel("");
		buttons.add(fireButton);
		buttons.add(hireButton);
		buttons.add(replaceButton);
		buttons.add(msg);
		
		add(staffPanel, BorderLayout.WEST);
		add(unemployedPanel, BorderLayout.CENTER);
		add(buttons, BorderLayout.EAST);
		
		
		Runnable command = new Runnable(){
			@Override
			public void run() {
				NewWorksTakePlace();
			
			}

			
		};
		
		int hour = 0;
		int minute = 0;
		
		task.scheduleDailyTask(command, hour, minute);
		
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
		unemployedList = new ArrayList<UnemployedButton>();
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
	public void addToStaffList(WorkRole role, Person person) {
		if (person == null) {
			StaffButton button = new StaffButton(null, role);
			button.setAvailable();
			staffList.add(button);
			staffView.add(button);
		}
		else {
			StaffButton button = new StaffButton(person, role);
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
				addToStaffList(r, r.getPerson());
			}else{
				addToStaffList(r, null);
			}
		}
	}
	
	public void updateStaffDisplay(){
		staffView.removeAll();
		staffList.clear();
		addAllWorkRolesToStaffList();
	}
	
	/**
	 * Adds people to unemployed list
	 * @param name
	 */
	public void addToUnemployedList(Person person) {
			UnemployedButton button = new UnemployedButton(person);
			unemployedList.add(button);
			unemployedView.add(button);
		validate();		
	}
	
	public void updateUnemployedList(){
		unemployedList.clear();
		unemployedView.removeAll();
		List<PersonAgent> people = citizenRecords.getCitizenList();
		for (PersonAgent p: people){
			if(p.getWorkRole() == null){
				addToUnemployedList(p);
			}
		}
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		msg.setText("");
		
		//Fire
		if (e.getSource() == fireButton){
			boolean hasStaffSelection = false;
			for (StaffButton staff: staffList){
				if(staff.selected && !staff.available){
					hasStaffSelection = true;
					FutureCareerMap.put(staff.role, firePerson);
					//FirePerson(staff.role);
					//updateStaffDisplay();
					//TODO fire staff.getPersonName()
				}
			}
			if(!hasStaffSelection){
				msg.setText("Please select a person to fire");
				return;
			}
			//TODO check if staff is important role, then need to select person to hire
			/*if(important){
				msg.setText("Please select a replacement for this position");
				return;
			}*/
			return;
		}
		
		//Hire
		if (e.getSource() == hireButton){
			boolean hasUnempSelection = false;
			boolean correctStaffSelection = false;
			//boolean posOpen = false;
			WorkRole openRole = null;
			Person newHire = null;
			for (StaffButton staff: staffList){
				if(staff.selected && staff.available){
					correctStaffSelection = true;
					openRole = staff.getWorkRole();
				}
			}
			if(!correctStaffSelection){
				msg.setText("Please select an available position");
				return;
			}
			for (UnemployedButton b: unemployedList){
				if(b.selected){
					hasUnempSelection = true;
					newHire = b.getPerson();
				}
			}
			if(!hasUnempSelection){
				msg.setText("Please select a person to hire");
				return;
			}
			else if(hasUnempSelection && correctStaffSelection){
				FutureCareerMap.put(openRole, newHire);
				//HirePerson(openRole, newHire);
				//updateUnemployedList();
				//updateStaffDisplay();
			}
			return;
		}
		
		//Replace
		if (e.getSource() == replaceButton){
			boolean hasStaffSelection = false;
			boolean hasUnempSelection = false;
			WorkRole switchRole = null;
			Person newHire = null;
			for (StaffButton staff: staffList){
				if(staff.selected && !staff.available){
					hasStaffSelection = true;
					switchRole = staff.getWorkRole();
				}
			}
			if(!hasStaffSelection){
				msg.setText("Please select a person to fire");
				return;
			}
			for (UnemployedButton b: unemployedList){
				if(b.selected){
					hasUnempSelection = true;
					newHire = b.getPerson();
				}
			}
			if(!hasUnempSelection){
				msg.setText("Please select a person to hire");
				return;
			}
			if(hasStaffSelection && hasUnempSelection){
				FutureCareerMap.put(switchRole,newHire);
				//SwitchRole(switchRole, newHire);
				//updateUnemployedList();
				//updateStaffDisplay();
				//TODO fire staff.getPersonName()
				//TODO hire b.getName();
			}
			
			return;
		}
	}
	
	private void NewWorksTakePlace() {
		
		for (Map.Entry<WorkRole, Person> entry : FutureCareerMap.entrySet()) {
		    WorkRole currentRole = entry.getKey();
		    Person currentPerson = entry.getValue();
			//Firing
			if (currentPerson == firePerson){
				FirePerson(currentRole);
			}
			//Hiring or Switch
			else {
				//Hiring
				if (currentRole.getPerson() == null){
					HirePerson(currentRole,currentPerson);
				}
				else{
					SwitchRole(currentRole, currentPerson);
				}
			}
		}
		updateUnemployedList();
		updateStaffDisplay();
		
	}
	
	public void setBuilding(Building b){
		building = b;
	}
	public void setCitizenRecords(CitizenRecords rec){
		citizenRecords = rec;
	}
	
	public void FirePerson(WorkRole r){
		//GUI to move the person offscreen (Exit)
		
		Person currentFiringPerson = r.getPerson();
		currentFiringPerson.removeRole(r);
		r.setPerson(null);
	}
	
	public void HirePerson(WorkRole r, Person prospectivePerson){
		
		r.setPerson(prospectivePerson);
		prospectivePerson.addRole(r);
		
	}
	
	public void SwitchRole(WorkRole r, Person prospectivePerson){
		
		Person currentFiringPerson = r.getPerson();
		
		//Remove the role in the firing person's list of role
		currentFiringPerson.removeRole(r);
		
		//Add the role in the hiring person's list of role
		prospectivePerson.addRole(r);
		
		//set the role's person to be the prospectivePerson (new hiring person)
		r.setPerson(prospectivePerson);
	}
	
	class StaffButton extends JPanel implements MouseListener{
		JLabel personName;
		private JLabel roleName;
		private Person person;
		private WorkRole role;
		boolean selected = false;
		boolean available = false;
		
		
		StaffButton(Person per, WorkRole rol){
			person = per;
			role = rol;
			
			Dimension buttonSize = new Dimension(panelDim.width - 19, (int) (panelDim.height / 7));
			setPreferredSize(buttonSize);
			setMaximumSize(buttonSize);
			setMinimumSize(buttonSize);
			setBackground(Color.white);
			setBorder(BorderFactory.createLineBorder(Color.BLACK));
			setLayout(new BorderLayout());
			addMouseListener(this);
			personName = new JLabel("", JLabel.CENTER);
			roleName = new JLabel(role.getShortName(), JLabel.CENTER);
			if(person != null){
				personName.setText(person.getName());
			}
			
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
		
		protected WorkRole getWorkRole(){
			return role;
		}
		
		protected Person getPerson(){
			return person;
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
		public void mouseEntered(MouseEvent arg0) {}
		@Override
		public void mouseExited(MouseEvent arg0) {}
		@Override
		public void mousePressed(MouseEvent arg0) {}
		@Override
		public void mouseReleased(MouseEvent arg0) {}
	}
	
	class UnemployedButton extends JPanel implements MouseListener{
		JLabel personName;
		Person person;
		boolean selected = false;
		
		UnemployedButton(Person per){
			person = per;
			
			Dimension buttonSize = new Dimension(panelDim.width - 19, (int) (panelDim.height / 7));
			setPreferredSize(buttonSize);
			setMaximumSize(buttonSize);
			setMinimumSize(buttonSize);
			setBackground(Color.white);
			setBorder(BorderFactory.createLineBorder(Color.BLACK));
			setLayout(new BorderLayout());
			addMouseListener(this);
			
			personName = new JLabel(person.getName(), JLabel.CENTER);
			personName.setPreferredSize(new Dimension((int)(buttonSize.width*0.45), (int)(buttonSize.height)));
			
			add(personName, BorderLayout.CENTER);
		}
		
		protected Person getPerson(){
			return person;
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			if(!selected){
				//deselect all		
				for (UnemployedButton b: unemployedList){
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
		public void mouseEntered(MouseEvent arg0) {}
		@Override
		public void mouseExited(MouseEvent arg0) {}
		@Override
		public void mousePressed(MouseEvent arg0) {}
		@Override
		public void mouseReleased(MouseEvent arg0) {}
		
	}
	
	
}
