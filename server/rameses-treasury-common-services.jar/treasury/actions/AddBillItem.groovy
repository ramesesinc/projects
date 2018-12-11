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

		int t = 0;

		if( !params.account || params.account.key == "null" ) t=1;
		if( !params.txntype || params.txntype.key == "null" ) t=t+1;

		if( t == 2 ) {
			throw new Exception("AddBillItem error. Please specify an account or txntype in rule "  );
		}

		def billitem = new BillItem(amount: NumberUtil.round( amt));
		if( params.txntype?.key && params.txntype?.key != "null" ) {
			billitem.txntype = params.txntype.key;
		}


		def acct = params.account;
		if(  acct ) {
			setAccountFact( billitem, acct.key );
		}

		//set the other parameters
		if( params.year ) billitem.year = params.year.eval();	
		if( params.month ) billitem.month = params.month.eval();		
		if( params.fromdate ) billitem.fromdate = params.fromdate.eval();		
		if( params.todate ) billitem.todate = params.todate.eval();		
		if( params.remarks ) billitem.remarks = params.remarks.eval();
		if( params.refid) billitem.refid = params.refid;

		addToFacts( billitem );
	}

}