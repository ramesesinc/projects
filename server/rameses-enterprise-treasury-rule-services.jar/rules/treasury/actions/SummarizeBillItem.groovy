package rules.treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import rules.treasury.facts.*;
import com.rameses.osiris3.common.*;

/***
* Parameters:
*    billitem
*    type ( SUM, MAX, MIN )
****/
class SummarizeBillItem implements RuleActionHandler {

	public void execute(def params, def drools) {
		def item = params.billitem;
		if(!item)
			throw new Exception("rules.treasury.actions.SummarizeBillItem");
		def remarks =  params.remarks?.eval();	

		def ct = RuleExecutionContext.getCurrentContext();
	}

}