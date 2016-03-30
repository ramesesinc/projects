package mt.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import com.rameses.osiris3.script.*;

public class CalculateCharge implements RuleActionHandler {

	def soutfact;

	public void execute( params, drools ) {
		double charge = NumberUtil.round(params.amount.doubleValue).doubleValue();
		soutfact.charge = charge; 
	} 
}