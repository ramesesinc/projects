package loan.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import loan.facts.*;

public class CalcUnderpaymentPenalty implements RuleActionHandler {
	def NS;
	public void execute(def params, def drools) {
		def BILLITEM = params.item;
		if (!BILLITEM.underpytpenalty) BILLITEM.underpytpenalty = 0;
		println 'BILL ITEM ' + BILLITEM.toMap();
		BILLITEM.underpytpenalty = NS.round( params.amount.decimalValue );
		println 'BILL ITEM ' + BILLITEM.toMap();
		//println 'due date ' + BILLITEM.duedate;
		//println 'under payment penalty ' + BILLITEM.underpytpenalty;
	}

}