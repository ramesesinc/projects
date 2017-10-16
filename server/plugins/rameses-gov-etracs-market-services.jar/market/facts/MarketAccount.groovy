package market.facts;

import java.util.*;
import com.rameses.util.*;
import com.rameses.functions.*;

class MarketAccount {

	String acctno;
	String objid;
	String payfrequency;	//MARKET_PAY_FREQUENCY
	double partialbalance;
	double extrate;
	int startyear;

	Date startdate;
	Date fromdate;

	public MarketAccount(def mm) {
		objid = mm.objid;
		acctno = mm.acctno;
		payfrequency = mm.payfrequency;
		partialbalance = mm.partialbalance;
		if(mm.extrate) extrate = mm.extrate;

		if( mm.lastdatecovered) {
			startdate = DateFunc.getDayAdd(mm.lastdatecovered,1);
		}
		else {
			startdate = mm.startdate;
		}

		//lastyearpaid
		//lastmonthpaid
		//lastdatecovered

		//if(!lastdatecovered) return startdate;
		//return DateFunc.getDayAdd(lastdatecovered,1);		
	}

}