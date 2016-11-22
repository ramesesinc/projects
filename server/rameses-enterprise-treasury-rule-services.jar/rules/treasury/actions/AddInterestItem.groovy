package rules.treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import rules.treasury.facts.*;
import com.rameses.osiris3.common.*;

/***
* Parameters:
*    billitem
*    amount 
****/
class AddInterestItem implements RuleActionHandler {

	public void execute(def params, def drools) {
		def billitem = params.billitem;
		def acct = params.account;
		def amt = params.amount.doubleValue;

		def intItem = new InterestItem(parent: billitem, amount:amt);
		intItem.account = new Account( objid: acct.key , title: acct.value );

		boolean b = billitem.items.add( intItem );
		if(b) {
			def ct = RuleExecutionContext.getCurrentContext();
			ct.result.billitems << intItem;
			ct.facts << intItem;	
		}
	}

}