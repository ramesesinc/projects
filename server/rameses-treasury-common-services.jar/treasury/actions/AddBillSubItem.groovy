package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;

/***
* Parameters:
*    billitem
*    amount 
****/
class AddBillSubItem extends AbstractAddBillItem {

	public def createSubItemFact( def billitem, def amt, def txntype ) {
		def subItem = new BillSubItem(parent: billitem);
		subItem.amount = NumberUtil.round(amt);
		if(txntype!=null && txntype!='null') subItem.txntype = txntype;
		return subItem;
	}

	public void execute(def params, def drools) {
		def billitem = params.billitem;
		def acct = params.account;
		def amt = params.amount.doubleValue;


		def txntype = params.txntype;
		if(txntype!=null &&  !(txntype instanceof String )) {
			txntype = params.txntype?.key;
			if( txntype == "null") txntype = null;
		}

		if( billitem == null ) throw new Exception("Please add billitem in AddBillSubItem of " + drools.rule.name );
		if( acct == null && txntype == null ) throw new Exception("Please specify account or txntype in AddBillSubItem of " + drools.rule.name );
		if( amt == null ) throw new Exception("Please specify amount in AddBillSubItem of " + drools.rule.name );

		def subItem = createSubItemFact(  billitem, amt, txntype );
		if(acct!=null) {
			setAccountFact( subItem, acct.key );
		};	
		boolean b = billitem.items.add(subItem);

		//add to facts so it can be evaluated...
		if(b) {
			getFacts() << subItem;	
		}
	}

}