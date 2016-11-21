package rules.treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import rules.treasury.facts.*;
import com.rameses.osiris3.common.*;

/***
* Parameters:
*    billitem
*    type ( SUM, MAX, MIN )
****/
class SummarizeBillItem implements RuleActionHandler {

	public void execute(def params, def drools) {
		def billitem = params.billitem;
		def aggtype = params.aggtype;

		if(!billitem)
			throw new Exception("rules.treasury.actions.SummarizeBillItem");
		def ct = RuleExecutionContext.getCurrentContext();
		def billitems = ct.result.billitems;
		def facts = ct.facts;

		def z = billitems.find{ it.account == billitem.account && it.summary == true };
		if( !z ) {
			def obj = billitem.getClass().newInstance();
			obj.account = billitem.account;
			obj.amount = billitem.amount;
			obj.summary = true;

			//remove items
			billitems.remove( billitem );
			facts.remove( billitem );
			drools.retract( billitem );

			billitems << obj;			
			facts << obj;
		}
		else {
			if( aggtype == "SUM") {
				z.amount =	z.amount + billitem.amount;
			}
		}

		/*
		def test = new BillItem(account: billitem.account);
		def newBillItem = ctx.result.billitems.find{ it == billitem };
		if(newBillItem) {
			newBillItem.amount += billitem.amount;
		}
		else {
			newBillItem.amount = billitem.amount;
		}
		*/
	}

}