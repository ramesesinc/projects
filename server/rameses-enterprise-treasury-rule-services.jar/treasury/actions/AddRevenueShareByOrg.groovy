package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;

class AddRevenueShareByOrg implements RuleActionHandler  {

	public void execute(def params, def drools) {

		def refacct = params.refaccount;
		def payableacct = params.payableparentaccount;
		def share = params.share.intValue();
		def amt = params.amount.decimalValue;
		def org = params.org;

		if( refacct ==null && payableacct==null)
			throw new Exception("Error in AddRevenueShareByOrg action. Please indicate a ref account or a payable parent account. Check the rules");

		def ct = RuleExecutionContext.getCurrentContext();
		def rs = new RevenueShare();

		if ( refacct?.key!=null && refacct?.key!='null' ) {
			rs.refaccount = ct.env.acctUtil.createAccountFact( [objid: refacct.key] );
		} 
		if ( payableacct?.key != null && payableacct?.key!='null' ) {
			rs.payableaccount = ct.env.acctUtil.createAccountFactByOrg( payableacct.key, org.orgid ); 
			if ( !rs.payableaccount ) throw new Exception('There is no payable account with parent '+ payableacct.value + ' org '+ org.orgid);
		}
		rs.amount  = amt;
		rs.share = share;

		if (!ct.result.sharing){
			ct.result.sharing = []
		}
		ct.facts << rs;
		ct.result.sharing << rs 
	}

}