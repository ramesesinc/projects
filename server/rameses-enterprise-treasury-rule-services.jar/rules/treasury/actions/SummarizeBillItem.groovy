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

		def z = billitems.find{ (it instanceof SummaryBillItem) && it.account.objid == billitem.account.objid  };
		if( !z ) {
			def obj = new SummaryBillItem();
			obj.items << billitem;
			obj.account = billitem.account;
			obj.aggtype = aggtype;

			//remove items
			billitems.remove( billitem );
			facts.remove( billitem );
			drools.retract( billitem );

			billitems << obj;			
			facts << obj;
		}
		else {
			z.items << billitem;
		}
	}

}