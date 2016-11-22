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
class AddSurchargeItem implements RuleActionHandler {

	public void execute(def params, def drools) {
		def billitem = params.billitem;
		def acct = params.account;
		def amt = params.amount.doubleValue;

		def surItem = new SurchargeItem(parent: billitem, amount:amt);
		surItem.account = new Account( objid: acct.key , title: acct.value );
		boolean b = billitem.items.add(surItem);

		//add to facts so it can be evaluated...
		if(b) {
			def ct = RuleExecutionContext.getCurrentContext();
			ct.result.billitems << surItem;
			ct.facts << surItem;	
		}
	}

}