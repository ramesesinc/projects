package loan.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import loan.facts.*;

public class CalculateBillingBalance implements RuleActionHandler {
	def NS;
	public void execute(def params, def drools) {
		//def LEDGER = params.ledger;
		def BILLITEM = params.billitem;
		BILLITEM.balance = roundOff(NS.round( params.amount.decimalValue ));
	}

	private def roundOff( amt ) {
		def amount = new BigDecimal(amt + "").setScale(0, BigDecimal.ROUND_HALF_UP)
		amount += '';

		def result = 0;
		def ld = Integer.parseInt(amount[amount.length()-1]);
		if(ld > 0 && ld < 5) {
		    result = new BigDecimal(amount.substring(0, amount.length()-1)+'5');
		    result = result.setScale(2);
		} else if(ld > 5) {
		    def a = (10 - ld)
		    result = new BigDecimal(amount).add(new BigDecimal(a)).setScale(0);
		} else {
		    result = new BigDecimal(amount).setScale(0); 
		} 
		return result;
	}

}