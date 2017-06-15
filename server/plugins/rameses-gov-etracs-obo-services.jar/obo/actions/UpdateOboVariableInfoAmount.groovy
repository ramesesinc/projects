package obo.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import rules.obo.facts.*;
import com.rameses.osiris3.common.*;

public class UpdateOboVariableInfoAmount implements RuleActionHandler {

	public void execute(def params, def drools) {
		def ba = params.info;
		def amt = params.amount.decimalValue;			
		ba.amount = amt;
	}

}