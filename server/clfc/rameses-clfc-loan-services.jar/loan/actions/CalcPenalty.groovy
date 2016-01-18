package loan.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import loan.facts.*;

public class CalcPenalty implements RuleActionHandler {
	def NS;
	public void execute(def params, def drools) {
		def BILLITEM = params.item;
		BILLITEM.penalty = NS.round( params.amount.decimalValue );
	}

}