package market.actions;

import com.rameses.rules.common.*;
import market.facts.*;
import com.rameses.util.*;
import java.util.*;
import com.rameses.osiris3.common.*;
import treasury.facts.*;
import treasury.actions.*;


public class AddMarketExtensionBillItem extends AddBillSubItem {

	public def createSubItemFact( def billitem, def amt, def txntype ) {
		return new MarketExtensionBillItem(parent: billitem, amount:amt);
	}

}