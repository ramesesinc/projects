package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import com.rameses.osiris3.common.*;
import treasury.facts.*;

public class ComputeSurcharge implements RuleActionHandler {
	
	public void execute(def params, def drools) {
		def bi = params.billitem;
		def acct = params.account;
		def amt = NumberUtil.round(params.amount.doubleValue).doubleValue();	

		if(amt <= 0 ) return;

		if(acct ==null) 
			throw new Exception("Please specify an account in vehicle.actions.ComputeSurcharge " + drools.rule.name);

		bi.surchargeAccount = new Account([objid:acct.key, title:acct.value]);
		bi.surcharge = amt;	
	}
}
