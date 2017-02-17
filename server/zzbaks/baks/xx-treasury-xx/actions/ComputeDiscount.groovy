package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;

public class ComputeDiscount implements RuleActionHandler {

	public void execute(def params, def drools) {
		def bi = params.billitem;
		def acct = params.account;
		def amt = NumberUtil.round(params.amount.doubleValue).doubleValue();	
		if(amt <= 0 ) return;

		if(acct ==null) 
			throw new Exception("Please specify an account in treasury.actions.ComputeInterest " + drools.rule.name);

		bi.discountAccount = new Account([objid:acct.key, title:acct.value]);
		bi.discount = amt;

	}
}