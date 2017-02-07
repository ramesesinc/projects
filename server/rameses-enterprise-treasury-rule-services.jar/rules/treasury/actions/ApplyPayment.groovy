package rules.treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import rules.treasury.facts.*;
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
		def billitems = ct.result.billitems.sort{it.sortorder};

		if(billitems) {
			
			def facts = ct.facts;

			def newBillItems = [];

			double amt = payment.amount;

			for( BillItem b: billitems ) {
				double linetotal = b.amount +  (b.items ? b.items.sum{ it.amount } :0 );
				if(amt > linetotal) {
					newBillItems << b;
					amt -= linetotal;
				}
				else {
					b.amount = ( b.amount / linetotal )*amt;
					for(BillItem bi: b.items) {
						bi.amount = (bi.amount / linetotal ) * amt;
					}
					newBillItems << b;
					amt = 0;
					break;
				}
				if(amt<=0) break;
			}

			//remove all billitems
			billitems.clear();
			billitems.addAll( newBillItems );
			ct.result.billitems = billitems;

			//remove all billitems in facts
			def removeList = facts.findAll{ it instanceof AbstractBillItem };
			facts.removeAll( removeList );
			facts.remove( payment );

			drools.retract(payment);

			//add new facts
			for(b in billitems) {
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
