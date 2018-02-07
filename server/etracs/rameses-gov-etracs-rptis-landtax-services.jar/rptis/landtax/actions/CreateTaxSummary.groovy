package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;


public class CreateTaxSummary implements RuleActionHandler {
	def numSvc
	def facts

	public void execute(def params, def drools) {
		// set revenue period 
		def rli = params.rptledgeritem
		rli.revperiod = params.revperiod

		def var = facts.findAll{it instanceof RPTLedgerTaxSummaryFact}.find{it.objid == params.var.key}

		if (! var){
			var = new RPTLedgerTaxSummaryFact(params)
			facts << var 
		}

		var.basic  += rli.basic
		var.basicint += rli.basicint
		var.basicdisc += rli.basicdisc
		var.basicidle += rli.basicidle
		var.basicidledisc += rli.basicidledisc
		var.basicidleint += rli.basicidleint
		var.sef += rli.sef
		var.sefint += rli.sefint
		var.sefdisc += rli.sefdisc
		var.firecode += rli.firecode
		var.sh += rli.sh
		var.shint += rli.shint
		var.shdisc += rli.shdisc
	}
}	

