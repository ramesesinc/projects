package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;

/***
* Parameters:
*    billitem
****/
class BuildMonthYearEntries implements RuleActionHandler {

	public void execute(def params, def drools) {
		def fromyear = params.fromyear;
		def frommonth = params.frommonth;
		def toyear = params.toyear;
		def tomonth = params.tomonth;

		def ct = RuleExecutionContext.getCurrentContext();	

		//println fromyear + " " + frommonth + " ->" + toyear + " " + tomonth;
		def list = [];

		(((fromyear*12)+(frommonth-1))..((toyear*12)+(tomonth-1))).eachWithIndex { o, idx->
			def me = new MonthEntry( year: ((int)(o/12)), month: ((o% 12)+1)  );
			me.index = idx;
			list << me;
		}
		if(list) {
			list.first().first = true;
			list.last().last = true;
			ct.facts.addAll(list);
		}
	}

}
