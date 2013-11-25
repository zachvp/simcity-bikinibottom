package bank.test;

import CommonSimpleClasses.CityLocation;
import agent.PersonAgent;
import bank.BankCustomerRole;
import bank.BankCustomerRole.State;
import bank.gui.BankBuilding;
import bank.test.mock.MockLoanManager;
import bank.test.mock.MockTeller;
import junit.framework.TestCase;

public class BankCustomerTest extends TestCase
{
        //these are instantiated for each test separately via the setUp() method.

		PersonAgent person = new PersonAgent("jack");
        BankCustomerRole bankCustomer;
        MockTeller teller;
        MockLoanManager loanManager;
        
        CityLocation testLocation = new BankBuilding(0,0,0,0);
        
        
        /**
         * This method is run before each test. You can use it to instantiate the class variables
         * for your agent and mocks, etc.
         */
        public void setUp() throws Exception{
                super.setUp();  
                bankCustomer = new BankCustomerRole(person, testLocation);
                teller = new MockTeller("mockTeller");
                loanManager = new MockLoanManager("mockLoanManager");
                bankCustomer.setTeller(teller);
        }        
        /**
         * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
         */
        
        public void initializationTest() { //tests to ensure Cashier is constructed properly
//        	  customer1.cashier = cashier;//You can do almost anything in a unit test.                        
              
              //check preconditions
        	
        	assertEquals("Bank customer should start with $50, did not", bankCustomer.getWalletAmount(), 90);
        	
//              assertEquals("Cashier should have 0 customers in it. It doesn't.",cashier.getMyCustomers().size(), 0); 
//              assertEquals("Cashier did not start with 1000 money", cashier.getMoney(),1000);
//              assertEquals("marketPayments list did not start empty", cashier.getMarketPayments().size(), 0);
//              assertEquals("myCustomers list did not start at size zero", cashier.getMyCustomers().size(), 0);
//              assertEquals("menuMap did not start with 4 items in it", cashier.getPriceMenu().size(), 4);
//              assertTrue("Cashier's name did not set correctly", cashier.getName().equals("cashier"));
        }
        
        public void testOpenNewAccount()
        {
        	bankCustomer.setMoney(50);
        	assertEquals("wrong state", bankCustomer.getState(), State.waiting);
          	assertEquals("wrong ID", bankCustomer.getAccountId(), -1);
        	assertEquals("Bank customer should start with $50, did not", bankCustomer.getWalletAmount(), 50.0);
            assertEquals(bankCustomer.getWalletThreshold(), 30.0);
            assertEquals(bankCustomer.getWalletCapacity(), 70.0);
            assertFalse("scheduler called", bankCustomer.pickAndExecuteAnAction());
            bankCustomer.msgGotToTeller();
            assertTrue("scheduler not called", bankCustomer.pickAndExecuteAnAction());
            assertTrue("message reception not logged", teller.log.containsString("received message" + 10));
            assertEquals("wrong state", bankCustomer.getState(), State.openingAccount);
            
            bankCustomer.msgAccountOpened(1001);
            assertEquals("wrong ID", bankCustomer.getAccountId(), 1001);
            assertTrue("scheduler not called", bankCustomer.pickAndExecuteAnAction());
            assertEquals("wrong amount", bankCustomer.getWalletAmount(), 40.0);
            assertEquals("wrong state", bankCustomer.getState(), State.leaving);

            //starts action speakToTeller() to open new account
//            assertEquals("state is incorrect", bankCustomer.getState(), State.waiting);
            
            
            
        }
             
        public void testGetLoan() {
        	bankCustomer.setMoney(50);
        	bankCustomer.setWalletAmount(20);
        	bankCustomer.setAccountId(1001);
        	assertEquals("wrong state", bankCustomer.getState(), State.waiting);
          	assertEquals("wrong ID", bankCustomer.getAccountId(), 1001);
        	assertEquals("Bank customer should start with $20, did not", bankCustomer.getWalletAmount(), 20.0);
            assertEquals(bankCustomer.getWalletThreshold(), 30.0);
            assertEquals(bankCustomer.getWalletCapacity(), 70.0);
            assertFalse("scheduler called", bankCustomer.pickAndExecuteAnAction());
            bankCustomer.msgGotToTeller();
            assertTrue("scheduler not called", bankCustomer.pickAndExecuteAnAction());
            assertEquals("wrong state", bankCustomer.getState(), State.gettingLoan);
            assertTrue("message reception not logged", teller.log.containsString("received loan message"));
            bankCustomer.msgSpeakToLoanManager(loanManager, 0);
            assertEquals("BC has wrong loanmanager", bankCustomer.getLoanManager(), loanManager);
            assertTrue("scheduler not called", bankCustomer.pickAndExecuteAnAction());
            assertEquals("wrong state", bankCustomer.getState(), State.atLoanManager);
            assertTrue("message reception not logged", loanManager.log.containsString("received loan message" + 30));
            bankCustomer.msgLoanApproved(30);
            assertTrue("scheduler not called", bankCustomer.pickAndExecuteAnAction());
            assertEquals("money not updated with loan", 50.0, bankCustomer.getWalletAmount());
            assertEquals("wrong state", bankCustomer.getState(), State.leaving);
            
        }
        
        public void testWithdraw() {
        	
        	double withdrawAmount = 30.0;
        	double accountCash = 100;
        	
        	bankCustomer.setAccountId(1001);
        	bankCustomer.setMoney(50);
        	bankCustomer.setWalletAmount(20);
        	bankCustomer.setAccountAmount(accountCash);
        	assertEquals("wrong state", bankCustomer.getState(), State.waiting);
        	assertEquals("wrong ID", bankCustomer.getAccountId(), 1001);
        	assertEquals("Bank customer should start with $20, did not", bankCustomer.getWalletAmount(), 20.0);
        	assertEquals("Bank Customer amount not started properly", bankCustomer.getAccountAmount(), 100.0);
        	assertEquals(bankCustomer.getWalletThreshold(), 30.0);
        	assertEquals(bankCustomer.getWalletCapacity(), 70.0);
        	assertFalse("scheduler called", bankCustomer.pickAndExecuteAnAction());
        	bankCustomer.msgGotToTeller();
        	assertTrue("scheduler not called", bankCustomer.pickAndExecuteAnAction());
        	assertEquals("wrong state", bankCustomer.getState(), State.withdrawing);
        	assertTrue("message reception not logged", teller.log.containsString("withdraw" + withdrawAmount));

        	bankCustomer.msgWithdrawSuccessful(withdrawAmount);
        	assertEquals(bankCustomer.getCashAdjustAmount(), withdrawAmount);
        	assertTrue("scheduler not called", bankCustomer.pickAndExecuteAnAction());
        	assertEquals("account info updated", bankCustomer.getAccountAmount(), accountCash - withdrawAmount);
        	assertEquals("wallet amount updated", bankCustomer.getWalletAmount(), withdrawAmount + 20);
        	assertEquals("cash adjustment not reset", bankCustomer.getCashAdjustAmount() , 0.0);
            assertEquals("wrong state", bankCustomer.getState(), State.leaving);
        }
        
        public void testDeposit() {
        	double depositAmount = 30;
        	double initialWalletAmount = 200;
        	double initialAccountAmount = 0;
        	
        	bankCustomer.setAccountId(1001);
        	bankCustomer.setMoney(50);
        	bankCustomer.setWalletAmount(initialWalletAmount);
        	bankCustomer.setAccountAmount(initialAccountAmount);
        	assertEquals("wrong state", bankCustomer.getState(), State.waiting);
        	assertEquals("wrong ID", bankCustomer.getAccountId(), 1001);
        	assertEquals("Bank customer should start with $200, did not", bankCustomer.getWalletAmount(), 200.0);
        	assertEquals("Bank Customer amount not started properly", bankCustomer.getAccountAmount(), 0.0);
        	assertEquals(bankCustomer.getWalletThreshold(), 30.0);
        	assertEquals(bankCustomer.getWalletCapacity(), 70.0);
        	assertFalse("scheduler called", bankCustomer.pickAndExecuteAnAction());
        	bankCustomer.msgGotToTeller();
        	assertTrue("scheduler not called", bankCustomer.pickAndExecuteAnAction());
        	assertEquals("wrong state", bankCustomer.getState(), State.depositing);
        	assertTrue("message reception not logged", teller.log.containsString("deposit" + depositAmount));
        	bankCustomer.msgDepositSuccessful(depositAmount);
        	assertEquals("cash adjust amount not set", bankCustomer.getCashAdjustAmount(), depositAmount*-1);

        	assertTrue("scheduler not called", bankCustomer.pickAndExecuteAnAction());
        	assertEquals("account info updated", bankCustomer.getAccountAmount(), initialAccountAmount + depositAmount);
        	assertEquals("wallet amount updated", bankCustomer.getWalletAmount(), 200 - depositAmount);
        	assertEquals("cash adjustment not reset", bankCustomer.getCashAdjustAmount() , 0.0);
        	assertEquals("wrong state", bankCustomer.getState(), State.leaving);
        }
}