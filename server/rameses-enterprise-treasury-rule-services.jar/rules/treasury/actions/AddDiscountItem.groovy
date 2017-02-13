package rules.treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import rules.treasury.facts.*;
import com.rameses.osiris3.common.*;

/***
* Parameters:
*    billitem
*    account 
*    amount 	
****/
class AddDiscountItem extends AddBillSubItem {

	public def createSubItemFact( def billitem, def amt, def txntype ) {
		return new DiscountItem(parent: billitem, amount: (amt * -1));
	}

}