package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;


public class CalcSEF implements RuleActionHandler {
	def numSvc

	public void execute(def params, def drools) {
		def rli = params.rptledgeritem

		if (params.computationtype.equalsIgnoreCase('tax') ){
			rli.sef = numSvc.round(params.expr.getDecimalValue())
		}
		else if (params.computationtype.equalsIgnoreCase('discount')){
			rli.sefdisc = numSvc.round(params.expr.getDecimalValue())
		}
		else if (params.computationtype.equalsIgnoreCase('penalty')){
			rli.sefint = numSvc.round(params.expr.getDecimalValue())
		}
	}
}