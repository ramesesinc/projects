package market.actions;

import com.rameses.rules.common.*;
import market.facts.*;
import com.rameses.util.*;
import java.util.*;

public class AddBillItem implements RuleActionHandler {

	def request;

	public void execute(def billitem, def drools) {
		//check first if there is an acctid
		if(request.payments) {
			def h = request.payments.find{ it.month == billitem.month && it.year == billitem.year };
			if( h ) {
				billitem.amtdue = billitem.amtdue - h.amtpaid;
			}	
		}		
		if(billitem.amtdue > 0 ) {
			request.facts.add( billitem );
			drools.insert( billitem );
		}
	}
}