package restaurant.vdea.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

    public JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private JPanel entryPanel = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    
    private JButton addPersonB = new JButton("Add");
    private JCheckBox hungryCB = new JCheckBox("Hungry");
    private JCheckBox onBreakCB = new JCheckBox("Go on Break");
    private JTextField nameTF = new JTextField("Enter Name Here");
    
	private static final int TFWidth = 205;
	private static final int TFHeight = 25;
	
    private RestaurantPanel restPanel;
    private String type;
    
    
    
    

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String type) {
    	int entryWidth = 220;
    	int entryHeight = 100;
    	
    	Dimension entryDim = new Dimension(entryWidth, entryHeight);
    	entryPanel.setPreferredSize(entryDim);
    	entryPanel.setMinimumSize(entryDim);
    	entryPanel.setMaximumSize(entryDim);
        
    	add(entryPanel);
    	
        restPanel = rp;
        this.type = type;

        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        
        entryPanel.add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));

        
        //add customer name input text field
        Dimension TFSize = new Dimension(TFWidth, TFHeight);
        nameTF.setPreferredSize(TFSize);
        nameTF.setMinimumSize(TFSize);
        nameTF.setMaximumSize(TFSize);
        //nameTF.addActionListener(this);
        nameTF.addMouseListener(new MouseAdapter() {
        	public void mousePressed(MouseEvent e) {
        		nameTF.setText(null);
        		if (getType() == "Customers"){
        			hungryCB.setEnabled(true);
        		}
        		if (getType() == "Waiters"){
        			onBreakCB.setEnabled(true);
        		}
        	}
        });

        entryPanel.add(nameTF); 
        
        //Adds checkboxes to panel
        if (getType() == "Customers"){
        	hungryCB.addActionListener(this);
        	hungryCB.setEnabled(false);
        	entryPanel.add(hungryCB);
        }

        addPersonB.addActionListener(this);
        entryPanel.add(addPersonB);

        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane);
    }
    
    public String getType(){
    	return type;
    }
    
    /**Handles textfield mouse clicked events
    public void mouseClicked(MouseEvent e){
    	nameTF.setText("");
    	hungryCB.setEnabled(true);
    }*/
    
    

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
    	
    	
    	if (e.getSource() == addPersonB) {
    		// Chapter 2.19 describes showInputDialog()
    		//addPerson(JOptionPane.showInputDialog("Please enter a name:"));

    		addPerson(nameTF.getText()); //get Text from text field
    		if (getType() == "Customers"){
    			if(hungryCB.isSelected()){
    				restPanel.setHunger(true);
    				hungryCB.setEnabled(false);
    			}
    		}
       	}
    	else {
    		for (JButton temp:list){
    			if (e.getSource() == temp)
    				restPanel.showInfo(type, temp.getText());
    		}
    	}


        
    }

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    public void addPerson(String name) {
        if (name != null) {
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 19,
                    (int) (paneSize.height / 7));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
            //System.out.println("button added " + buttonSize);
            nameTF.setText("");
            restPanel.addPerson(type, name);//puts customer on list
            restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }
}
