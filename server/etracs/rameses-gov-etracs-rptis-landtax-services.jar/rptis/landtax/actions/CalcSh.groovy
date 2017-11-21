package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;

public class CalcSh implements RuleActionHandler {
	def numSvc

	public void execute(def params, def drools) {
		def rli = params.rptledgeritem

		if (params.computationtype.equalsIgnoreCase('tax')){
			rli.sh = numSvc.round(params.expr.getDecimalValue())
		}
		else if (params.computationtype.equalsIgnoreCase('discount')){
			rli.shdisc = numSvc.round(params.expr.getDecimalValue())
		}
		else if (params.computationtype.equalsIgnoreCase('penalty')){
			rli.shint = numSvc.round(params.expr.getDecimalValue())
		}
	}
}	