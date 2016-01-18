package loan.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import loan.facts.*;

public class ApplyAbsentPenalty implements RuleActionHandler {
	def NS;
	public void execute(def params, def drools) {
		def BILLITEM = params.item;
		if (!BILLITEM.absentpenalty) BILLITEM.absentpenalty = 0;
		BILLITEM.absentpenalty = NS.round( params.amount.decimalValue );
	}

}