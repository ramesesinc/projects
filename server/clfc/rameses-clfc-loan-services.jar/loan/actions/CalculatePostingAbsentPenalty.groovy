package loan.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import loan.facts.*;

public class CalculatePostingAbsentPenalty implements RuleActionHandler {
	def NS;
	public void execute(def params, def drools) {
		//def LEDGER = params.ledger;
		def POSTINGITEM = params.postingitem;
		POSTINGITEM.absentpenalty = NS.round( params.amount.decimalValue );
	}

}