package loan.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import loan.facts.*;

public class AddBillItemLastPayment implements RuleActionHandler {
	
	def facts;
	
	public void execute(def params, def drools) {
		def app = params.LOANAPP;
		int c = app.counter;
		def b = new LoanBillingItem();
		b.itemno = c+1;
		b.duedate = DateUtil.add( app.startdate, c+"d");
		facts << b;
		app.counter += 1;
		drools.update( app );
	}

}