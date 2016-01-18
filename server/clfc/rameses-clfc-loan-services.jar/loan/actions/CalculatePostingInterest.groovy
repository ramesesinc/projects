package loan.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import loan.facts.*;

public class CalculatePostingInterest implements RuleActionHandler {
	def NS;
	public void execute(def params, def drools) {
		//def LEDGER = params.ledger;
		def POSTINGITEM = params.postingitem;
		POSTINGITEM.interest = NS.round( params.amount.decimalValue );
		//println POSTINGITEM.toMap();
	}

}