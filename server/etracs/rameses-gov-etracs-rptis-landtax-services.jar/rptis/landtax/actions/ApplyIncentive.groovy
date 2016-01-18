package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;

public class ApplyIncentive implements RuleActionHandler {
	def numSvc
	def items 

	public void execute(def params, def drools) {
		def item = items.find{ it.objid == params.rptledgeritem.objid }
		def incentive = params.incentive

		if (incentive.basicrate > 0.0){
			item.basic = numSvc.round( item.basic * (100 - incentive.basicrate) / 100.0 )
			item.basicdisc = numSvc.round( item.basicdisc * (100 - incentive.basicrate) / 100.0 )
			item.basicint = numSvc.round( item.basicint * (100 - incentive.basicrate) / 100.0 )
		}
		
		if (incentive.sefrate > 0.0){
			item.sef = numSvc.round( item.sef * (100 - incentive.sefrate) / 100.0 )
			item.sefdisc = numSvc.round( item.sefdisc * (100 - incentive.sefrate) / 100.0 )
			item.sefint = numSvc.round( item.sefint * (100 - incentive.sefrate) / 100.0 )
		}

		item.basicnet = item.basic - item.basicdisc + item.basicint
		item.sefnet = item.sef - item.sefdisc + item.sefint
		item.totalbasicsef = item.basicnet + item.sefnet
		item.total = item.totalbasicsef + item.firecode 

		def rli = params.rptledgeritem
		rli.basic = item.basic
		rli.basicdisc = item.basicdisc
		rli.basicint = item.basicint
		rli.basicnet = item.basicnet
		rli.sef = item.sef
		rli.sefdisc = item.sefdisc
		rli.sefint = item.sefint
		rli.sefnet = item.sefnet 
		rli.totalbasicsef = item.totalbasicsef
		rli.total = item.total 
	}
}	
