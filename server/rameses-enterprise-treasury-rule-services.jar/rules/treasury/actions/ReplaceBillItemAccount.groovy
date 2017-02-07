package rules.treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import rules.treasury.facts.*;
import com.rameses.osiris3.common.*;

/***
* Parameters:
*    billitem
*    account
****/
class ReplaceBillItemAccount implements RuleActionHandler {

	public void execute(def params, def drools) {
		def billitem = params.billitem;
		def acct = params.account;
		billitem.account = new Account( objid: acct.key , title: acct.value );
	}

}