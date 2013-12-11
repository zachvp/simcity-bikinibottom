package restaurant.lucas.test.mock;


import java.awt.Dimension;
import java.util.Map;

import restaurant.lucas.interfaces.Cashier;
import restaurant.lucas.interfaces.Customer;




/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Jack Lucas
 *
 */
public class MockWaiter extends Mock implements restaurant.lucas.interfaces.Waiter {

        /**
         * Reference to the Cashier under test that can be set by the unit test.
         */
        public Cashier cashier;
        public EventLog log = new EventLog();;

        public MockWaiter(String name) {
                super(name);

        }

		@Override
		public void msgAtDestination() {
			// TODO Auto-generated method stub
			
		}

//		@Override
//		public void msgSitAtTable(Customer c, int table) {
//			// TODO Auto-generated method stub
//			
//		}

		@Override
		public void msgSetBreakBit() {
			// TODO Auto-generated method stub
			
		}
//
//		@Override
//		public void msgImReadyToOrder(Customer c) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void msgHereIsMyChoice(Customer c, String choice) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void msgOutOfFood(int tableNum, String choice) {
//			// TODO Auto-generated method stub
//			
//		}

//		@Override
//		public void msgOrderIsReady(Customer c, String choic, int table,
//				Dimension p) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void msgReadyToPay(Customer c) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void msgHereIsCheck(Customer c, double check) {
//			log.add("received msgHereIsCheck from cashier" + check);
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void msgLeavingTable(Customer c) {
//			// TODO Auto-generated method stub
//			
//		}

		@Override
		public void msgGoOnBreak() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgSitAtTable(restaurant.lucas.interfaces.Customer c,
				int table) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgImReadyToOrder(restaurant.lucas.interfaces.Customer c) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgHereIsMyChoice(restaurant.lucas.interfaces.Customer c,
				String choice) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgReadyToPay(restaurant.lucas.interfaces.Customer c) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgHereIsCheck(restaurant.lucas.interfaces.Customer c,
				double check) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgLeavingTable(restaurant.lucas.interfaces.Customer c) {
			// TODO Auto-generated method stub
			
		}

		
       

}