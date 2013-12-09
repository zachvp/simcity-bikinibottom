package restaurant.vdea.gui;


//import restaurant.WaiterAgent;
import restaurant.CookAgent;

import java.awt.*;

public class CookGui implements Gui {

    private CookAgent agent = null;

    private int xPos = 515, yPos = 50;//default waiter position
    private int xDestination =365, yDestination = 50;//default start position
    
    private final int WINDOWX = 600;
    private final int WINDOWY = 490;
    
    private final int buffer = 15;
	private final int grillW = 82;
	private final int grillH = 25;
	private final int grillX = WINDOWX-grillW-buffer;
	private final int grillY = buffer+15;
	
	private final int platingW = 25;
	private final int platingH = 80;
	private final int platingX = grillX-platingW;
	private final int platingY = grillY+grillH;
	private final int plateY = platingY+1;
	private final int plateX = platingX+2;
	static final int grillNum = 4;
	//private List<Grill> grills = new ArrayList<Grill>();    
    private boolean[] cooking = new boolean[grillNum];
    private String[] grills = new String[grillNum];
    private boolean[] plating = new boolean[grillNum];
    private String[] plates = new String[grillNum];

    public CookGui(CookAgent agent) {
        this.agent = agent;
        
        for (int i = 0; i<grillNum; i++){
        	cooking[i] = false;
        	grills[i] = "";
        	plating[i] = false;
        	plates[i] = "";
        }
        
     
    }

    public void updatePosition() {
        
    }
    
    public boolean isPresent() {
        return true;
    }


    public void DoCooking(String f, int n) {
        grills[n-1] = f;
        plates[n-1] = f;
        cooking[n-1] = true;
    }
    
    public void doneCooking(int t){
    	  grills[t-1] = "";
          cooking[t-1] = false;
          plating[t-1] = true;
          
    }
    
    public void collected(int t){
    	plating[t-1] = false;
    	plates[t-1] = "";
    }
    
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void draw(Graphics2D g) {
        g.setColor(Color.blue);
        g.fillRect(xPos, yPos, 20, 20);
        
        g.setColor(Color.black);
        if(cooking[0]){
    		if(grills[0].equals("steak")){
    			g.drawString("ST", grillX, grillY);
    		}
    		if(grills[0].equals("chicken")){
    			g.drawString("CH", grillX, grillY);
    		}
    		if(grills[0].equals("salad")){
    			g.drawString("SAL", grillX, grillY);
    		}
    		if(grills[0].equals("pizza")){
    			g.drawString("PZ", grillX, grillY);
    		}
    	}
        if(cooking[1]){
    		int grillX1 = grillX+20;
    		if(grills[1].equals("steak")){
    			g.drawString("ST", grillX1, grillY);
    		}
    		if(grills[1].equals("chicken")){
    			g.drawString("CH", grillX1, grillY);
    		}
    		if(grills[1].equals("salad")){
    			g.drawString("SAL", grillX1, grillY);
    		}
    		if(grills[1].equals("pizza")){
    			g.drawString("PZ", grillX1, grillY);
    		}
    	}
        if(cooking[2]){
    		int grillX2 = grillX+40;
    		if(grills[2].equals("steak")){
    			g.drawString("ST", grillX2, grillY);
    		}
    		if(grills[2].equals("chicken")){
    			g.drawString("CH", grillX2, grillY);
    		}
    		if(grills[2].equals("salad")){
    			g.drawString("SAL", grillX2, grillY);
    		}
    		if(grills[2].equals("pizza")){
    			g.drawString("PZ", grillX2, grillY);
    		}
    	}
        if(cooking[3]){
    		int grillX3 = grillX+60;
    		if(grills[3].equals("steak")){
    			g.drawString("ST", grillX3, grillY);
    		}
    		if(grills[3].equals("chicken")){
    			g.drawString("CH", grillX3, grillY);
    		}
    		if(grills[3].equals("salad")){
    			g.drawString("SAL", grillX3, grillY);
    		}
    		if(grills[3].equals("pizza")){
    			g.drawString("PZ", grillX3, grillY);
    		}
    	}
        
        if(plating[0]){
    		if(plates[0].equals("steak")){
    			g.drawString("ST", plateX, plateY);
    		}
    		if(plates[0].equals("chicken")){
    			g.drawString("CH", plateX, plateY);
    		}
    		if(plates[0].equals("salad")){
    			g.drawString("SAL", plateX, plateY);
    		}
    		if(plates[0].equals("pizza")){
    			g.drawString("PZ", plateX, plateY);
    		}
    	}
        if(plating[1]){
    		int plateY1 = plateY+20;
    		if(plates[1].equals("steak")){
    			g.drawString("ST", plateX, plateY1);
    		}
    		if(plates[1].equals("chicken")){
    			g.drawString("CH", plateX, plateY1);
    		}
    		if(plates[1].equals("salad")){
    			g.drawString("SAL", plateX, plateY1);
    		}
    		if(plates[1].equals("pizza")){
    			g.drawString("PZ", plateX, plateY1);
    		}
    	}
        if(plating[2]){
    		int plateY2 = plateY+40;
    		if(plates[2].equals("steak")){
    			g.drawString("ST", plateX, plateY2);
    		}
    		if(plates[2].equals("chicken")){
    			g.drawString("CH", plateX, plateY2);
    		}
    		if(plates[2].equals("salad")){
    			g.drawString("SAL", plateX, plateY2);
    		}
    		if(plates[2].equals("pizza")){
    			g.drawString("PZ", plateX, plateY2);
    		}
    	}
        if(plating[3]){
    		int plateY3 = plateY+60;
    		if(plates[3].equals("steak")){
    			g.drawString("ST", plateX, plateY3);
    		}
    		if(plates[3].equals("chicken")){
    			g.drawString("CH", plateX, plateY3);
    		}
    		if(plates[3].equals("salad")){
    			g.drawString("SAL", plateX, plateY3);
    		}
    		if(plates[3].equals("pizza")){
    			g.drawString("PZ", plateX, plateY3);
    		}
    	}

    }
   
}