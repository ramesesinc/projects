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

		def ct = RuleExecutionContext.getCurrentContext();
		boolean b = billitem.items.add(surItem);
		if(b) {
			ct.facts << surItem;	
		}
	}

}