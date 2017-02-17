package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import treasury.utils.*;
import com.rameses.osiris3.common.*;


/***
* Description: Simple Add of Item. Item is unique based on the account. 
* Parameters:
*    account 
*    amount
****/
public abstract class AbstractAddBillItem implements RuleActionHandler {

	public List getFacts() {
		def ct = RuleExecutionContext.getCurrentContext();
		return ct.facts;
	}

	public def getBillItems() {
		def ct = RuleExecutionContext.getCurrentContext();
		if(!ct.result.billitems) {
			ct.result.billitems = new LinkedHashSet<BillItem>();
		}
		return ct.result.billitems;
	}

	public void setAccountFact( def acctid, def billitem ) {
		def ct = RuleExecutionContext.getCurrentContext();		
		if( !ct.env.acctUtil ) ct.env.acctUtil = new ItemAccountUtil();
		billitem.account = ct.env.acctUtil.createAccountFact( [objid: acctid] );
		//billitem.sortorder = acct.sortorder;
	}


}