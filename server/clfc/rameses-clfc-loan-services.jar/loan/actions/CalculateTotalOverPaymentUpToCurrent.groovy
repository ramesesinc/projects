package loan.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import loan.facts.*;

public class CalculateTotalOverPaymentUpToCurrent implements RuleActionHandler {
	def NS;
	public void execute(def params, def drools) {
		//def LEDGER = params.ledger;
		def LEDGER = params.ledger;
		LEDGER.totaloverpaymentuptocurrent = NS.round( params.amount.decimalValue );
	}

}