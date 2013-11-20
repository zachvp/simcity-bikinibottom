package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Panel for creating PersonAgents from UI
 * Treated as a "Hospital" Building
 * @author Victoria Dea
 *
 */
public class PersonCreationPanel extends JPanel implements ActionListener{
	
	JTextField nameText;
	JComboBox<String> occupations;
	JComboBox<String> residences;
	JComboBox<String> wealth;
	JComboBox<String> car;
	JButton createButton;

	public PersonCreationPanel(Dimension d){
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		setBackground(Color.white);
		//setLayout(new BorderLayout());
		
		JLabel welcomeText = new JLabel("Welcome to the Hospital!");
		Dimension textDim = new Dimension(d.width, (int)(d.height*0.2));
		welcomeText.setPreferredSize(textDim);
		welcomeText.setMaximumSize(textDim);
		welcomeText.setMinimumSize(textDim);
		Font font = new Font("Serif", Font.BOLD, 20);
		
		welcomeText.setFont(font);
		
		JPanel inputPanel = new JPanel();
		Dimension inputDim = new Dimension(d.width, (int)(d.height*0.3));
		inputPanel.setPreferredSize(inputDim);
		inputPanel.setMaximumSize(inputDim);
		inputPanel.setMinimumSize(inputDim);
		inputPanel.setLayout(new GridLayout(5,2,5,5));
		inputPanel.setBackground(Color.white);
		
		nameText = new JTextField("Enter a name");
		occupations = new JComboBox<String>(new String[] {"Select an Occupation"});
		residences = new JComboBox<String>(new String[] {"Select a Residence"});
		wealth = new JComboBox<String>(new String[] {"Select a Status", "Rich", "Middle", "Poor"});
		car = new JComboBox<String>(new String[] {"Has a Car", "Yes", "No"});

		nameText.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				nameText.setText(null);
			}
		});

		inputPanel.add(new JLabel("Name: "));
		inputPanel.add(nameText);
		inputPanel.add(new JLabel("Occupation: "));
		inputPanel.add(occupations);
		inputPanel.add(new JLabel("Residence: "));
		inputPanel.add(residences);
		inputPanel.add(new JLabel("Status: "));
		inputPanel.add(wealth);
		inputPanel.add(new JLabel("Car: "));
		inputPanel.add(car);
		
		createButton = new JButton("Create");
		createButton.addActionListener(this);
		
		add(welcomeText);
		add(inputPanel);
		add(createButton);
		
	}

	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == createButton){
			String name = nameText.getText();
			String job = (String)occupations.getSelectedItem();
			String home = (String)residences.getSelectedItem();
			String wealthStatus = (String)wealth.getSelectedItem();
			boolean hasCar = ((String)car.getSelectedItem()).equals("Yes");
			
			nameText.setText("");
			occupations.setSelectedIndex(0);
			residences.setSelectedIndex(0);
			wealth.setSelectedIndex(0);
			car.setSelectedIndex(0);
			
			//TODO Create personAgent
			//TODO Add to person InfoList
		}
		
	}
	
	
}
