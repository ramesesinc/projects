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

		def billitems = facts.findAll{ it instanceof BillItem }.sort{it.paypriority};

		if(billitems) {

			def newBillItems = [];
			double amt = payment.amount;

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
			facts.remove( payment );


			//effect immediately in the drools engine.
			drools.retract(payment);
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

			//add excess payment if any...
			if(  amt > 0 ) {
				def ep = new ExcessPayment( amount: amt );
				facts << ep;
				drools.insert( ep );
			}	
		}
	}

}
