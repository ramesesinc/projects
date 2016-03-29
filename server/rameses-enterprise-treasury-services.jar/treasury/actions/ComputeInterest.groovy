package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import com.rameses.osiris3.common.*;
import treasury.facts.*;

public class ComputeInterest implements RuleActionHandler {
	
	public void execute(def params, def drools) {
		def ct = RuleExecutionContext.getCurrentContext();

		def billItem = params.billitem;
		def amt = NumberUtil.round(params.amount.doubleValue).doubleValue();
		def acct = params.account;

		//lookup account
		def svc = EntityManagerUtil.lookup( "itemaccount" );
		def m = svc.find( [objid: acct.key] ).first();
		if( !m ) 
			throw new Exception("Error CalcInterest action. Interest account not found ");
			
		billItem.interestAccount = new Account(m);
		billItem.interest = amt;
	}

}