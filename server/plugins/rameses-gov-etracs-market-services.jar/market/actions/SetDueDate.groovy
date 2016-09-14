package market.actions;

import com.rameses.rules.common.*;
import market.facts.*;
import com.rameses.util.*;

public class SetDueDate implements RuleActionHandler {
	
	public void execute(def params, def drools) {
		def bi = params.billitem;
		def dt = params.duedate.eval();
		bi.duedate = dt;
	}

}
