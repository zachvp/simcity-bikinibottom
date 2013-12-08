package restaurant.vonbeck.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import agent.Agent;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
@SuppressWarnings("serial")
public class ListPanel extends JPanel implements ActionListener {

	private static final int CUSTOMER_BUTTON_HEIGHT = 16;
	private JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addPersonB = new JButton("Add");
    private JButton pauseB = new JButton("Pause");
    private JTextField nameField = new JTextField();
    private JCheckBox hungryCheck = new JCheckBox();

    private RestaurantPanel restPanel;
    private String type;

    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        //setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 120;
        
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"),c);
        c.gridy++;
        
        nameField.getDocument().addDocumentListener(new DocumentListener() {
        	public void changedUpdate(DocumentEvent e) {
        		check();
        	}
        	public void removeUpdate(DocumentEvent e) {
        		check();
        	}
        	public void insertUpdate(DocumentEvent e) {
        		check();
        	}
        	public void check(){
        		if (nameField.getText().length() == 0) {
        			hungryCheck.setEnabled(false);
        		} else {
        			hungryCheck.setEnabled(true);
        		}
        	}
        });
        add (nameField,c);
        
        c.gridy++;
        hungryCheck.setText("Hungry?");
        hungryCheck.setEnabled(false);
        add(hungryCheck,c);
        
        c.gridwidth = 1;
        
        c.gridx--;
        c.gridy++;
        
        addPersonB.addActionListener(this);
        add(addPersonB,c);
        c.gridy++;
        
        pauseB.addActionListener(this);
        add(pauseB,c);
        c.gridy++;
        
        c.ipady = 66;
        view.setLayout(new BoxLayout((Container) view, BoxLayout.Y_AXIS));
        pane.setViewportView(view);
        add(pane,c);
        
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
    	
        if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
            addPerson(nameField.getText(),hungryCheck.isSelected());
            
        } else {
        	// Isn't the second for loop more beautiful?
            /*for (int i = 0; i < list.size(); i++) {
                JButton temp = list.get(i);*/
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
    public void addPerson(String name, boolean hungry) {
        if (name != null) {
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - 20,
                    CUSTOMER_BUTTON_HEIGHT);
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
            restPanel.addPerson(type, name, hungry);//puts customer on list
            restPanel.showInfo(type, name);//puts hungry button on panel
            validate();
        }
    }
}
