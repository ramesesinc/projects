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

	public void setAccountFact( def billitem, def acctid ) {
		def ct = RuleExecutionContext.getCurrentContext();		
		if( !ct.env.acctUtil ) ct.env.acctUtil = new ItemAccountUtil();
		billitem.account = ct.env.acctUtil.createAccountFact( [objid: acctid] );
		//billitem.sortorder = acct.sortorder;
	}


	public void addToFacts( def billitem ) {
		def facts = getFacts();
		//check if we can add the fact. Do not include if it already exists.
		if( !facts.find{ (it instanceof AbstractBillItem) && (it.hashCode() == billitem.hashCode()) } ) {
			facts << billitem;
		}
	}


}