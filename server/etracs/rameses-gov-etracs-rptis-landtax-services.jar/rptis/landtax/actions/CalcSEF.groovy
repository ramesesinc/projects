package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;


public class CalcSEF implements RuleActionHandler {
	def numSvc
	def items 

	public void execute(def params, def drools) {
		def item = items.find{ it.objid == params.rptledgeritem.objid }

		if (params.computationtype.equalsIgnoreCase('tax') )
			item.sef = numSvc.round(params.expr.getDecimalValue())
		else if (params.computationtype.equalsIgnoreCase('discount'))
			item.sefdisc = numSvc.round(params.expr.getDecimalValue())
		else if (params.computationtype.equalsIgnoreCase('penalty'))
			item.sefint = numSvc.round(params.expr.getDecimalValue())

		item.basicnet = item.basic - item.basicdisc + item.basicint
		item.sefnet = item.sef - item.sefdisc + item.sefint
		item.totalbasicsef = item.basicnet + item.sefnet
		item.total = item.totalbasicsef + item.firecode + 
		             item.basicidle - item.basicidledisc + item.basicidleint

		def rli = params.rptledgeritem
		rli.sef = item.sef
		rli.sefdisc = item.sefdisc
		rli.sefint = item.sefint
		rli.sefnet = item.sefnet 
		rli.totalbasicsef = item.totalbasicsef
		rli.total = item.total 
	}
}