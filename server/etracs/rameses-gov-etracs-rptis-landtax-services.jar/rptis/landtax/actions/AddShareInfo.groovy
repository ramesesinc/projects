package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;



public class AddShareInfo implements RuleActionHandler {
	def numSvc
	def facts
	def taxes 
	def getRevenueAccount
	def createTaxItem
	def lgutype

	public void execute(def params, def drools) {
		def amount = numSvc.roundA(params.expr.getDecimalValue(), 4)
		if (amount <= 0 ) return 

		params.lgutype = lgutype 
		params.acct = getRevenueAccount(params)

		def share = facts.findAll{f -> f instanceof rptis.landtax.facts.ShareInfoFact}.find{
			return  it.lgutype == params.lgutype && 
					it.revperiod == params.taxsummary.revperiod &&
				    it.rptledger.objid == params.taxsummary.rptledger.objid 
		}

		if (!share){
			share = new rptis.landtax.facts.ShareInfoFact(params)
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

	def createTaxItem(params){
		return [
			objid 		: 'BI' + new java.rmi.server.UID(),
			rptledgerid : params.taxsummary.rptledger.objid,
			revperiod	: params.taxsummary.revperiod,
			lgutype	    : params.lgutype,
			sharetype 	: params.lgutype,
			revtype 	: params.sharetype.replace('disc',''),
			item 		: [objid:params.acct.key, title:params.acct.value],
			amount 		: 0.0,
			discount    : 0.0,
		]
	}
}	
