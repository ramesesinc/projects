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

		def facts = ct.facts;

		def ct = RuleExecutionContext.getCurrentContext();
		def billitems = ct.result.billitems.sort{it.sortorder};

		if(billitems) {
			def newBillItems = [];

			double amt = payment.amount;

			for( BillItem b: billitems ) {
				double linetotal = b.amount + b.items.sum{ it.amount };
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
					break;
				}
			}

			//remove all billitems
			billitems.clear();
			billitems.addAll( newBillItems );

			//remove all billitems in facts
			def removeList = facts.findAll{ it instanceof AbstractBillItem };
			facts.removeAll( removeList );
			facts.remove( payment );

			//add new facts
			for(b in billitems) {
				facts << b;
				for(bi in b.items) {
					facts << bi;
				}		
			}
			
			//add excess payment if any...
			if(  amt > 0 ) {
				facts << new ExcessPayment( amount: amt );
			}	
		}

		
	}

}
