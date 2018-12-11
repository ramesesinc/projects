package ovs.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;
import waterworks.facts.*;

public class ComputeViolationFee implements RuleActionHandler {

	public void execute(def params, def drools) {
		if( !params.amount )
			throw new Exception( "ComputeViolationFee  error. amount is required ");

		if( !params.ref )
			throw new Exception( "ComputeViolationFee  error. ref is required ");

		def amt = params.amount.decimalValue;
		def ref = params.ref;
		ref.amount = amt;
		
	}
}