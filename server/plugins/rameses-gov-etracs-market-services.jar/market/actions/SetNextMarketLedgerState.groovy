package market.actions;

import com.rameses.rules.common.*;
import market.facts.*;
import com.rameses.util.*;
import java.util.*;
import com.rameses.osiris3.common.*;
import treasury.facts.*;

//This rule gets the market payment result. It gets the last item base on the date.
public class SetNextMarketLedgerState implements RuleActionHandler {

	public void execute(def params, def drools) {

		def bi = params.billitem;  //market bill item
		def ext = params.extbillitem;  //extended market bill item
		def mu = params.marketunit;	  //market rental unit	
		def todate = bi.todate;

		def ct = RuleExecutionContext.getCurrentContext();
		def facts = ct.facts;
		//check if market ledger status exists
		def v = facts.find{ it instanceof MarketLedgerStatus };
		if(!v  || v.startdate.before(todate) ) {
			//println adding market ledger status
			def mp = new MarketLedgerStatus();
			def r1 = bi.amount % mu.rate;
			def ex1 = ext.amount % mu.extrate;
			if(r1 > 0) mp.partialbalance = NumberUtil.round( mu.rate - r1) ;
			if(ex1 > 0) mp.partialextbalance = NumberUtil.round(mu.extrate - ex1);
			mp.startdate = DateUtil.add( todate, "1d" );
			if( v!=null) facts.remove(v);
			facts << mp;
		}
	}
}