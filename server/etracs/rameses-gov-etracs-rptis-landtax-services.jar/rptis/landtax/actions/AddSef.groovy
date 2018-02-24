package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;

public class AddSef implements RuleActionHandler {
	def request

	public void execute(def params, def drools) {
		def avfact = params.avfact 

		def item = [:]
		item.objid = 'RLI' + new java.rmi.server.UID()
		item.rptledgerfaas = [objid:avfact.objid]
		item.rptledgerfaasid = avfact.objid 
		item.year = params.year 
		item.taxdifference = avfact.taxdifference 
		item.av = params.av.getDecimalValue()
		item.basicav = (params.basicav ? params.basicav.getDecimalValue() : item.av)
		item.sefav = (params.sefav ? params.sefav.getDecimalValue() : item.av)
		item.amount = 0.0
		item.amtpaid = 0.0

		item.revtype = 'sef'
		item.system = true 
		item.priority = 10000 + (item.taxdifference == 1 || item.taxdifference == true ? -1 : 0)

		request.items << item 

		def ledgerfact = request.facts.find{it instanceof RPTLedgerFact}
		request.facts << new RPTLedgerItemFact(ledgerfact, item)
	}
}	