package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import com.rameses.osiris3.common.*;
import treasury.facts.*;

public class ComputeSurcharge implements RuleActionHandler {

	def res;

	public void execute(def params, def drools) {
		def billItem = params.billitem;
		def amt = NumberUtil.round(params.amount.doubleValue).doubleValue();
		def acct = params.account;

		//lookup account
		def svc = ServiceUtil.lookup( "RevenueItemAccountService" );
		def m = svc.findAccount( [objid: acct.key] );
		if( !m ) 
			throw new Exception("Error CalcSurcharge action. Surcharge account not found ");
		billItem.surchargeAccount = new Account(m);
		billItem.surcharge = amt;
	}
}
