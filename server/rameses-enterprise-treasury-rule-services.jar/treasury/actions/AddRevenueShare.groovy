package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;



class AddRevenueShare implements RuleActionHandler  {

	public void execute(def params, def drools) {

		def refacct = params.refaccount;
		def payableacct = params.payableaccount;
		def amt = params.amount.decimalValue;

		def ct = RuleExecutionContext.getCurrentContext();
		def rs = new RevenueShare();
		rs.refaccount = ct.env.acctUtil.createAccountFact( [objid: refacct.key] );
		rs.payableaccount = ct.env.acctUtil.createAccountFact( [objid: payableacct.key] );
		rs.amount  = amt;

		if (!ct.result.shareitems){
			ct.result.shareitems = []
		}

		ct.result.shareitems << rs 
	}

}