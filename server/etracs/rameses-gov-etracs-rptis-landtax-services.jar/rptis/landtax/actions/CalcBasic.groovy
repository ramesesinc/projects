package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;

public class CalcBasic implements RuleActionHandler {
	def numSvc

	public void execute(def params, def drools) {
		def rli = params.rptledgeritem

		if (params.computationtype.equalsIgnoreCase('tax')){
			rli.basic = numSvc.round(params.expr.getDecimalValue())
		}
		else if (params.computationtype.equalsIgnoreCase('discount')){
			rli.basicdisc = numSvc.round(params.expr.getDecimalValue())
		}
		else if (params.computationtype.equalsIgnoreCase('penalty')){
			rli.basicint = numSvc.round(params.expr.getDecimalValue())
		}
	}
}	