package rules.treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import rules.treasury.facts.*;
import com.rameses.osiris3.common.*;

/***
* Parameters:
*    billitem
*    amount 
****/
class AddInterestItem extends AddBillSubItem {

	public def createSubItemFact( def billitem, def amt, def txntype ) {
		return new InterestItem(parent: billitem, amount:amt);
	}

}