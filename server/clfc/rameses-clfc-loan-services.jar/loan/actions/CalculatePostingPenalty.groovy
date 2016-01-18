package loan.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import loan.facts.*;

public class CalculatePostingPenalty implements RuleActionHandler {
	def NS, LS;
	public void execute(def params, def drools) {
		//def LEDGER = params.ledger;
		def POSTINGITEM = params.postingitem;
		def amt = NS.round( params.amount.decimalValue );
		POSTINGITEM.penalty = LS.roundOffAmount(amt);
	}

}