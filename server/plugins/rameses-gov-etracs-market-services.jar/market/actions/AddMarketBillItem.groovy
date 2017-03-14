package market.actions;

import com.rameses.rules.common.*;
import market.facts.*;
import com.rameses.util.*;
import java.util.*;
import com.rameses.osiris3.common.*;
import treasury.facts.*;
import treasury.utils.*;
import treasury.actions.*;

public class AddMarketBillItem extends AbstractAddBillItem {

	public void execute(def params, def drools) {

		def rentalunit = params.marketunit;
		def entry = params.monthentry;
		def acct = params.account;
		def amt = params.amount.doubleValue;

		def remarks = null;
		if( params.remarks ) {
			remarks = params.remarks.eval();		
		}

		def billitem = new MarketBillItem();
		billitem.year = entry.year;
		billitem.month = entry.month;
		billitem.days = entry.days;
		billitem.fromdate = entry.fromdate;
		billitem.todate = entry.todate;
		billitem.amount = amt;
		billitem.monthentry = entry;
		billitem.marketunit = rentalunit;
		billitem.remarks = remarks;
		setAccountFact( billitem, acct.key );

		addToFacts( billitem );
	}
}