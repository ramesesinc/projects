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

		def remarks = null;
		if( params.remarks ) {
			remarks = params.remarks.eval();		
		}

		def billitem = new BillItem(amount: amt);
		billitem.remarks = remarks;
		setAccountFact( acct.key, billitem );

		//check first if there is already existing. If successfully added, then add to facts
		boolean b = getBillItems().add(billitem);
		if(b) {
			getFacts() << billitem;	
		}
	}

}