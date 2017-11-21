package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;

public class CalcFireCode implements RuleActionHandler {
	def numSvc

	public void execute(def params, def drools) {
		def rli = params.rptledgeritem
		rli.firecode = numSvc.round(params.expr.getDecimalValue())
	}
}	