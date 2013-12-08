package restaurant.vegaperk.gui;

import restaurant.vegaperk.CustomerAgent;
import restaurant.vegaperk.WaiterAgent;
import gui.AnimationPanel;

import javax.swing.*;

import java.awt.*;

/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */

@SuppressWarnings("serial")
public class RestaurantGui extends JFrame{// implements ActionListener {
    /* The GUI has one frame with two components: the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds...
     * the staff listing, menu, and lists of current customers all constructed
     * in RestaurantPanel()
     */
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    
    private JPanel imagePanel;
    private JLabel imageLabel;// part of imagePanel
    
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui() {    	
        int WINDOWX = 1100;
        int WINDOWY = 500;

        setBounds(50, 50, WINDOWX, WINDOWY);

        setLayout(new BoxLayout((Container) getContentPane(), 
        		BoxLayout.X_AXIS));
        JPanel restInterface = new JPanel();
        restInterface.setPreferredSize(new Dimension(WINDOWX/2, WINDOWY));
        restInterface.setAlignmentX(Component.LEFT_ALIGNMENT);
        restInterface.setAlignmentY(Component.TOP_ALIGNMENT);
        restInterface.setLayout(new BoxLayout(restInterface, BoxLayout.Y_AXIS));
        
        //set up the image  jpanel
        Dimension imageDim = new Dimension(WINDOWX/2, (int) (WINDOWY* .4));
        imagePanel = new JPanel();
        imagePanel.setPreferredSize(imageDim);
        imagePanel.setMinimumSize(imageDim);
        imagePanel.setMaximumSize(imageDim);
        imagePanel.setBorder(BorderFactory.createTitledBorder("Welcome!"));
        imagePanel.setLayout(new GridLayout(1,2,30,0));
        
        //add an image
        String icon = "resource/krusty-krab.jpg";
        
        imageLabel = new JLabel(new ImageIcon(icon));
        imagePanel.add(imageLabel);
        
        restInterface.add(imagePanel);

        Dimension restDim = new Dimension(WINDOWX/2, (int) (WINDOWY * .45));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        restInterface.add(restPanel);
        
        JPanel animContainer = new JPanel();
        animContainer.setPreferredSize(new Dimension(WINDOWX,WINDOWY));
        animContainer.setAlignmentX(Component.RIGHT_ALIGNMENT);
        animContainer.setLayout(new BoxLayout(animContainer, BoxLayout.Y_AXIS));
        animContainer.add(animationPanel);
        add(restInterface);
        add(animContainer);
        setLocationRelativeTo(null);
    }
    
    public void pauseAnimationPanel(){
    	animationPanel.stopTimer();
    }
    
    public void resumeAnimationPanel(){
    	animationPanel.resumeTimer();
    }
    
    public void setCustomerEnabled(CustomerAgent c) {
    	restPanel.setCustomerEnabled(c);
    }
    
    public void setWaiterEnabled(WaiterAgent w){
    	restPanel.denyBreak(w);
    }
    
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("Krusty Krab");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
