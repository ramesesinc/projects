package loan.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import loan.facts.*;

public class CalculateTotalSchedulePaymentUpToCurrent implements RuleActionHandler {
	def NS;
	public void execute(def params, def drools) {
		//def LEDGER = params.ledger;
		def LEDGER = params.ledger;
		LEDGER.totalschedulepaymentuptocurrent = NS.round( params.amount.decimalValue );
	}

}