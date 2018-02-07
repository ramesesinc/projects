package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;
import treasury.utils.*;

/***
* Parameters:
*    billitem
*    account
****/
class SetBillItemAccount implements RuleActionHandler {

	public void execute(def params, def drools) {
		def billitem = params.billitem;
		def acct = params.account;

		def ct = RuleExecutionContext.getCurrentContext();		
		if( !ct.env.acctUtil ) ct.env.acctUtil = new ItemAccountUtil();
		billitem.account = ct.env.acctUtil.createAccountFact( [objid: acct.key] );
	}

}