package waterworks.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;
import waterworks.facts.*;

public class AddWaterBillItem implements RuleActionHandler {

	public void execute(def params, def drools) {
		def year = params.year;
		def month = params.month;
		def ttype = params.txntype;
		def refid = params.refid;

		double amt = 0;
		def _amt = params.amount.eval();
		if(_amt instanceof Number) {
			amt = _amt.doubleValue();
		}

		amt = NumberUtil.round(amt).doubleValue();	

		def ct = RuleExecutionContext.getCurrentContext();
		
		//lookup txntype
		def svc = EntityManagerUtil.lookup( "waterworks_txntype" );
		def txntype = svc.find( [objid: ttype.key] ).first(); 
		if( !txntype ) 
			throw new Exception("Error AddWaterBillItem action. Txntype not found ");

		if(!txntype.item)
			throw new Exception("Error AddWaterBillItem action. Please define an item account  in txntype " + txntype.objid);

		def bi = new WaterBillItem();
		bi.account = new Account(txntype.item);
		bi.txntype = txntype.objid;
		bi.amount = amt;
		bi.year = year;
		bi.month = month;
		bi.priority = txntype.priority;
		bi.sortorder = (((year * 12)+month)*10) + bi.priority;
		bi.reftype = txntype.ledgertype;
		bi.refid = refid;
		ct.facts << bi;

	}
}