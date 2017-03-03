package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
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
			throw new Exception("treasury.actions.SummarizeBillItem");
		def ct = RuleExecutionContext.getCurrentContext();
		def facts = ct.facts;

		def obj = facts.find{ (it instanceof SummaryBillItem) && it.account.objid == billitem.account.objid  };
		if( !obj ) {
			obj = new SummaryBillItem();
			obj.account = billitem.account;
			obj.aggtype = aggtype;
			obj.dynamic = billitem.dynamic;
			obj.txntype = billitem.txntype;
			obj.sortorder = billitem.sortorder;
			facts << obj;
		};
		
		obj.items << billitem;
		obj.amount = NumberUtil.round(obj.items.sum{ it.amount });

		//Calculate the amount 
		/*
		if(aggtype =="SUM" ) {
			obj.amount = obj.items.sum{ it.amount };
		}
		else if( aggtype == "COUNT") {
			obj.amount = obj.items.size();
		}
		else if( aggtype == "AVG" ) {
			obj.amount = obj.items.sum{ it.amount } / obj.items.size();
		}
		else if( aggtype == "MIN" ) {
			obj.amount = obj.items.min{ it.amount };
		}
		else if( aggtype == "MAX" ) {
			obj.amount = obj.items.max{ it.amount };
		}
		*/
		//remove the billitem in the main facts and billitems
		facts.remove( billitem );
		drools.retract( billitem );		
	}

}