package waterworks.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;

public class AddRepeatingFee implements RuleActionHandler {

	public void execute(def params, def drools) {
		
		def ct = RuleExecutionContext.getCurrentContext();
		if(!ct.result.otherFees ) {
			ct.result.otherFees = [];
		}

		def acct = params.account;
		def amt = NumberUtil.round(params.amount.doubleValue).doubleValue();	

		def rem = params.remarks;
		String remarks = "";
		if( rem !=null ) {
			remarks = params.remarks.getStringValue();	
		}

		//lookup account
		def svc = EntityManagerUtil.lookup( "itemaccount" );
		def m = svc.find( [objid: acct.key] ).first();
		if( !m ) 
			throw new Exception("Error AddRepeatingFee action. Account not found ");

		def bi = new BillItem();
		bi.account = new Account(m);
		bi.amtdue = amt;
		bi.amount = amt;
		bi.remarks = remarks;
		ct.result.otherFees << bi ;
	}
}