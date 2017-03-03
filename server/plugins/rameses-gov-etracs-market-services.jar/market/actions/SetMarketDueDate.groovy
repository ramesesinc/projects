package market.actions;

import com.rameses.rules.common.*;
import market.facts.*;
import com.rameses.util.*;
import java.util.*;
import com.rameses.osiris3.common.*;
import treasury.facts.*;

import market.utils.*;

public class SetMarketDueDate implements RuleActionHandler {
	
	public void execute(def params, def drools) {
		def billitem = params.billitem;
		def duedate = params.duedate.eval();

		//check if there are overrides in the database for the dates:
		def ct = RuleExecutionContext.getCurrentContext();

		if( !ct.env.dueDateLookup ) ct.env.dueDateLookup = new MarketDueDateLookup();
		def _duedate = ct.env.dueDateLookup.lookup( billitem.year, billitem.month );
		if(_duedate) duedate = _duedate;

		billitem.duedate = duedate;	
	}
}