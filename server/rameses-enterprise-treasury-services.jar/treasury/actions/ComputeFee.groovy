package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;

public class ComputeFee implements RuleActionHandler {

	def res;	//resources

	public void execute(def params, def drools) {
		if( !res.billItemList ) 
			throw new Exception("ComputeFee error. Please define res.BillItemList");

		def acct = params.account;
		def amt = NumberUtil.round(params.amount.doubleValue).doubleValue();	

		//lookup account
		def svc = ServiceUtil.lookup( "RevenueItemAccountService" );
		def m = svc.findAccount( [objid: acct.key] );
		if( !m ) 
			throw new Exception("Error ComputeFee action. Account not found ");

		def bi = new BillItem();
		bi.account = new Account(m);
		bi.amtdue = amt;
		bi.amount = amt;

		res.billItemList.addItem( bi );
	}
}