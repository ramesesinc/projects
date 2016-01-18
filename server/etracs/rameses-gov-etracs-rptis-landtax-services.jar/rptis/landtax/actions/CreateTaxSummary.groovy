package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;


public class CreateTaxSummary implements RuleActionHandler {
	def items 
	def numSvc
	def facts
	def createTaxSummaryFact

	public void execute(def params, def drools) {
		// set revenue period 
		params.rptledgeritem.revperiod = params.revperiod
		def item = items.find{ it.objid == params.rptledgeritem.objid }
		item.revperiod = params.revperiod


		def var = facts.find{
			try {
				return it.objid == params.var.key
			}
			catch(e){
				// ignore mismatch fact
			}
		}	

		if (! var){
			var = createTaxSummaryFact(params)
			facts << var 
		}

		var.basic  += params.rptledgeritem.basic
		var.basicint += params.rptledgeritem.basicint
		var.basicdisc += params.rptledgeritem.basicdisc
		var.basicidle += params.rptledgeritem.basicidle
		var.basicidledisc += params.rptledgeritem.basicidledisc
		var.basicidleint += params.rptledgeritem.basicidleint
		var.sef += params.rptledgeritem.sef
		var.sefint += params.rptledgeritem.sefint
		var.sefdisc += params.rptledgeritem.sefdisc
		var.firecode += params.rptledgeritem.firecode
	}
}	

