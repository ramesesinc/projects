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

		double amt = 0;
		def _amt = params.amount.eval();
		if(_amt instanceof Number) {
			amt = _amt.doubleValue();
		}

		amt = NumberUtil.round(amt).doubleValue();	
		if(amt<=0) return;

		def ref = params.ref;
		def ttype = params.txntype;

		def ct = RuleExecutionContext.getCurrentContext();
		
		//lookup account
		def svc = EntityManagerUtil.lookup( "itemaccount" );
		def m = svc.find( [objid: acct.key] ).first();
		if( !m ) 
			throw new Exception("Error AddWaterBillItem action. Account not found ");

		//lookup txntype
		svc = EntityManagerUtil.lookup( "waterworks_txntype" );
		def txntype = svc.find( [objid: ttype.key] ).first();
		if( !txntype ) 
			throw new Exception("Error AddWaterBillItem action. Txntype not found ");

		def bi = new WaterBillItem();
		bi.account = new Account(m);
		bi.txntype = txntype.objid;
		bi.amount = amt;
		bi.year = ref.year;
		bi.month = ref.month;
		bi.priority = txntype.priority;
		bi.sortorder = (((ref.year * 12)+ref.month)*10) + bi.priority;
		bi.refid = ref.refid;
		bi.duedate = ref.duedate;
		ct.facts << bi;

	}
}