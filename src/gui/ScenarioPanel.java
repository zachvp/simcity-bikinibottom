package gui;

import gui.PersonCreationPanel.MyComboBoxItem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import classifieds.Classifieds;
import classifieds.ClassifiedsClass;
import CommonSimpleClasses.CityLocation;
import CommonSimpleClasses.Constants;

public class ScenarioPanel extends JPanel implements ActionListener{ 

	JPanel imagePanel, eastPanel, populatePanel, normativePanel;
	Dimension d, eastDim, populateDim, normativeDim;

	
	//populate panel components
	JComboBox<MyComboBoxItem> workplaceCB;
	JButton populateButton;
	
	//normative panel components
	JRadioButton scenarioA, scenarioB, scenarioC;
	JButton runScenarioButton;
	
	public ScenarioPanel() {
		d = new Dimension(Constants.ANIMATION_PANEL_WIDTH, Constants.ANIMATION_PANEL_HEIGHT-20);
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		setBackground(Color.white);
		setLayout(new BorderLayout());
		
		imagePanel = new JPanel();
		Dimension imageDim = new Dimension((int)(d.width*0.50),(int)(d.height));
		imagePanel.setPreferredSize(imageDim);
		imagePanel.setMaximumSize(imageDim);
		imagePanel.setMinimumSize(imageDim);
		imagePanel.setOpaque(false);
		
		eastPanel = new JPanel();
		eastDim = new Dimension((int)(d.width*0.50),(int)(d.height));
		eastPanel.setPreferredSize(eastDim);
		eastPanel.setMaximumSize(eastDim);
		eastPanel.setMinimumSize(eastDim);
		eastPanel.setOpaque(false);
		
		populatePanel = new JPanel();
		normativePanel = new JPanel();
		makePopulatePanel();
		makeNormativePanel();
		
		eastPanel.add(populatePanel, BorderLayout.NORTH);
		eastPanel.add(normativePanel, BorderLayout.SOUTH);
		
		//add(imagePanel, BorderLayout.WEST);
		//add(eastPanel, BorderLayout.EAST);
		add(eastPanel, BorderLayout.CENTER);
	}

	private void makePopulatePanel() {
		populateDim = new Dimension((int)(eastDim.width),(int)(eastDim.height*0.2));
		populatePanel.setPreferredSize(populateDim);
		populatePanel.setMaximumSize(populateDim);
		populatePanel.setMinimumSize(populateDim);
		populatePanel.setOpaque(false);
		
		Font font = new Font("Serif", Font.BOLD, 16);
		JLabel title = new JLabel("Populate Workplaces");
		title.setFont(font);
		
		//Middle panel
		JPanel middle = new JPanel();
		Dimension middleDim = new Dimension(populateDim.width, (int)(populateDim.height*0.3));
		middle.setPreferredSize(middleDim);
		middle.setLayout(new BorderLayout());
		middle.setOpaque(false);
		
		JLabel workplaceLabel = new JLabel("Workplace: ", JLabel.CENTER);
		
		workplaceCB = new JComboBox<MyComboBoxItem>(); //TODO add array here
		workplaceCB.addActionListener(this);
		Dimension cbDim = new Dimension((int)(populateDim.width*0.7), (int)(populateDim.height*.15));
		workplaceCB.setPreferredSize(cbDim);
		
		middle.add(workplaceLabel, BorderLayout.WEST);
		middle.add(workplaceCB, BorderLayout.EAST);
		
		//Populate Button
		populateButton = new JButton("Populate");
		populateButton.addActionListener(this);
		
		populatePanel.add(title);
		populatePanel.add(middle);
		populatePanel.add(populateButton);
	}

	private void makeNormativePanel() {
		normativeDim = new Dimension((int)(eastDim.width), (int)(eastDim.height*0.65));
		normativePanel.setPreferredSize(normativeDim);
		normativePanel.setMaximumSize(normativeDim);
		normativePanel.setMinimumSize(normativeDim);
		normativePanel.setOpaque(false);
		
		Font font = new Font("Serif", Font.BOLD, 16);
		JLabel title = new JLabel("Normative Scenarios", JLabel.CENTER);
		title.setFont(font);
		Dimension titleDim = new Dimension(normativeDim.width, (int)(normativeDim.height*0.15));
		title.setPreferredSize(titleDim);
		runScenarioButton = new JButton("Run Scenario");
		runScenarioButton.addActionListener(this);
		
		//RadioButtons
		scenarioA = new JRadioButton("Scenario A");
		scenarioB = new JRadioButton("Scenario B");
		scenarioC = new JRadioButton("Scenario C");
		scenarioA.setOpaque(false);
		scenarioB.setOpaque(false);
		scenarioC.setOpaque(false);
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(scenarioA);
		buttonGroup.add(scenarioB);
		buttonGroup.add(scenarioC);
		JPanel rButtons = new JPanel();
		rButtons.setPreferredSize(new Dimension(normativeDim.width, (int)(normativeDim.height*0.7)));
		rButtons.setLayout(new GridLayout(5,1,1,1));
		rButtons.setOpaque(false);
		rButtons.add(scenarioA);
		rButtons.add(scenarioB);
		rButtons.add(scenarioC);

		normativePanel.add(title);
		normativePanel.add(rButtons);
		normativePanel.add(runScenarioButton);
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == populateButton){
			
			
		}
		
		if(e.getSource() == runScenarioButton){
			
		}
		
	}

}