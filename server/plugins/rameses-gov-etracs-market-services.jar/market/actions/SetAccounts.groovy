package market.actions;

import com.rameses.rules.common.*;
import market.facts.*;

public class SetAccounts implements RuleActionHandler {
	def request;
	def BA;

	public void execute(def params, def drools) {
		def acct = params.account;
		def sur_acct = params.surchargeaccount;
		def int_acct = params.interestaccount;

		if(!acct) throw new Exception("Account must not be null");
		if(!sur_acct) throw new Exception("Surcharge account must not be null");
		if(!int_acct) throw new Exception("Interest account must not be null");

		request.account = BA.findAccount( [objid: acct.key] );
		if(!request.account) {
			throw new Exception("SetAccounts rule error. Account not found ");
		}

		request.surchargeaccount = BA.findAccount( [objid: sur_acct.key] );
		if(!request.surchargeaccount) {
			throw new Exception("SetAccounts rule error. Surcharge account not found ");
		}

		request.interestaccount = BA.findAccount( [objid: int_acct.key] );
		if(!request.interestaccount) {
			throw new Exception("SetAccounts rule error. Interest account not found ");
		}

	}
}