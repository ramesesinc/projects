package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;



public class AddLguShare implements RuleActionHandler {
	def numSvc
	def request
	def lgutype
	def EM

	public void execute(def params, def drools) {
		/* params:
		*      - taxsummary 
		*      - sharetype
		*/

		def amount = numSvc.roundA(params.expr.getDecimalValue(), 4)
		if (amount <= 0 ) return 

		params.revtype = params.taxsummary.revtype
		params.revperiod = params.taxsummary.revperiod 
		params.lgutype = lgutype 
		
		params.accttype = params.revtype
		if ('interest' == params.sharetype){
			params.accttype += 'int'
		}

		params.key = params.revtype + ':' + params.revperiod + ':' + params.accttype 
		params.acct = AccountCache.instance.getRevenueAccount(EM, params)
		params.stype = (params.sharetype != 'discount' ? params.sharetype : 'amount')

		def sharefact = request.facts.findAll{f -> f instanceof rptis.landtax.facts.ShareFact}.find{
			return  it.lgutype == params.lgutype && 
					it.revtype == params.revtype &&
					it.revperiod == params.revperiod &&
					it.sharetype == params.stype 

		}

		if (!sharefact){
			def share = createShare(params)
			sharefact = new rptis.landtax.facts.ShareFact(params, share)
			request.shares << share 
			request.facts << sharefact 
		}

		if (params.sharetype == 'discount'){
			sharefact.discount += amount 
			sharefact.amount -= amount 
		}
		else {
			sharefact.amount += amount 
		}
	}

	def createShare(params){
		return [
			accttype    : params.accttype , 
			revtype 	: params.revtype,
			revperiod	: params.revperiod,
			sharetype 	: params.lgutype,
			item 		: params.acct.item,
			amount 		: 0.0,
			discount    : 0.0,
		]
	}
}	
