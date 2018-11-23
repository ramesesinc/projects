package waterworks.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;

public class ComputeConsumption implements RuleActionHandler {

	public void execute(def params, def drools) {
		if(params.amount == null )
			throw new Exception("ComputeConsumption action error. amount must not be null");
		def amt = NumberUtil.round(params.amount.doubleValue).doubleValue();	
		def cw = params.ref;
		if( cw == null ) throw new Exception("Please add a ref parameter in ComputeConsumption action for "+drools.rule.name )
		if(!cw.updated) {
			cw.amount = amt;
			cw.updated = true;
		}
	}
}

