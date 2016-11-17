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
class UpdateBillItemSurcharge implements RuleActionHandler {

	public void execute(def params, def drools) {
		def acct = params.account;
		def amt = params.amount.doubleValue();
		def bi = params.billitem;

		def ct = RuleExecutionContext.getCurrentContext();
		bi.surcharge = amt;

		if(!ct.result.surcharges) ct.result.surcharges = [];
		def items = ct.result.surcharges;

		def surAcct = ct.facts.find{ it instanceof BillItem }.find{ it.account.objid == acct.key };
		if( surAcct ) {
			surAcct.amount += amt;
			ct.items << 
		}
		else {
			ct.facts
		}
	}

}