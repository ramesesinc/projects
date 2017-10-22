package market.actions;

import com.rameses.rules.common.*;
import market.facts.*;
import com.rameses.util.*;
import java.util.*;
import com.rameses.osiris3.common.*;
import treasury.facts.*;
import treasury.utils.*;
import treasury.actions.*;

public class AddMarketRentalBillItem extends AddMonthBillItem {

	public MonthBillItem createMonthBillItem(def params) {
		if(!params.rentalrate ) {
			throw new Exception("AddMarketRentalBillItem error. Please specify rental rate");
		}
		def rr = params.rentalrate;
		def mr = new MarketRentalBillItem();		
		mr .rate = rr.rate;
		mr.ratetype = rr.type;
		return mr;
	}

}