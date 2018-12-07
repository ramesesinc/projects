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


		if( refitem ==null && payableaccount ==null)
			throw new Exception("Error in AddRevenueShare action. Please indicate a ref item and payable item. Check the rules");

			
		def ct = RuleExecutionContext.getCurrentContext();
		def rs = new RevenueShare();
		

		if(refitem.account.objid!=null && refitem.account?.objid!='null') {
			rs.refitem = ct.env.acctUtil.createAccountFact( [objid: refitem.account.objid] );
		};
		
		if(payableaccount?.key!=null && payableaccount?.key!='null') {
			rs.payableitem = ct.env.acctUtil.createAccountFact( [objid: payableaccount.key] );
		}

		rs.amount  = amt;
		if (!ct.result.sharing) {
			ct.result.sharing = []
		}
		ct.facts << rs;
	}

}