package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;



public class AddFireCodeAccount implements RuleActionHandler {
	def numSvc
	def items 
	def taxes 
	def createTaxItem

	public void execute(def params, def drools) {
		params.sharetype = 'firecode'
		def amount = numSvc.round(params.expr.getDecimalValue())

		if (amount > 0.0){
			def tax = taxes.find{it.item.objid 	== params.acct?.key}
			if (!tax){
				tax = createTaxItem(params)
				taxes << tax
			}
			tax.amount += amount 
		}
	}
}	

