package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;

class AddRevenueShareByOrg implements RuleActionHandler  {

	public void execute(def params, def drools) {

		def refitem = params.refitem;
		def payableaccount = params.payableaccount;
		def amt = params.amount.decimalValue;
		def org = params.org;

		if( refitem ==null || payableaccount ==null || org == null )
			throw new Exception("Error in AddRevenueShare action. Please indicate a ref item, payableaccount and org. Check the rules");

			
		def ct = RuleExecutionContext.getCurrentContext();
		def rs = new RevenueShare();
		
		if(refitem.account.objid!=null && refitem.account?.objid!='null') {
			rs.refitem = ct.env.acctUtil.createAccountFact( [objid: refitem.account.objid] );
		};
		
		if ( payableaccount?.key != null && payableaccount?.key!='null' ) {
			rs.payableitem = ct.env.acctUtil.createAccountFactByOrg( payableaccount.key, org.orgid ); 
			if ( !rs.payableitem ) throw new Exception('There is no payable account with parent '+ payableaccount.value + ' org '+ org.orgid);
		}

		rs.amount  = amt;

		if (!ct.result.sharing) {
			ct.result.sharing = []
		}
		ct.facts << rs;
	}

}