package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;

public class SetBillItemDueDate implements RuleActionHandler {

	def res;	//resources

	public void execute(def params, def drools) {
		def billitem = params.billitem;
		if( billitem == null ) 
			throw new Exception("SetBillItemDueDate error. Please specify a billitem");

		//update only if duedate not yet set.	
		if( billitem.duedate == null ) {
			def duedate = params.duedate.getStringValue();
			def dd = new DueDate( duedate );
			billitem.duedate = dd.date;	
		}
		
	}
}