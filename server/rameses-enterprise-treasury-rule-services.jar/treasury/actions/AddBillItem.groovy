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
class AddBillItem extends AbstractAddBillItem {

	public void execute(def params, def drools) {

		def acct = params.account;
		def amt = params.amount.decimalValue;

		def billitem = new BillItem(amount: NumberUtil.round( amt));
		if( params.txntype?.key ) {
			billitem.txntype = params.txntype.key;
		}
		setAccountFact( billitem, acct.key );
		addToFacts( billitem );
	}

}