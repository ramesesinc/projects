package loan.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import loan.facts.*;

public class CalcInterest implements RuleActionHandler {
	def NS;
	public void execute(def params, def drools) {
		def APP = params.app;
		APP.interest = roundOff( NS.round(params.amount.decimalValue) );
	}

	private def roundOff( number ) {		
		def amount = number + '';
		def result = 0;
		def ld = Integer.parseInt(amount[amount.length()-1]);
		if(ld > 0 && ld < 5) {
			result = new BigDecimal(amount.substring(0, amount.length()-1)+'5');
			result = result.setScale(2);
		} else if(ld > 5) {
		    def a = '0.0'+(10 - ld)
		    result = new BigDecimal(amount).setScale(2)
		    result = result.add(new BigDecimal(a)).setScale(2);
		} else {
			result = new BigDecimal(amount).setScale(2); 
		} 
		return result;
	}

}