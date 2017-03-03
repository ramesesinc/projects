package market.actions;

import com.rameses.rules.common.*;
import market.facts.*;
import com.rameses.util.*;
import java.util.*;
import com.rameses.osiris3.common.*;
import treasury.facts.*;
import treasury.utils.*;

//drools is class org.drools.base.DefaultKnowledgeHelper
public class AddMarketExcessMonth implements RuleActionHandler {

	public def buildMonths( def fromdate, double total, def handler ) {
	    def cal = Calendar.instance;
	    cal.setTime(fromdate);
	    int year = cal.get(Calendar.YEAR);
	    int mon = cal.get(Calendar.MONTH);
	    int d = cal.get(Calendar.DAY_OF_MONTH);
	    
	    int idx = 0;
	    int ym = (year*12)+mon;
	    def amt = total;
	    def list = [];
	    while( amt > 0 ) {
	        int _yr = (int) (ym / 12);
	        int _mon = (ym % 12);
	        def m = [:];        
	        cal.set(Calendar.YEAR, _yr );
	        cal.set(Calendar.MONTH, _mon );
	        cal.set(Calendar.DATE, d );
	        m.fromdate = cal.getTime();

	        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	        cal.set(Calendar.DATE, days );
	        m.todate = cal.getTime();

	        m.year = _yr;
	        m.month = _mon + 1;
	        m.days = days - d + 1;
	        
	        def me = handler(m, amt);
	        list << me;
	        amt = amt - me.linetotal;
	        if( amt <= 0 ) break;
	        ym++;    //move the next month    
	        d = 1;  //reset the day so the fromdate will start at 1 on the next loop     
	    };
	    return list;
	}

	public def getMonthEntryMonthly( def marketunit, def m, def amt ) {
		def unitrate = marketunit.rate + marketunit.extrate;
		if( amt < unitrate )  {
			m.rate = NumberUtil.round( amt * (marketunit.rate/unitrate)  );
			m.extrate = NumberUtil.round( amt * (marketunit.extrate/unitrate)  );
		};
		else {
			m.rate = NumberUtil.round( marketunit.rate  );
			m.extrate = NumberUtil.round( marketunit.extrate );
		}
		return new MarketExcessMonthEntry(m);
	}

	public def getMonthEntryDaily( def marketunit, def m, def amt ) {
		def unitrate = marketunit.rate + marketunit.extrate;
		def retval = unitrate * m.days;
		if( amt < retval )  {
			m.rate = NumberUtil.round( amt * (marketunit.rate/unitrate)  );
			m.extrate = NumberUtil.round( amt * (marketunit.extrate/unitrate)  );
			m.days = (int) (amt / unitrate);
			if( amt % unitrate ) m.days = m.days + 1;
			m.todate = DateUtil.add( m.fromdate, (m.days-1) +"d" );
		}
		else {
			m.rate = NumberUtil.round( marketunit.rate * m.days  );
			m.extrate = NumberUtil.round( marketunit.extrate * m.days  );
		}
		return new MarketExcessMonthEntry(m);
	}

	public void execute(def params, def drools) {
		if( !params.fromdate ) 
			throw new Exception("fromdate is required in AddMArketExcessMonth");

		def ctx = RuleExecutionContext.getCurrentContext();
		def facts = ctx.facts;

		def fromdate = DateUtil.add(params.fromdate, "1d");
		def excess = params.excess;
		def marketunit = params.marketunit;
		def h = { m, amt ->
			if( marketunit.payfrequency == "MONTHLY" ) {
				return getMonthEntryMonthly( marketunit, m, amt );
			}
			else {
				return getMonthEntryDaily( marketunit, m, amt );	
			}
		}
		def list = buildMonths( fromdate, excess.amount, h  ); 
		list.each {
			facts << it;
			drools.insert(it);		
		}
		drools.retract(excess);
	}
}

