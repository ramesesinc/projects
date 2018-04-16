package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;


/***
* Description: Simple Add of Item. Item is unique based on the account. 
* This is used for overpayment
* Parameters:
*    account 
*    amount
****/
class AddExcessBillItem extends AddBillItem {

	public void execute(def params, def drools) {
		def amt = params.amount.decimalValue;

		if( !params.account || params.account.key == "null" ) 
			throw new Exception("Account is required");

		def billitem = new CreditBillItem(amount: NumberUtil.round( amt), txntype: 'credit');
		billitem.remarks = "EXCESS PAYMENT";
		def acct = params.account;
		if(  acct ) {
			setAccountFact( billitem, acct.key );
		}
		addToFacts( billitem );
	}


}