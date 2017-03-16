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
public class BuildDailyItems implements RuleActionHandler {

	public void execute(def params, def drools) {

		def ctx = RuleExecutionContext.getCurrentContext();
		def facts = ctx.facts;

		def fromdate = params.fromdate;
		def todate = params.todate;
		def amtpaid = params.payment.amount;	

		def mu = params.marketunit;
		def rate = mu.rate;
		def extrate = mu.extrate;
		def linetotal = rate + extrate;

		//calculate the number of day iterations necessary based on amount or dates.
		int i1 = 0;
		if( amtpaid > 0 ) {
			i1 = (int)amtpaid / linetotal +  (((amtpaid % linetotal)>0)? 1 : 0) ;
		}
				
		int i2 = DateFunc.daysDiff( fromdate, todate ) + 1;

		int i = ((i1 > i2) ? i1  : i2);

		//calculate the new todate based on number of days.
		def cal = Calendar.instance;
		cal.setTime( fromdate );
		cal.add( Calendar.DAY_OF_YEAR, i-1);
		todate = cal.getTime();

		int monthsDiff = DateFunc.monthsDiff( fromdate, todate )+1;

		def _fromdate = fromdate;

		for(int x=0; x < monthsDiff; x++ ) {
		   cal.setTime(_fromdate);
		   int _yr = cal.get( Calendar.YEAR );
		   int _mon = cal.get( Calendar.MONTH )+1;

		   int _days = cal.getActualMaximum( Calendar.DAY_OF_MONTH );
		   cal.add( Calendar.DAY_OF_MONTH, _days - 1 );
		   def _todate = cal.getTime();
		   if( _todate.after(todate) ) {
		   		_todate = todate;
		   		_days = DateFunc.daysDiff( _fromdate, _todate )+1;
		   }		
		   
		   MarketMonthEntry me = new MarketMonthEntry();
		   me.fromdate = _fromdate;
		   me.year = _yr;
		   me.month = _mon;
		   me.rate = rate * _days;
		   me.extrate = extrate * _days ;
		   me.days = _days;
		   me.todate = _todate;
		   if(x==0) {
		   		if( mu.partialbalance > 0 ) me.rate = NumberUtil.round( mu.partialbalance + (me.rate * (_days - 1)) ) ;
		   		if( mu.partialextbalance > 0 ) me.extrate = NumberUtil.round( mu.partialextbalance + (me.extrate * (_days - 1)) );
		   }
		   facts << me;

		   //add 1 day to todate
		   cal.add( Calendar.DAY_OF_MONTH, 1 );
		   _fromdate = cal.getTime();
		} 

	};


	

}