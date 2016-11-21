package rules.treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import rules.treasury.facts.*;
import com.rameses.osiris3.common.*;

/***
* Parameters:
*    billitem
*    type ( SUM, MAX, MIN )
****/
class SummarizeBillItem implements RuleActionHandler {

	public void execute(def params, def drools) {
		def billitem = params.billitem;
		if(!billitem)
			throw new Exception("rules.treasury.actions.SummarizeBillItem");
		def ct = RuleExecutionContext.getCurrentContext();

		def test = new BillItem(account: billitem.account);
		def newBillItem = ctx.result.billitems.find{ it == billitem };
		if(newBillItem) {
			newBillItem.amount += billitem.amount;
		}
		else {
			newBillItem.amount = billitem.amount;
		}
	}

}