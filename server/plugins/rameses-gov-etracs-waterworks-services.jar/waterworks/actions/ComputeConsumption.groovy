package waterworks.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;

public class ComputeConsumption implements RuleActionHandler {

	public void execute(def params, def drools) {
		def amt = NumberUtil.round(params.amount.doubleValue).doubleValue();	
		def cw = params.ref;
		cw.amount = amt;
	}
}