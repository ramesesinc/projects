package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;

/***
* Parameters:
*    billitem
****/
class ApplyPayment implements RuleActionHandler {

	public void execute(def params, def drools) {
		def payment = params.payment;
		if(!payment) throw new Exception("Payment fact is required in ApplyPayment action");

		def ct = RuleExecutionContext.getCurrentContext();
		def facts = ct.facts;

		double totalCredit = 0;
		def creditList = facts.findAll{ it instanceof CreditBillItem };
		if(creditList ) {
			totalCredit = (creditList.sum{ it.amount }*-1);
		}
		
		double amt = payment.amount + totalCredit;

		def billitems = facts.findAll{ it instanceof BillItem }.sort{it.paypriority};

		if(billitems) {

			def newBillItems = [];
			
			/******************************************************************************
			* This is already a tested routine. If you cannot get the desired result
			* please check the payment priority order. 
			*******************************************************************************/
			for( BillItem b: billitems ) {
				amt = b.applyPayment( amt );
				newBillItems << b;
				if( amt == 0 ) break;
			}

			//remove all billitems in facts
			def removeList = facts.findAll{ it instanceof AbstractBillItem };
			facts.removeAll( removeList );
			
			removeList.each {
				drools.retract( it );
			}

			//add new facts
			for(b in newBillItems) {
				facts << b;
				for(bi in b.items) {
					facts << bi;
				}		
			}

			//if there are credit items
			if( creditList ) {
				facts.addAll( creditList );
			}

		}

		//add excess payment if any...
		if(  amt > 0 ) {
			def ep = new ExcessPayment( amount: amt );
			facts << ep;
			drools.insert( ep );
		}	

		//effect immediately in the drools engine removal of the payment so it can be used again
		facts.remove( payment );
		drools.retract(payment);
		
	}

}
