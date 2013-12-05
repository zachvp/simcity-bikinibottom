package gui;

import housing.gui.HousingInfoPanel;
import housing.roles.ResidentialBuilding;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import market.gui.MarketBuilding;
import CommonSimpleClasses.Constants;
import CommonSimpleClasses.TimeManager;
import agent.PersonAgent;

/**
 * A Panel that displays information about the person or building
 * Also contains some person/building controls
 * @author Victoria Dea
 *
 */
public class InfoPanel extends JPanel implements ActionListener{
	
	private Dimension d;
	private JLabel info;
	JPanel card = new JPanel();
	JPanel timePanel = new JPanel();
	JLabel time;
	int hour, min, sec;
	String hourStr, minStr, secStr;
	
	TimeManager timeManager = TimeManager.getInstance();
	private Timer timer;
	
	JLabel name, job, residence, money, hunger, nameL, jobL, residenceL, moneyL, hungerL;
	
	public InfoPanel(int w, int h){
		d = new Dimension(Constants.INFO_PANEL_WIDTH, h); //700 X 190
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		setLayout(new BorderLayout());
		
		Dimension cardDim = new Dimension(Constants.INFO_PANEL_WIDTH, h-46); //700 X 145
		card.setPreferredSize(cardDim);
		card.setMaximumSize(cardDim);
		card.setMinimumSize(cardDim);
		card.setLayout(new CardLayout());
		
		JPanel personText = new JPanel();
		personText.setPreferredSize(d);
		personText.setMaximumSize(d);
		personText.setMinimumSize(d);
		personText.setLayout(new BorderLayout());
		
		JPanel textWest = new JPanel();
		Dimension textWDim = new Dimension((int)(Constants.INFO_PANEL_WIDTH*0.2), cardDim.height);
		textWest.setPreferredSize(textWDim);
		textWest.setMaximumSize(textWDim);
		textWest.setMinimumSize(textWDim);
		textWest.setLayout(new GridLayout(5, 1, 5, 2));
		
		nameL = new JLabel("");
		jobL = new JLabel("");
		residenceL = new JLabel("");
		moneyL = new JLabel("");
		hungerL = new JLabel("");
		
		textWest.add(nameL);
		textWest.add(jobL);
		textWest.add(residenceL);
		textWest.add(moneyL);
		textWest.add(hungerL);
		
		JPanel textEast = new JPanel();
		Dimension textEDim = new Dimension((int)(Constants.INFO_PANEL_WIDTH*0.8), cardDim.height);
		textEast.setPreferredSize(textEDim);
		textEast.setMaximumSize(textEDim);
		textEast.setMinimumSize(textEDim);
		textEast.setLayout(new GridLayout(5, 1, 5, 2));
		
		name = new JLabel("");
		job = new JLabel("");
		residence = new JLabel("");
		money = new JLabel("");
		hunger = new JLabel("");
		
		textEast.add(name);
		textEast.add(job);
		textEast.add(residence);
		textEast.add(money);
		textEast.add(hunger);
		
		personText.add(textWest, BorderLayout.WEST);
		personText.add(textEast, BorderLayout.EAST);

		card.add(personText, "person");

		//info = new JLabel("");
		//personText.add(info);
		
		timer = new java.util.Timer();

    	// timer.scheduleAtFixedRate(new PrintTask(), 0, 500);
		time = new JLabel();
		timePanel.add(time);
		//timePanel.setBorder(BorderFactory.createEtchedBorder());
		//card.setBorder(BorderFactory.createEtchedBorder());
		
		add(card, BorderLayout.SOUTH);
		add(timePanel, BorderLayout.NORTH);
	}
	
	
	
	private void getTimeDisplay(){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeManager.currentSimTime());
//		Date date = cal.getTime();
//		hour = date.getHours();
//		min = date.getMinutes();
//		sec = date.getSeconds();
//		hourStr = "" + hour;
//		minStr = "" + min;
//		secStr = ""+ sec;
//		if(hour<10){
//			hourStr = "0" + hour;
//		}
//		if(min<10){
//			minStr = "0" + min;
//		}
//		if(sec<10){
//			secStr = "0" + sec;
//		}
		
		SimpleDateFormat format = new SimpleDateFormat("E, MM-dd-yyyy, HH:mm");
		time.setText(format.format(cal.getTime()) + " in Encino, CA");
		
	}

	/**
	 * Displays the Person information
	 * @param p Person name
	 */
	public void updatePersonInfoPanel(PersonAgent person){
		CardLayout cl = (CardLayout)(card.getLayout());
		cl.show(card, "person");
		//System.out.println("update info with "+person.getName());
		/*info.setText("<html><div>&nbsp;</div><div> "
						+ "Name: "+ person.getName() +"</div><div>&nbsp;</div>"
						+ "<div> Job: "+ (person.getWorkRole() == null ? "none" : person.getWorkRole().getShortName()) + "</div><div>&nbsp;</div>"
						+ "<div> Residence: "+ (person.getResidentRole() == null ? "none" : person.getResidentRole().getDwelling()) + "</div><div>&nbsp;</div>"
						+ "<div> Money: $"+ person.getWallet().getCashOnHand() +"</div><div>&nbsp;</div>"
						+ "<div> Hunger Level: "+ person.getHungerLevel() +"</div></html>"
				);*/
		
		nameL.setText("Name & Loc.: ");
		jobL.setText("Job: ");
		residenceL.setText("Residence: ");
		moneyL.setText("Money: ");
		hungerL.setText("Hunger Level: ");
		
		
		name.setText(person.getName() + " @ " + person.getPassengerRole().getLocation());
		job .setText((person.getWorkRole() == null ? "none" : person.getWorkRole().toString()));
		residence.setText(""+(person.getResidentRole() == null ? "none" : person.getResidentRole().getDwelling()));
		money.setText("$"+person.getWallet().getCashOnHand());
		hunger.setText(""+person.getHungerLevel());		
		validate();
	}
	
	/**
	 * Displays the Building information
	 * @param b Building name
	 */
	public void updateBuildingInfoPanel(Building b){
		//Building building = b;
		//System.out.println(b.getName()+ " update info panel");
		if (b instanceof MarketBuilding){
			((MarketBuilding) b).UpdateInfoPanel();
		}
		//if(b instanceof ResidentialBuilding){
		//	 ((HousingInfoPanel)(b.getInfoPanel())).updatePanel();
		//}
		
		CardLayout cl = (CardLayout)(card.getLayout());
		if(b.getInfoPanel() == null){
			cl.show(card, "blank");
		}
		else
		{
			cl.show(card, b.getName());
		}
		
		validate();

	}
	
	/**
	 * Adds the Building's control panel to the cardlayout
	 * @param panel Building's info panel
	 * @param name Building's name
	 */
	public void addBuildingInfoPanel(JPanel panel, String name){
		card.add(panel, name);
	}
	
	public void actionPerformed(ActionEvent e) {
		
	}
	
	class PrintTask extends TimerTask {
        public void run() {
        	getTimeDisplay();
        }
	}

}
