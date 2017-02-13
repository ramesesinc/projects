package market.actions;

import com.rameses.rules.common.*;
import market.facts.*;
import com.rameses.util.*;
import java.util.*;
import com.rameses.osiris3.common.*;
import treasury.facts.*;


public class ComputeInterest implements RuleActionHandler {
	
	public void execute(def params, def drools) {
		def bi = params.billitem;
		def acct = params.account;
		def amt = NumberUtil.round(params.amount.doubleValue).doubleValue();	
		if(amt <= 0 ) return;

		if(acct ==null) 
			throw new Exception("Please specify an account in market.actions.ComputeInterest " + drools.rule.name);

		bi.interestAccount = new Account([objid:acct.key, title:acct.value]);
		bi.interest = amt;
	}
}