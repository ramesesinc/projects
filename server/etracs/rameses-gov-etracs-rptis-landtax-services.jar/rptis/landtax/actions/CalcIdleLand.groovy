package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;


public class CalcIdleLand implements RuleActionHandler {
	def numSvc
	def items 

	public void execute(def params, def drools) {
		def item = items.find{ it.objid == params.rptledgeritem.objid }
		if (params.computationtype.equalsIgnoreCase('tax'))
			item.basicidle = numSvc.round(params.expr.getDecimalValue())
		else if (params.computationtype.equalsIgnoreCase('discount'))
			item.basicidledisc = numSvc.round(params.expr.getDecimalValue())
		else if (params.computationtype.equalsIgnoreCase('penalty'))
			item.basicidleint = numSvc.round(params.expr.getDecimalValue())

		item.basicnet = item.basic - item.basicdisc + item.basicint
		item.sefnet = item.sef - item.sefdisc + item.sefint
		item.totalbasicsef = item.basicnet + item.sefnet
		item.total = item.totalbasicsef + item.firecode + 
		             item.basicidle - item.basicidledisc + item.basicidleint

		def rli = params.rptledgeritem
		rli.basicidle = item.basicidle
		rli.basicidledisc = item.basicidledisc
		rli.basicidleint = item.basicidleint
		rli.total = item.total 
	}
}	
