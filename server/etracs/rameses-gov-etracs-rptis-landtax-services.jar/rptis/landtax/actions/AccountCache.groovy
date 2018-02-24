package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;

public class AccountCache{
	private static AccountCache instance;
	
	private AccountCache(){}
	
	public static AccountCache getInstance(){
		if (!instance){
			instance = new AccountCache();
		}
		return instance;
	}

	public def getAccount(em, params){
		return getRevenueAccount(em, params)
	}

	private def getRevenueAccount(em, params){
		def acct = params.acct 
		if (acct)
			return acct;

		def p = [:]
		p.revtype = params.accttype
		p.revperiod = params.revperiod 
		def fields = 'item.objid,item.code,item.title,item.fund.objid'

		if ('barangay'.equalsIgnoreCase(params.lgutype)){
			p.lgu_objid = params.taxsummary.ledger.barangayid
			acct = em.select(fields).find(p).first() 
		}
		else if (params.lgutype.toLowerCase().matches('municipality|city')){
			p.lgu_objid = params.taxsummary.ledger.lguid
			acct = em.select(fields).find(p).first() 
		}
		else if ('province'.equalsIgnoreCase(params.lgutype)){
			p.lgu_orgclass = 'PROVINCE'
			acct = em.select(fields).find(p).first() 	
		}

		if (acct){
			return acct 
		}
		throw new Exception('Account mapping does not exist for ' + p.revtype + ' ' + p.revperiod + '.')
	}
}	