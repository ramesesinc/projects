package treasury.utils;

import com.rameses.rules.common.*;
import com.rameses.osiris3.common.*;
import com.rameses.util.*;
import treasury.facts.*;

public class ItemAccountUtil {
	
	private def map = [:];
	private def svc;

	public def lookup( def acctid ) {
		if(svc==null) {
			svc = EntityManagerUtil.lookup( "itemaccount" );
		}
		if( ! map.containsKey(acctid)) {
			def m = svc.find( [objid: acctid] ).first();	
			if( !m ) throw new Exception("Account not found in item account.  " );
			map.put(acctid, m );
		}
		return map.get(acctid);		
	}
 
	public def createAccountFact(def v) {
		def acct = lookup(v.objid);
		return buildAccountFact( acct );
	}

	public def createAccountFactByOrg( def parentid, def orgid ) {
		if(svc==null) {
			svc = EntityManagerUtil.lookup( "itemaccount" );
		}
		def o = svc.find([ parentid: parentid ]).where(' org.objid = :orgid ', [ orgid: orgid ]).first(); 
		if ( o ) {
			return buildAccountFact( o );
		} 
		return null; 
	}

	public def buildAccountFact(def acct ) {
		Fund f = null;
		if( acct.fund?.objid  ) {
			f = new Fund( objid: acct.fund.objid, code: acct.fund.code, title: acct.fund.title);
		}
		def ac = new Account( objid: acct.objid, code: acct.code, title: acct.title, fund: f);
		if( acct.parentaccount?.objid  ) {
			def pac = acct.parentaccount;
			ac.parentaccount = new Account(objid: pac.objid, code: pac.code, title: pac.title,   )
		}
		return ac;
	}


	public def lookupIdByParentAndOrg( def parentid, def orgid ) {
		if(svc==null) {
			svc = EntityManagerUtil.lookup( "itemaccount" );
		}; 
		return svc.select("objid").find( [parentid:parentid ] ).where("org.objid = :orgid", [orgid: orgid ]);
	}

}