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
class AddSurchargeItem extends AddBillSubItem {

	public def createSubItemFact( def billitem, def amt, def txntype ) {
		return new SurchargeItem(parent: billitem, amount:amt);
	}


}