package rules.treasury.facts;

import com.rameses.rules.common.*;
import com.rameses.osiris3.common.*;
import com.rameses.util.*;

public class ItemAccountLookup {
	
	private def map = [:];
	private def svc;

	public def lookup( def acctid ) {
		if(svc==null) {
			svc = EntityManagerUtil.lookup( "itemaccount" );
		}
		if( ! map.containsKey(acctid)) {
			def m = svc.find( [objid: acctid] ).first();	
			if( !m ) throw new Exception("Account not found in item account. " + o.title);
			map.put(acctid, m );
		}
		return map.get(acctid);		
	}
 
}