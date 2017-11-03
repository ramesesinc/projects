package market.facts;

import java.util.*;
import com.rameses.util.*;
import com.rameses.functions.*;

class MarketAccount {

	String objid;
	String acctno;
	String payfrequency;	//MARKET_PAY_FREQUENCY
	double partialbalance;
	double extrate;
	double extarea;

	String ratetype;
	Date lastdatepaid;
	Date startdate;

	public MarketAccount(def mm) {
		objid = mm.objid;
		acctno = mm.acctno;
		payfrequency = mm.payfrequency;
		partialbalance = mm.partialbalance;
		lastdatepaid = mm.lastdatepaid;
		if(mm.extrate) extrate = mm.extrate;
		if(mm.dtstarted) startdate = mm.dtstarted;
		if(mm.extarea) extarea = mm.extarea;
	}

}