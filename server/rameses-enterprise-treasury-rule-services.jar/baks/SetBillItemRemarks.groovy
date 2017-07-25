package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;


/***
* Description: Simple Add of Item. Item is unique based on the account. 
* Parameters:
*    billitem
****/
class SetBillItemRemarks  implements RuleActionHandler  {

	public void execute(def params, def drools) {
		def billitem = params.billitem;
		if( params.remarks ) {
			billitem.remarks = params.remarks.eval();		
		}
	}

}