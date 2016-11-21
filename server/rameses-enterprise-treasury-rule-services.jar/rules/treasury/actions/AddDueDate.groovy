package rules.treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import rules.treasury.facts.*;
import com.rameses.osiris3.common.*;

/***
* This adds a simple due date 
* Parameters:
*    date expression
****/
class AddDueDate implements RuleActionHandler {

	public void execute(def params, def drools) {
		def sdate = params.date.eval();
		def tag = params.tag;

		def ct = RuleExecutionContext.getCurrentContext();
		def facts = ct.facts;

		def dt = new DueDate(sdate );
		dt.tag = tag;
		facts << dt;
	}

}