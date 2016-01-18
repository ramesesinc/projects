package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;

public class CalcBasic implements RuleActionHandler {
	def numSvc
	def items 

	public void execute(def params, def drools) {
		def item = items.find{ it.objid == params.rptledgeritem.objid }

		if (params.computationtype.equalsIgnoreCase('tax'))
			item.basic = numSvc.round(params.expr.getDecimalValue())
		else if (params.computationtype.equalsIgnoreCase('discount'))
			item.basicdisc = numSvc.round(params.expr.getDecimalValue())
		else if (params.computationtype.equalsIgnoreCase('penalty'))
			item.basicint = numSvc.round(params.expr.getDecimalValue())

		item.basicnet = item.basic - item.basicdisc + item.basicint
		item.sefnet = item.sef - item.sefdisc + item.sefint
		item.totalbasicsef = item.basicnet + item.sefnet
		item.total = item.totalbasicsef + item.firecode + 
		             item.basicidle - item.basicidledisc + item.basicidleint

		def rli = params.rptledgeritem
		rli.basic = item.basic
		rli.basicdisc = item.basicdisc
		rli.basicint = item.basicint
		rli.basicnet = item.basicnet
		rli.totalbasicsef = item.totalbasicsef
		rli.total = item.total 
	}
}	