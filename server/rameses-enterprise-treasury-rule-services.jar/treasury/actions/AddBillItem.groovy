package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;

public class AddBillItem implements RuleActionHandler {

	public void execute(def params, def drools) {
		def amt = NumberUtil.round(params.amount.doubleValue).doubleValue();
		def item = params.account;
		if(!item)
			throw new Exception("treasury.actions.AddBillItem. Account is required");
		def remarks =  params.remarks?.eval();	
		def txntype = params.txntype;

		def ct = RuleExecutionContext.getCurrentContext();
		
		//lookup txntype
		def bi = new BillItem();
		bi.account = new Account([objid:item.key, code: item.value]);
		bi.amount = amt;
		bi.sortorder = 0;
		bi.remarks = remarks;
		bi.txntype = txntype;
		ct.facts << bi;

	}
}