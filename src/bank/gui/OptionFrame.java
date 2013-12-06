package bank.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class OptionFrame extends JFrame implements ActionListener {

    private JButton addCustomerButton = new JButton("add Customer");;
    private JButton addTellerButton = new JButton("add teller");
    private JButton endWorkDayButton = new JButton("end work day");
    private JButton resumeWorkButton = new JButton("resume work button");
    private JTextField text1 = new JTextField();
    private JTextField text2 = new JTextField();
    private GridLayout optionGridLayout = new GridLayout(3,2);
	
	OptionFrame() {
		this.setLayout(optionGridLayout);
//        this.add(addCustomerButton);
//        this.add(text1);
//        this.add(addTellerButton);
//        this.add(text2);
//        this.add(endWorkDayButton);
//        this.add(resumeWorkButton);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}