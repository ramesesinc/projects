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
class AddCreditBillItem extends AddBillItem {

	public void execute(def params, def drools) {
		def amt = params.amount.decimalValue;

		if( !params.account || params.account.key == "null" ) 
			throw new Exception("Account is required in AddCreditBillItem");


		def billitem = new CreditBillItem(amount: NumberUtil.round( amt), txntype: 'credit');
		def acct = params.account;
		if ( acct ) {
			setAccountFact( billitem, acct.key );
		}
		addToFacts( billitem );
	}


}