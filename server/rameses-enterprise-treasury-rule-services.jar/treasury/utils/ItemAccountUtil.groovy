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
			if( !m ) throw new Exception("Account not found in item account. " );
			map.put(acctid, m );
		}
		return map.get(acctid);		
	}
 
	public def createAccountFact(def v) {
		def acct = lookup(v.objid);
		Fund f = null;
		if( acct.fund?.objid  ) {
			f = new Fund( objid: acct.fund.objid, code: acct.fund.code, title: acct.fund.title);
		}
		return new Account( objid: acct.objid, code: acct.code, title: acct.title, fund: f);
	}

}