package market.actions;

import com.rameses.rules.common.*;
import market.facts.*;
import com.rameses.util.*;

public class RemoveBillItem implements RuleActionHandler {

	def request;

	public void execute(def bi, def drools) {
		request.facts.remove( bi );
		drools.retract(bi);
	}
}