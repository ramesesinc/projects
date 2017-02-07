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
class AddBillSubItem extends AbstractAddBillItem {

	public def createSubItemFact( def billitem, def amt, def txntype ) {
		def subItem = new BillSubItem(parent: billitem, amount:amt);
		subItem.txntype = txntype;
		return subItem;
	}

	public void execute(def params, def drools) {
		def billitem = params.billitem;
		def acct = params.account;
		def amt = params.amount.doubleValue;
		def txntype = params.txntype;

		def subItem = createSubItemFact(  billitem, amt, txntype );
		setAccountFact( acct.key, subItem );
		boolean b = billitem.items.add(subItem);

		//add to facts so it can be evaluated...
		if(b) {
			getBillItems() << subItem;
			getFacts() << subItem;	
		}
	}

}