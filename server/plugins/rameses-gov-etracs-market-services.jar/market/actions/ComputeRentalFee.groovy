package market.actions;

import com.rameses.rules.common.*;
import market.facts.*;
import com.rameses.util.*;
import java.util.*;
import com.rameses.osiris3.common.*;
import treasury.facts.*;

//drools is class org.drools.base.DefaultKnowledgeHelper
public class ComputeRentalFee implements RuleActionHandler {

	public void execute(def params, def drools) {
		def bi = params.billitem;
		def acct = params.account;
		def amt = NumberUtil.round(params.amount.doubleValue).doubleValue();	

		if(acct ==null) 
			throw new Exception("Please specify an account in market.actions.ComputeRentalFee " + drools.rule.name);

		bi.account = new Account( [objid:acct.key, title:acct.value] );
		bi.amount = amt;
	}
}