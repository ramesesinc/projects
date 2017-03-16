package market.actions;

import com.rameses.rules.common.*;
import market.facts.*;
import com.rameses.util.*;
import java.util.*;
import com.rameses.osiris3.common.*;
import treasury.facts.*;
import treasury.utils.*;
import com.rameses.functions.*;

//drools is class org.drools.base.DefaultKnowledgeHelper
public class BuildMonthItems implements RuleActionHandler {

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

	private def correctFromdate( def dt, def freq ) {
		if( freq == 'MONTHLY') {
			def cal = Calendar.instance;
			cal.setTime(dt);
			cal.set(Calendar.DATE, 1);
			return cal.getTime();
		}
		return dt;	
	}

	private def correctTodate( def dt, def freq ) {
		if( freq == 'MONTHLY') {
			def cal = Calendar.instance;
			cal.setTime(dt);
			int days = cal.getActualMaximum( Calendar.DAY_OF_MONTH );
			cal.set(Calendar.DATE, days);
			return cal.getTime();
		}
		return dt;
	}


	/*************************************************************************
	* for monthly period, adjust fromdate and todate to begin and month ends
	**************************************************************************/
	public void execute(def params, def drools) {
		def ctx = RuleExecutionContext.getCurrentContext();
		def facts = ctx.facts;

		def billperiod = params.billperiod;
		def amtpaid = billperiod.payment.amount;	
		def mu = params.marketunit;
		def rate = mu.rate;
		def extrate = mu.extrate;
		def payfrequency = mu.payfrequency;

		def fromdate = correctFromdate( billperiod.fromdate, payfrequency );
		def todate = correctTodate( billperiod.todate, payfrequency );

		def linetotal = rate + extrate;
		//calculate the number of iterations necessary based on amount or dates.
		int i1 = 0;
		if( amtpaid > 0 ) {
			i1 = (int)amtpaid / linetotal +  (((amtpaid % linetotal)>0)? 1 : 0) ;
		}
				
		int i2 = DateFunc.monthsDiff( fromdate, todate ) + 1;

		int i = ((i1 > i2) ? i1  : i2);

		def cal = Calendar.instance;
		def currdate = fromdate;

		def list = [];

		for(int x=0; x<i; x++ ) {
		   cal.setTime( currdate );
		   cal.add(Calendar.MONTH, x);
		   if( x > 0 ) cal.set(Calendar.DATE, 1); 
		   int days = cal.getActualMaximum( Calendar.DAY_OF_MONTH );
		   def cdate = cal.getTime();
		   
		   MarketMonthEntry me = new MarketMonthEntry();
		   

		   println x + " " + df.format(cdate) + " days " + days; 
		} 



	};


	

}