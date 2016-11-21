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
class AddDiscountItem implements RuleActionHandler {

	public void execute(def params, def drools) {

		def ct = RuleExecutionContext.getCurrentContext();
		
		def billitem = params.billitem;
		def acct = params.account;
		def amt = params.amount.doubleValue;

		def disc = new DiscountItem(parent: billitem, amount:amt);
		disc.account = new Account( objid: acct.key , title: (acct.value * -1) );

		def ct = RuleExecutionContext.getCurrentContext();
		boolean b = billitem.items.add(disc);

		//add to facts so it can be evaluated...
		if(b) {
			ct.facts << disc;	
		}

	}

}