package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;

public class CalcFireCode implements RuleActionHandler {
	def numSvc
	def items 

	public void execute(def params, def drools) {
		def item = items.find{ it.objid == params.rptledgeritem.objid }

		item.firecode = numSvc.round(params.expr.getDecimalValue())
		
		item.basicnet = item.basic - item.basicdisc + item.basicint
		item.sefnet = item.sef - item.sefdisc + item.sefint
		item.totalbasicsef = item.basicnet + item.sefnet
		item.total = item.totalbasicsef + item.firecode + 
		             item.basicidle - item.basicidledisc + item.basicidleint

		def rli = params.rptledgeritem
		rli.firecode = item.firecode
		rli.total = item.total 
	}
}	