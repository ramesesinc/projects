package market.actions;

import com.rameses.rules.common.*;
import market.facts.*;
import com.rameses.util.*;
import java.util.*;
import com.rameses.osiris3.common.*;
import treasury.facts.*;
import treasury.utils.*;

//drools is class org.drools.base.DefaultKnowledgeHelper
public class AddMarketBasicMonth implements RuleActionHandler {

	public def buildMonths( def fromdate, def todate ) {
		Calendar cal = Calendar.instance;
		cal.setTime( fromdate );  
		int y1 = cal.get(Calendar.YEAR);
		int m1 = cal.get(Calendar.MONTH);
		int d1 = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime( todate );  
		int y2 = cal.get(Calendar.YEAR);
		int m2 = cal.get(Calendar.MONTH);
		int d2 = cal.get(Calendar.DAY_OF_MONTH);

		int ym1 = (y1*12)+m1;
		int ym2 = (y2*12)+m2;

		def list = [];
		boolean first = true;
		( ym1..ym2 ).eachWithIndex { o,i->
		    def m = [:];

		    int yr = (int) (o / 12);
		    int mon = (o % 12);
		    cal.set(Calendar.YEAR, yr );
		    cal.set(Calendar.MONTH, mon );
		    int dd1 = 1;
		    if( first ) {
		    	dd1 = d1;
		    	first = false;
		    }	
		    cal.set(Calendar.DATE, dd1);
			m.fromdate = cal.getTime();

		    int dd2 = cal.getActualMaximum( Calendar.DAY_OF_MONTH);		    
		    
		    if( o == ym2 ) dd2 = d2;
		    cal.set(Calendar.DATE, dd2 );
		    m.todate = cal.getTime();

		    m.days = (dd2 - dd1) + 1;
		    m.year = yr;
		    m.month = mon + 1;
		    m.index = i++;
		    list << new MarketMonthEntry( m );
		}
		return list;		
	}


	/*************************************************************************
	* for monthly period, adjust fromdate and todate to begin and month ends
	**************************************************************************/
	public void execute(def params, def drools) {
		def ctx = RuleExecutionContext.getCurrentContext();
		def facts = ctx.facts;

		def period = params.billperiod;
		def fromdate = period.fromdate;
		def todate = period.todate;
		def mu = params.marketunit;

		if(mu==null) throw new Exception("marketunit is required in rule!");
		if(period==null) throw new Exception("billperiod is required in rule!");

		//if pay frequency is monthly we will adjust the fromdate and todate to beginning and ending months
		if( mu.payfrequency == 'MONTHLY' ) {
			def cal = Calendar.instance;
			cal.setTime(fromdate);
			cal.set(Calendar.DATE, 1);
			fromdate = cal.getTime();

			cal.setTime(todate);
			todate = cal.getTime();
			int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
			cal.set(Calendar.DATE, days);
			todate = cal.getTime();
		}

		def list = buildMonths( fromdate, todate ); 
		list.each {
			facts << it;
			drools.insert(it);		
		}
	};


	

}