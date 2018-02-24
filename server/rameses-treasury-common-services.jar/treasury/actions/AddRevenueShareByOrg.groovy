package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;

class AddRevenueShareByOrg implements RuleActionHandler  {

	public void execute(def params, def drools) {

		def refitem = params.refitem;
		def payableacct = params.payableaccount;
		def amt = params.amount.decimalValue;
		def org = params.org;


		if( refitem ==null || payableacct==null || org == null )
			throw new Exception("Error in AddRevenueShareByOrg action. Please indicate a ref account,payable account and org. Check the rules");

		def ct = RuleExecutionContext.getCurrentContext();
		def rs = new RevenueShare();

		if ( refitem.account?.objid!=null && refitem.account?.objid!='null' ) {
			rs.refitem = ct.env.acctUtil.createAccountFact( [objid: refitem.account.objid] );
		} 
		if ( payableacct?.key != null && payableacct?.key!='null' ) {
			rs.payableaccount = ct.env.acctUtil.createAccountFactByOrg( payableacct.key, org.orgid ); 
			if ( !rs.payableaccount ) throw new Exception('There is no payable account with parent '+ payableacct.value + ' org '+ org.orgid);
		}
		rs.amount  = amt;

		if (!ct.result.sharing){
			ct.result.sharing = []
		}
		ct.facts << rs;
		ct.result.sharing << rs 
	}

}