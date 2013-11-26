package bank.interfaces;

import bank.gui.SecurityGuardGui;
import agent.Role;
import agent.WorkRole;
import agent.gui.Gui;


public interface SecurityGuard {

    public abstract void msgCustomerArrived(BankCustomer bc);
	
    public abstract void msgTellerOpen(Teller t);
    
    public abstract void msgLeavingBank(BankCustomer bc);
    
    public abstract void addRole(WorkRole r);
    
    public abstract void addTeller(Teller t, int deskX);
    
    public abstract void msgAtDestination();
    
    public abstract void setGui(SecurityGuardGuiInterface g);

	public abstract String getName();
	
	public abstract int getWaitingCustomersSize();
	
	public abstract int getTellerPositionsSize();
}