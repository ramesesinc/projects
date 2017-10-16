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
		def amt = params.amount.decimalValue;

		if(!params.account && !params.txntype ) {
			throw new Exception("AddBillItem error. Please specify an account or txntype in rule");
		}

		def billitem = new BillItem(amount: NumberUtil.round( amt));
		if( params.txntype?.key && params.txntype?.key != "null" ) {
			billitem.txntype = params.txntype.key;
		}

		def acct = params.account;
		if(  acct ) {
			setAccountFact( billitem, acct.key );
		}
		
		addToFacts( billitem );
	}

}