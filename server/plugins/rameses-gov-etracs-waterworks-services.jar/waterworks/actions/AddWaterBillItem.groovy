package waterworks.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;
import waterworks.facts.*;

public class AddWaterBillItem implements RuleActionHandler {

	public void execute(def params, def drools) {
		def acct = params.account;
		def amt = NumberUtil.round(params.amount.doubleValue).doubleValue();	
		def ref = params.ref;
		def priority = params.priority.toInteger();
		def txntype = params.txntype;

		def ct = RuleExecutionContext.getCurrentContext();
		
		//lookup account
		def svc = EntityManagerUtil.lookup( "itemaccount" );
		def m = svc.find( [objid: acct.key] ).first();
		if( !m ) 
			throw new Exception("Error ComputeFee action. Account not found ");

		def bi = new WaterBillItem();
		bi.account = new Account(m);
		bi.amount = amt;
		bi.year = ref.year;
		bi.month = ref.month;
		bi.priority = priority;
		bi.sortorder = (((ref.year * 12)+ref.month)*10) + priority;
		bi.refid = ref.refid;
		bi.txntype = txntype;
		bi.duedate = ref.duedate;
		ct.facts << bi;
	}
}