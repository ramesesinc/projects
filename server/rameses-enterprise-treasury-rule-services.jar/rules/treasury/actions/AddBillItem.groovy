package rules.treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import rules.treasury.facts.*;
import com.rameses.osiris3.common.*;

/***
* Description: Simple Add of Item. Item is unique based on the account. 
* Parameters:
*    account 
*    amount
****/
class AddBillItem extends AbstractAddBillItem {

	public void execute(def params, def drools) {
		def acct = params.account;
		def amt = params.amount.decimalValue;

		def ct = RuleExecutionContext.getCurrentContext();
		if(!ct.result.billitems) {
			ct.result.billitems = new LinkedHashSet<BillItem>();
		}
			
		def billitem = new BillItem(amount: amt);
		setAccountFact( acct.key, billitem );

		//check first if there is already existing. If successfully added, then add to facts
		boolean b = ct.result.billitems.add(billitem);
		if(b) {
			ct.facts << billitem;	
		}
	}

}