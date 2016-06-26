package waterworks.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;

public class SetBillingPeriod implements RuleActionHandler {

	public void execute(def params, def drools) {
		def ct = RuleExecutionContext.getCurrentContext();
		def period = params.period.eval();

		if(!period.fromperiod) 
			throw new Exception("fromperiod is required in SetBillingPeriod rule result");
		if(!period.toperiod) 
			throw new Exception("toperiod is required in SetBillingPeriod rule result");
		if(!period.readingdate) 
			throw new Exception("readingdate is required in SetBillingPeriod rule result");
		if(!period.billdate) 
			throw new Exception("billdate is required in SetBillingPeriod rule result");
		if(!period.duedate) 
			throw new Exception("duedate is required in SetBillingPeriod rule result");
		if(!period.disconnectiondate)
			throw new Exception("disconnectiondate is required in SetBillingPeriod rule result");

		ct.result.period = period;

	}
	
}