package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;

public class InitBilling implements RuleActionHandler {

	public void execute(def billitem, def drools) {
		throw new Exception("InitBilling Error. Please check rule action init billing")	
		//add here if there are payments made. if payment already made, do not add in list.
		//check first if there is an acctid
		if(request.acctid) {
			if(request.payments==null) {
				def m = [:];
				m.acctid = request.acctid;
				m.fromdate = billitem.fromdate;
				request.payments = PS.getPaymentDetailsByDate(m).collect{ [objid:it.objid, month:it.imonth, year:it.iyear, amtpaid:it.amtpaid] };
			}		
		}
		drools.insert( billitem );
	}
}