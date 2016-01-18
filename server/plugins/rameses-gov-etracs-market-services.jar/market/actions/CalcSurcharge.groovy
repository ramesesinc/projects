package market.actions;

import com.rameses.rules.common.*;
import market.facts.*;
import com.rameses.util.*;

public class CalcSurcharge implements RuleActionHandler {
	def request;
	public void execute(def params, def drools) {
		def tf = params.billitem;
		def amt = params.amount.doubleValue;
		tf.surcharge = NumberUtil.round(amt).doubleValue();
	}
}
