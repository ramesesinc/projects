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
		m.rate = rate;
		m.ratetype = ratetype;
		m.txntype = txntype;
		m.numdays = numdays;
		if( m.surcharge == null ) m.surcharge = 0;
		if(m.interest ==null) m.interest = 0;
		if(m.discount==null) m.discount = 0;
		return m;
	}	

}