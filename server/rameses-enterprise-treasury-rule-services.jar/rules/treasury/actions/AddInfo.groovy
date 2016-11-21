package rules.treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import rules.treasury.facts.*;
import com.rameses.osiris3.common.*;

/***
* Parameters:
****/
class AddInfo implements RuleActionHandler {

	public void execute(def params, def drools) {

		def ct = RuleExecutionContext.getCurrentContext();

		def acct = params.account;
		def amt = params.amount.doubleValue;
		def remarks = null;
		try {
			remarks = params.remarks.eval();
		}
		catch(e) {;}

		def ct = RuleExecutionContext.getCurrentContext();
		if(!ct.result.items) ct.result.items = [];
		def items = ct.result.items;

		def exists = items.findAll{ it instanceof BillItem }.find{ it.account.objid == acct.key && it.remarks == remarks };
		if( !exists ) {
			//check first if there is already existing
			def billitem = new BillItem();
			billitem.account = new Account( objid: acct.key , title: acct.value );
			billitem.amount = amt;
			billitem.remarks = remarks;
			items << billitem;
		}

	}

}