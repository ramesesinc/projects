package market.actions;

import com.rameses.rules.common.*;
import market.facts.*;
import com.rameses.util.*;
import java.util.*;
import com.rameses.osiris3.common.*;
import treasury.facts.*;
import treasury.utils.*;

/********************************************************************************** 
* This action creates month entry facts. Could be daily or monthly
* Options:
*   payfrequency : sepcifies DAILY or MONTHLY
*   fromdate (required): starts off with from date
*   todate (optional): if specified ends with to date
*   amount (optional): if specified distributes payment until it is fully used.
***********************************************************************************/
public class AddMarketMonthEntry implements RuleActionHandler {

	private MonthYearIterator mi = new MonthYearIterator();

	//cursor here refers to the looped entry in MonthYearIterator
	private createDailyMonthEntry( MarketRentalUnit mru, int year, int month, int days, def fromdate, def todate, def partialbalance, def partialextbalance ) {
		def me = new MarketMonthEntry();
		me.month = month;
		me.year = year;
		me.days = days;
		me.fromdate = fromdate;
		me.todate = todate;
		me.index = mx.index;
		if( partialbalance && (mx.index==0) ) {
			me.rate =   (param.rate * (mx.days-1)) + partialbalance ;
			me.extrate = (param.extrate * (mx.days-1)) + partialextbalance;
		}
		else {
			me.rate =   param.rate * mx.days;
			me.extrate = param.extrate * mx.days;
		}
		return me;
	}

	private MonthYearEntry createDailyItem( def fromdate, def days ) {
		if(days <=0) return null;
  		def cal = Calendar.instance;
      	cal.setTime(fromdate);
      	int year = cal.get( Calendar.YEAR );
      	int month = cal.get( Calendar.MONTH)+1;
		
		//get the no. of days in a month
		def todate = mi.findMonthEnd( fromdate );
		int monthDays = DateUtil.diff(fromdate, todate, Calendar.DATE)+1;

		if( days > monthDays ) {
			days = monthDays;
		}
		else {
			todate = DateUtil.add( fromdate, (days-1) + "d" );
		}
		def ad = new MonthYearEntry();
		ad.year = year;
		ad.month = month;
		ad.fromdate = fromdate;
		ad.todate = todate;
		ad.days = days;
		return ad;
	}


	public void execute(def params, def drools) {
		def pmt = params.payment;
		def freq = params.payfrequency;
		def fromdate = params.fromdate;
		def todate = params.fromdate;

		def ctx = RuleExecutionContext.getCurrentContext();
		def facts = ctx.facts;


		def monthCursor = fromdate;
		while(true) {

			monthCursor = todate;
		}


		def mi = new MonthYearIterator();
		def handler = dailtEntryHandler;

		mi.collectYearMonth( fromdate, todate, { mx-> fb.facts << me;
		});

		list.each {
			facts << it;
			drools.insert(it);		
		}
		drools.retract(pmt);
	}
}

===========================================================================
import com.rameses.util.*;
def fromdate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse("2016-12-25");
def todate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse("2016-12-28");

int getMaxDays( def t ) {
    def cal = Calendar.instance;
    cal.setTime(t);
    int d = cal.get(Calendar.DAY_OF_MONTH);
    return (cal.getActualMaximum(Calendar.DAY_OF_MONTH)-d)+1;
};

double calcAmount( def freq, double amt, def days )  {
    if( freq == "MONTHLY" ) return amt;
    else return amt * days;
}

class MonthItem {
   Date fromdate;
   Date todate;
   double rate;
   double extrate; 
   int days;

   double partialbalance;
   double partialextbalance; 
   
   String toString() {
       return " from:" + fromdate + "-" + todate + " days:"+days + " rate:" + rate + " ext:" + extrate;
   }
   
}

def createItem( Date fromdate, Date todate, int days, double rate, double extrate, String freq ) {
     def mi = new MonthItem();
     mi.days = days;  
     mi.fromdate = fromdate;
     mi.todate = todate;
     mi.rate = rate;
     mi.extrate = extrate;
     return mi;
}        

def dateCursor = fromdate;
def amount = 1100.00;
double rate = 10.0;
double extrate =  5.0; 
def freq = "DAILY";
def list = [];

//breakdown amount:
while(amount > 0) {
    def d = getMaxDays(dateCursor);
    def drate = calcAmount(freq, rate, d) + calcAmount(freq, extrate, d);
    if( drate < amount ) {
        def _todate = DateUtil.add( dateCursor, (d-1)+"d" );
        list << createItem( dateCursor, _todate, d,calcAmount( freq, rate, d ), calcAmount( freq, extrate, d ), freq);             
        dateCursor = DateUtil.add(_todate, "1d"); 
        amount = amount - drate;
    }
    else {
        //find how many days remaining that can cover the amount
        def tot = (rate+extrate);
        d = (int)(amount/tot );
        def rem = (amount % tot);
    
        println "number of days last " + d + "rem is "+rem;
        
        def _rate = calcAmount( freq, rate, d);
        def _extrate = calcAmount( freq, extrate, d);
        
        println "calc amount " + _rate;
        println "calc ext amount " + _extrate;
        println "tot is " + tot;
        if(rem>0) {
            d = d+1;
            println "_rate" + NumberUtil.round((rem * (rate / tot)));
            println "_extrate " + NumberUtil.round((rem * (extrate / tot)));
            _rate += NumberUtil.round((rem * (rate / tot)));
            _extrate += NumberUtil.round((rem * (extrate / tot)));
             println (_rate);
            println (_extrate);            
        }    
        def _todate = DateUtil.add( dateCursor, (d-1)+"d" );
        list << createItem( dateCursor, _todate, d, _rate, _extrate, freq ); 
        dateCursor = DateUtil.add(_todate, "1d");            
        amount = 0;
    }
    
}

/*
while(true) {
    def d = getMaxDays(dateCursor);
    def _todate = DateUtil.add( dateCursor, (d-1)+"d" );
    if(_todate.after(todate)) {
        _todate = todate;
        d = (DateUtil.diff( dateCursor, _todate )+1);
        list << createItem( dateCursor, _todate, d,  calcAmount( freq, rate, d ), calcAmount( freq, extrate, d ), freq );
        break;
    }
    else {
        list << createItem( dateCursor, _todate, d,  calcAmount( freq, rate, d ), calcAmount( freq, extrate, d ), freq );
        dateCursor = DateUtil.add(_todate, "1d");
        if( dateCursor.after( todate )) {
            break; 
        }    
    }    
}
*/

list.each {
    println it;
}



