package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;

import com.rameses.osiris3.common.*;

public class ComputeDiscount implements RuleActionHandler {

	def res;	//resources

	public void execute(def params, def drools) {
		def acct = params.account;
		def amt = NumberUtil.round(params.amount.doubleValue).doubleValue();	

		//lookup discount account
		def svc = ServiceUtil.lookup( "RevenueItemAccountService" );
		def m = svc.findAccount( [objid: acct.key] );
		if( !m ) 
			throw new Exception("Error CalcFee action. Account not found ");

		def bi = new BillItem();
		bi.account = new Account(m);
		bi.amtdue = amt;
		bi.amount = amt;

		//
	}
}