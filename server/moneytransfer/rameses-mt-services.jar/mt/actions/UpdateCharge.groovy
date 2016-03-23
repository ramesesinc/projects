package mt.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;

public class UpdateCharge implements RuleActionHandler {

	def soutfact; 

	public void execute( params, drools ) { 
		double newcharge = NumberUtil.round(params.amount.doubleValue).doubleValue();
		soutfact.charge = newcharge; 
	} 
}