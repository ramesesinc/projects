package market.facts;

import java.util.*;
import com.rameses.util.*;
import com.rameses.functions.*;
import treasury.facts.*;

class MarketRentalBillItem extends MonthBillItem {

	double rate;
	String ratetype;
	String txntype = 'MARKETRENTAL';

	public def toMap() {
		def m = super.toMap();

		//there is a possibility that txntype might be MARKETRENTAL-BAL.
		if( txntype == 'MARKETRENTAL') {
			m.rate = rate;
			m.ratetype = ratetype;
			m.numdays = numdays;
		}
		m.txntype = txntype;
		if( m.surcharge == null ) m.surcharge = 0;
		if(m.interest ==null) m.interest = 0;
		if(m.discount==null) m.discount = 0;
		return m;
	}	

	double applyPayment( double payamt ) {
		double pmt = super.applyPayment( payamt );
		if(pmt == 0 ) {
			if(ratetype == "DAY" && txntype == 'MARKETRENTAL') {
				//for partial payments use the ff. formula.
				int dd = ((int) (amount / rate )) +  ((amount % rate > 0 )?1 : 0);
				todate =  DateFunc.getDayAdd(fromdate, dd - 1);
				principal = dd * rate;
			}
		}
		return pmt;
	}

}