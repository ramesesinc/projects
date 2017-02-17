package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;

public class RemoveBillItem implements RuleActionHandler {

	public void execute(def bi, def drools) {
		def ct = RuleExecutionContext.getCurrentContext();
		ct.facts.remove( bi );
		drools.retract(bi);
	}
	
}