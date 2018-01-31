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
class AddExcessBillItem extends AddBillItem {

	public void execute(def params, def drools) {
		def amt = params.amount.decimalValue;

		if( !params.account || params.account.key == "null" ) 
			throw new Exception("Account is required");

		if( !params.reftype) 
			throw new Exception("reftype is required. This should be the table where excess payment is stored");


		def billitem = new BillItem(amount: NumberUtil.round( amt));
		billitem.txntype = "excess";
		billitem.reftype = params.reftype;

		def acct = params.account;
		if(  acct ) {
			setAccountFact( billitem, acct.key );
		}
		addToFacts( billitem );
	}


}