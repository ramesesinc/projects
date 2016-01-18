package loan.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import loan.facts.*;

public class SegregateLedger implements RuleActionHandler {
	
	public void execute(def params, def drools) {
		//def APP = params.app;
		//APP.absentpenalty = NS.round( params.amount.decimalValue );
		//println 'params ' + params;
		def LEDGER = params.ledger;
		def SEGREGATIONTYPE = params.segregationtype;
		if (!LEDGER.segregationtypes) LEDGER.segregationtypes = [];
		//println 'segregation type ' + SEGREGATIONTYPE.toMap();
		LEDGER.segregationtypes.add(SEGREGATIONTYPE.toMap());
	}

}