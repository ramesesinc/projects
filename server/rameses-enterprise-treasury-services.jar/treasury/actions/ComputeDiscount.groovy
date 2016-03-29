package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;

public class ComputeDiscount implements RuleActionHandler {

	public void execute(def params, def drools) {
		def acct = params.account;
		def amt = NumberUtil.round(params.amount.doubleValue).doubleValue();	

		def ct = RuleExecutionContext.getCurrentContext();
		
		//lookup discount account
		def svc = EntityManagerUtil.lookup( "itemaccount" );
		def m = svc.find( [objid: acct.key] ).first();
		if( !m ) 
			throw new Exception("Error ComputeDiscount action. Account not found ");

		def bi = new BillItem();
		bi.account = new Account(m);
		bi.amtdue = amt;
		bi.amount = amt;

		//
	}
}