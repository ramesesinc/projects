package loan.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import loan.facts.*;

public class CalculateLackingPenalty implements RuleActionHandler {
	def NS, LS;
	public void execute(def params, def drools) {
		//def LEDGER = params.ledger;
		def LEDGER = params.ledger;
		def amt = NS.round( params.amount.decimalValue );
		LEDGER.lackingpenalty = LS.roundOffAmount(amt);
	}

}