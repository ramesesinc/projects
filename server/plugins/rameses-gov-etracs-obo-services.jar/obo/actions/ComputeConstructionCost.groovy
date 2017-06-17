package obo.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import rules.obo.facts.*;
import com.rameses.osiris3.common.*;


/***
* Description: Simple Add of Item. Item is unique based on the account. 
* Parameters:
*    application (obo.facts.OboApplication)
*    amount
****/
public class ComputeConstructionCost implements RuleActionHandler {

	public void execute(def params, def drools) {
		def ba = params.application;
		if( !ba.constructioncost || ba.constructioncost <= 0 ) {
			def amt = params.amount.decimalValue;			
			ba.constructioncost = amt;
		}
	}

}