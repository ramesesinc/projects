package treasury.utils;

import com.rameses.rules.common.*;
import com.rameses.osiris3.common.*;
import com.rameses.util.*;

public class LookupAccount {
	
	private def map = [:];
	private def svc;

	public def findAccount( def o ) {
		if(svc==null) {
			svc = EntityManagerUtil.lookup( "itemaccount" );
		}
		if( ! map.containsKey(o.objid)) {
			def m = svc.find( [objid: o.objid] ).first();	
			if( !m ) throw new Exception("Account not found in item account. " + o.title);
			map.put(o.objid, m );
		}
		return map.get(o.objid);		
	}
 
}