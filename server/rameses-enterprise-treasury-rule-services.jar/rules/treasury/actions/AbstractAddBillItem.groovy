package rules.treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import rules.treasury.facts.*;
import com.rameses.osiris3.common.*;

/***
* Description: Simple Add of Item. Item is unique based on the account. 
* Parameters:
*    account 
*    amount
****/
public abstract class AbstractAddBillItem implements RuleActionHandler {

	public void setAccountFact( def acctid, def billitem ) {
		def ct = RuleExecutionContext.getCurrentContext();		
		if( !ct.env.acctLookup ) ct.env.acctLookup = new ItemAccountLookup();
		def acctLookup = new ItemAccountLookup();
		def acct = acctLookup.lookup( acctid );
		Fund f = new Fund( objid: acct.fund.objid, code: acct.fund.code, title: acct.fund.title);
		billitem.account = new Account( objid: acct.objid, code: acct.code, title: acct.title, fund: f);
		//billitem.sortorder = acct.sortorder;
	}


}