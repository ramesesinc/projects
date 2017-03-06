package ovs.actions;

import com.rameses.rules.common.*;

class ComputeViolationFee implements RuleActionHandler {
	
	public void execute(def params, def drools) {
		def violation = params.violation;
		violation.amount = params.amount.doubleValue;		
	}		

}