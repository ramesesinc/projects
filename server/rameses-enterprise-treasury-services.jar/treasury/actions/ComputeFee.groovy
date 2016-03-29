package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;

public class ComputeFee implements RuleActionHandler {

	public void execute(def params, def drools) {
		
		def ct = RuleExecutionContext.getCurrentContext();
		if(!ct.result.billItemList ) {
			ct.result.billItemList = new BillItemList();
		}

		def acct = params.account;
		def amt = NumberUtil.round(params.amount.doubleValue).doubleValue();	

		//lookup account
		def svc = EntityManagerUtil.lookup( "itemaccount" );
		def m = svc.find( [objid: acct.key] ).first();
		if( !m ) 
			throw new Exception("Error ComputeFee action. Account not found ");

		def bi = new BillItem();
		bi.account = new Account(m);
		bi.amtdue = amt;
		bi.amount = amt;
		ct.result.billItemList.addItem( bi );
	}
}