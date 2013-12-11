package restaurant.vegaperk.gui;

import restaurant.vegaperk.backend.CustomerRole;
import restaurant.vegaperk.backend.RestaurantVegaPerkBuilding;
import restaurant.vegaperk.backend.WaiterRole;
import restaurant.vegaperk.backend.WaiterRoleBase;
import gui.AnimationPanel;
import gui.Building;

import javax.swing.*;

import agent.Role;
import CommonSimpleClasses.Constants;

import java.awt.*;

/**
 * Restaurant GUI class.
 * Contains the main frame and subsequent panels
 */

@SuppressWarnings("serial")
public class RestaurantGui extends JPanel{
	
    /* The GUI has one frame with two components: the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	private AnimationPanel animationPanel = new AnimationPanel();
	
    /* restPanel holds...
     * the staff listing, menu, and lists of current customers all constructed
     * in RestaurantPanel()
     */
    private RestaurantPanel restPanel;
    
    private JPanel imagePanel;
    private JLabel imageLabel;// part of imagePanel
    
    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public RestaurantGui(RestaurantVegaPerkBuilding building) {
    	this.restPanel = new RestaurantPanel(this, building);
    	
        int WINDOWX = Constants.ANIMATION_PANEL_WIDTH * 2;
        int WINDOWY = Constants.ANIMATION_PANEL_HEIGHT;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        JPanel restInterface = new JPanel();
        restInterface.setPreferredSize(new Dimension(WINDOWX/2, WINDOWY));
        restInterface.setAlignmentX(Component.LEFT_ALIGNMENT);
        restInterface.setAlignmentY(Component.TOP_ALIGNMENT);
        restInterface.setLayout(new BoxLayout(restInterface, BoxLayout.Y_AXIS));
        
        //set up the image panel
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
    }
    
    public void pauseAnimationPanel(){
    	animationPanel.stopTimer();
    }
    
    public void resumeAnimationPanel(){
    	animationPanel.resumeTimer();
    }
    
    public void setCustomerEnabled(CustomerRole c) {
    	restPanel.setCustomerEnabled(c);
    }
    
    public void setWaiterEnabled(WaiterRoleBase agent){
    	restPanel.denyBreak(agent);
    }
    
	public AnimationPanel getAnimationPanel() {
		return animationPanel;
	}
	
	public RestaurantPanel getInfoPanel() {
		return restPanel;
	}
	
	public Role getHost() {
		return restPanel.getHost();
	}

	public void setAnimationPanel(AnimationPanel animationPanel) {
		this.animationPanel = animationPanel;
	}

	/**
	 * @return the restPanel
	 */
	public RestaurantPanel getRestPanel() {
		return restPanel;
	}
}
