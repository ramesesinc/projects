package market.utils;

import com.rameses.rules.common.*;
import com.rameses.osiris3.common.*;
import com.rameses.util.*;
import treasury.facts.*;

public class MarketDueDateLookup {
	
	private def map = [:];
	private def svc;

	public def lookup( int year, int month ) {
		if(svc==null) {
			svc = EntityManagerUtil.lookup( "market_duedate" );
		}
		def str = "" + year + "-" + month;
		if( ! map.containsKey(str)) {
			def m = svc.find( [year: year, month: month] ).first();	
			map.put(str, m?.duedate );
		}
		return map.get(str);		
	}
 

}