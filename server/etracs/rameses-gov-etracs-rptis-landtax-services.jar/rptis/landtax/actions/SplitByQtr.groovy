package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;

public class SplitByQtr implements RuleActionHandler {
	def NS 
	def facts 

	public void execute(def params, def drools) {
		def item = params.rptledgeritem

		drools.retract(item)
		facts.remove(item)

		def qtrlyvalues = buildQtrlyValues(item)

		def payment = [:]
		payment.basicpaid = item.basicpaid 
		payment.basicidlepaid = item.basicidlepaid 
		payment.sefpaid = item.sefpaid 
		payment.firecodepaid = item.firecodepaid 
		payment.shpaid = item.shpaid 

		def amtpaid = 0.0
		payment.each{k,v -> amtpaid += v}

		for( int qtr=1; qtr<=4; qtr++){
			if (amtpaid > 0 && amtpaid >= qtrlyvalues[qtr].amtdue ){
				amtpaid -=  qtrlyvalues[qtr].amtdue 
				payment.basicpaid -= qtrlyvalues[qtr].basic  
				payment.basicidlepaid -= qtrlyvalues[qtr].basicidle  
				payment.sefpaid -= qtrlyvalues[qtr].sef  
				payment.firecodepaid -= qtrlyvalues[qtr].firecode 
				payment.shpaid -= qtrlyvalues[qtr].sh  
				continue 
			}

			def qifact = createLedgerItemFact(item, qtr, qtrlyvalues[qtr])
			if (amtpaid > 0){
				qifact.basicpaid = payment.basicpaid 
				qifact.basicidlepaid = payment.basicidlepaid 
				qifact.sefpaid = payment.sefpaid 
				qifact.firecodepaid = payment.firecodepaid 
				qifact.shpaid = payment.shpaid 
				amtpaid = 0
			}
			qifact.qtrly = true
			facts << qifact 
			drools.insert(qifact)
		}
	}

	def buildQtrlyValues(item){
		def av13 = NS.round(item.av / 4)
		def av4 = NS.round(item.av - (av13 * 3))
		def basicav13 = NS.round(item.basicav / 4)
		def basicav4 = NS.round(item.basicav - (basicav13 * 3))
		def sefav13 = NS.round(item.sefav / 4)
		def sefav4 = NS.round(item.sefav - (sefav13 * 3))


		def basic13 = NS.round(item.basic / 4)
		def basic4 = NS.round(item.basic - (basic13 * 3))
		def basicidle13 = NS.round(item.basicidle / 4)
		def basicidle4 = NS.round(item.basicidle - (basicidle13 * 3))
		def sef13 = NS.round(item.sef / 4)
		def sef4 = NS.round(item.sef - (sef13 * 3))
		def sh13 = NS.round(item.sh / 4)
		def sh4 = NS.round(item.sh - (sh13 * 3))

		def amtdue13 = basic13 + basicidle13 + sef13 + sh13
		def amtdue4 = basic4 + basicidle4 + sef4 + sh4 

		def qv = []
		qv[0] = [av:av13, basicav:basicav13, sefav:sefav13, basic:basic13, basicidle:basicidle13, sef:sef13, sh:sh13, firecode:item.firecode, amtdue:amtdue13 + item.firecode]
		qv[1] = [av:av13, basicav:basicav13, sefav:sefav13, basic:basic13, basicidle:basicidle13, sef:sef13, sh:sh13, firecode:item.firecode, amtdue:amtdue13 + item.firecode]
		qv[2] = [av:av13, basicav:basicav13, sefav:sefav13, basic:basic13, basicidle:basicidle13, sef:sef13, sh:sh13, firecode:0.0, amtdue:amtdue13]
		qv[3] = [av:av13, basicav:basicav13, sefav:sefav13, basic:basic13, basicidle:basicidle13, sef:sef13, sh:sh13, firecode:0.0, amtdue:amtdue13]
		qv[4] = [av:av4, basicav:basicav4, sefav:sefav4, basic:basic4, basicidle:basicidle4, sef:sef4, sh:sh4, firecode:0.0, amtdue:amtdue4]
		return qv 
	}

	def createLedgerItemFact(item, qtr, qtrav){
		def qi = new RPTLedgerItemFact()
		qi.objid = item.objid + '-' + qtr 
		qi.parentid = item.objid 
		qi.rptledger = item.rptledger
		qi.rptledgerfaasid = item.rptledgerfaasid
		qi.rptledgeritemid = item.rptledgeritemid
		qi.firstitem = item.firstitem
		qi.year = item.year
		qi.qtr = qtr 
		qi.av = qtrav.av 
		qi.basicav = qtrav.basicav 
		qi.sefav = qtrav.sefav 
		qi.basic = qtrav.basic 
		qi.basicidle = qtrav.basicidle 
		qi.sef = qtrav.sef 
		qi.firecode = qtrav.firecode 
		qi.sh = qtrav.sh 
		qi.txntype = item.txntype
		qi.classification = item.classification
		qi.actualuse = item.actualuse
		qi.numberofmonthsfromjan = item.numberofmonthsfromjan
		qi.numberofmonthsfromqtr = (item.numberofmonthsfromjan - ((qtr - 1) * 3))
		qi.backtax = item.backtax
		qi.expr = item.expr
		qi.idleland = item.idleland
		qi.paidyear = item.paidyear
		qi.revperiod = item.revperiod
		qi.reclassed = item.reclassed
		qi.idx = item.idx
		qi.taxdifference = item.taxdifference
		qi.fullpayment = item.fullpayment
		qi.qtrlypaymentavailed = item.qtrlypaymentavailed
		qi.qtrly = item.qtrly
		return qi 
	}
}