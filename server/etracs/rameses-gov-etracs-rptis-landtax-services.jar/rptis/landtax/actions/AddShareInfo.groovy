package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;



public class AddShareInfo implements RuleActionHandler {
	def numSvc
	def facts
	def taxes 
	def getRevenueAccount
	def createShareFact
	def createTaxItem
	def lgutype

	public void execute(def params, def drools) {
		params.lgutype = lgutype 
		params.acct = getRevenueAccount(params)

		def amount = numSvc.roundA(params.expr.getDecimalValue(), 4)

		def share = facts.find{
			try {
				return it.lgutype == params.lgutype && 
				       it.revperiod == params.taxsummary.revperiod
			}
			catch(e){
				// ignore mismatch fact
			}
		}	
		if (! share){
			share = createShareFact(params)
			facts << share 
		}
		share[params.sharetype] += amount 
		def tax = taxes.find{it.item.objid 	== params.acct?.key}
		if (!tax){
			tax = createTaxItem(params)
			taxes << tax
		}
		if (params.sharetype.matches('.*disc'))
			tax.discount += amount 
		else
			tax.amount += amount 
	}
}	
