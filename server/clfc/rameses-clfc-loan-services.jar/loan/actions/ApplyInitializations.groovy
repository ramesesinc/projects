package loan.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import loan.facts.*;

public class ApplyInitializations implements RuleActionHandler {

	public void execute(def params, def drools) {
		def app = params.LOANAPP;
		def item = params.BILLITEM;
		item.amtdue = app.schedule;
		item.interest = app.interest;
	}

}