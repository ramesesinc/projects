package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.osiris3.common.*;
import treasury.facts.*;


public class SetAccounts implements RuleActionHandler {

	public void execute(def params, def drools) {
		def acct = params.account;
		def sur_acct = params.surchargeaccount;
		def int_acct = params.interestaccount;

		if(!acct) throw new Exception("Account must not be null");
		if(!sur_acct) throw new Exception("Surcharge account must not be null");
		if(!int_acct) throw new Exception("Interest account must not be null");

		def em = EntityManagerUtil.lookup("itemaccount");

		ct.result.account = em.find( [objid: acct.key] ).first();
		if(!ct.result.account) {
			throw new Exception("SetAccounts rule error. Account not found ");
		}

		ct.result.surchargeaccount = em.find( [objid: sur_acct.key] ).first();
		if(!ct.result.surchargeaccount) {
			throw new Exception("SetAccounts rule error. Surcharge account not found ");
		}

		ct.result.interestaccount = em.find( [objid: int_acct.key] ).first();
		if(!ct.result.interestaccount) {
			throw new Exception("SetAccounts rule error. Interest account not found ");
		}

	}
}