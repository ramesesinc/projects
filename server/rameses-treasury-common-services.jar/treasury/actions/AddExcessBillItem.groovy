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


		def billitem = new BillItem(amount: NumberUtil.round( amt));

		//mark as credit
		billitem.txntype = "credit";

		def acct = params.account;
		if(  acct ) {
			setAccountFact( billitem, acct.key );
		}
		addToFacts( billitem );
	}


}