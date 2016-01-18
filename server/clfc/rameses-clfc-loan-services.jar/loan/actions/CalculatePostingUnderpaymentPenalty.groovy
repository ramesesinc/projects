package loan.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import loan.facts.*;

public class CalculatePostingUnderpaymentPenalty implements RuleActionHandler {
	def NS;
	public void execute(def params, def drools) {
		//def LEDGER = params.ledger;
		def POSTINGITEM = params.postingitem;
		POSTINGITEM.underpaymentpenalty = NS.round( params.amount.decimalValue );
	}

}