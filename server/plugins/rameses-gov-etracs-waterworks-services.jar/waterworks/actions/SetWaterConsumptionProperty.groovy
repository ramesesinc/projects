package waterworks.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;

class SetWaterConsumptionProperty  implements RuleActionHandler  {

	public void execute(def params, def drools) {
		def obj = params.item;
		def propname = params.fieldname;
		def value = params.value.eval();

		obj[(propname)] = value;
	}

}