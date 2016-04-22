package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;
import com.rameses.functions.*;

/***
* This calculates the installment based on the currentdate and the term
* It calculates how much to bill based on the date requested. 
* If amtpaid is greater than the bill amount, no bill will be generated.
* Used for installment payments. Also applicable for compromise payments 
* Parameters :
*     Installment Fact
*     currentdate: 
*/
public class ComputeInstallmentBill implements RuleActionHandler {

	public void execute(def params, def drools) {

		def ct = RuleExecutionContext.getCurrentContext();
		def installment = params.installmentitem;
		def currentDate = params.currentdate;

		def startDate = installment.startdate;
		int monthDiff = DateFunc.monthsDiff( startDate, currentDate );
		if( monthDiff > 0 ) {
			def amtdue = 0;
			if( installment.term >= monthDiff ) {
				amtdue = installment.amount;
			}
			else {
				amtdue = NumberUtil.round( installment.amount / installment.term );
			}
			amtdue = amtdue - amtpaid;
			if( amtdue > 0 ) {
				//give this a very low payment order so it will be prioritized in the billing:
				def bi = new BillItem();

				bi.account = installment.account;
				bi.amtdue = amtdue;
				bi.amount = amtdue;

				drools.insert( bi );
				ct.facts << bi;
			} 
		}

	}
}