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
		cal.setTime( fromtime );
		cal.add( Calendar.DAY_OF_YEAR, i-1);

		def currdate = fromdate;

		for(int x=0; x<i; x++ ) {
		   cal.setTime( currdate );
		   cal.add(Calendar.MONTH, x);
		   cal.set(Calendar.DATE, 1); 
		   int days = cal.getActualMaximum( Calendar.DAY_OF_MONTH );
		   def cdate = cal.getTime();

		   MarketMonthEntry me = new MarketMonthEntry();
		   me.fromdate = cdate;
		   me.year = cal.get( Calendar.YEAR );
		   me.month = cal.get( Calendar.MONTH )+1;
		   me.rate = rate;
		   me.extrate = extrate;
		   me.days = days;
		   me.todate = DateFunc.getDayAdd(cdate,  days - 1);
		   if(x==0) {
		   		if( mu.partialbalance > 0 ) me.rate = mu.partialbalance;
		   		if( mu.partialextbalance > 0 ) me.extrate = mu.partialextbalance;
		   }
		   facts << me;
		} 

	};


	

}