package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;

public class SetDueDate implements RuleActionHandler {

	public void execute(def params, def drools) {
		def billdate = params.duedate.getStringValue();
		drools.insert( new DueDate(billdate) );
	}
	
}