package market.actions;

import com.rameses.rules.common.*;
import market.facts.*;

public class CalcRate implements RuleActionHandler {
	def request;
	public void execute(def params, def drools) {
		def mu = params.marketunit;
		mu.rate = params.rate.doubleValue;
	}
}