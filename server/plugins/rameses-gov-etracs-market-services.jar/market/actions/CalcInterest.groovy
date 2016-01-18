package market.actions;

import com.rameses.rules.common.*;
import market.facts.*;
import com.rameses.util.*;

public class CalcInterest implements RuleActionHandler {
	def request;
	public void execute(def params, def drools) {
		def tf = params.billitem;
		def amt = params.amount.doubleValue;
		tf.interest = NumberUtil.round(amt).doubleValue();
	}
}