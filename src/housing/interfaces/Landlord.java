package housing.interfaces;

/** 
 * The landlord charges his residents rent once a month and
 * takes care of maintenance problems in their apartments
 * The landlord interfaces with tenants and the Bank Manager.
 * */
public interface Landlord {
	/** Normative Messages */
	/** From Bank Manager
	 * @param payment
	 * @param accountNumber
	*/
	public void msgReceivedPayment(double payment, int accountNumber);
	
	/** Non-Normative Messages */
	/** From Tenant
	 * @param complainant
	 * @param unitNumber
	 * @param problemType
	*/
	public void msgFileWorkOrder(Resident complainant, int unitNumber, String problemType);
}
