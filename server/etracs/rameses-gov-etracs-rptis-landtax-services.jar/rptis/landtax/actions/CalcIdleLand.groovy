package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;


public class CalcIdleLand implements RuleActionHandler {
	def numSvc

	public void execute(def params, def drools) {
		def rli = params.rptledgeritem

		if (params.computationtype.equalsIgnoreCase('tax'))
			rli.basicidle = numSvc.round(params.expr.getDecimalValue())
		else if (params.computationtype.equalsIgnoreCase('discount'))
			rli.basicidledisc = numSvc.round(params.expr.getDecimalValue())
		else if (params.computationtype.equalsIgnoreCase('penalty'))
			rli.basicidleint = numSvc.round(params.expr.getDecimalValue())
	}
}	
