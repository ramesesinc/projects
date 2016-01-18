package loan.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import loan.facts.*;

public class CalcSchedule implements RuleActionHandler {
	def NS;
	public void execute(def params, def drools) {
		def APP = params.app;
		APP.schedule = NS.round( params.amount.decimalValue );
	}

}