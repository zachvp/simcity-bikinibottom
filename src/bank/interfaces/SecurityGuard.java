package bank.interfaces;


public interface SecurityGuard {

    public abstract void msgCustomerArrived(BankCustomer bc);
	
    public abstract void msgTellerOpen(Teller t);
}