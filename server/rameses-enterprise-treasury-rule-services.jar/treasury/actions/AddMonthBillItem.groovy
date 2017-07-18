package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;


/***
* Description: Simple Add of Item. Item is unique based on the account. 
* Parameters:
*    account 
*    amount
****/
class AddMonthBillItem extends AbstractAddBillItem {

	public void execute(def params, def drools) {

		def acct = params.account;
		def me = params.monthentry;
		def amt = params.amount.decimalValue;

		def remarks = null;
		if( params.remarks ) {
			remarks = params.remarks.eval();		
		}

		def billitem = new MonthBillItem(amount: NumberUtil.round( amt), year: me.year, month: me.month );
		billitem.fromdate = me.fromdate;
		billitem.todate = me.todate;
		
		billitem.remarks = remarks;
		if( params.txntype?.key ) {
			billitem.txntype = params.txntype.key;
		}
		setAccountFact( billitem, acct.key );
		addToFacts( billitem );
	}

}