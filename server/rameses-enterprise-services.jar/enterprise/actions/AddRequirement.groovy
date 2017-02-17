package enterprise.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import enterprise.facts.*;
import com.rameses.osiris3.common.*;

/***
* Description: Simple Add of Item. Item is unique based on the account. 
* Parameters:
*    account 
*    amount
****/
class AddRequirement implements RuleActionHandler {

	public void execute(def params, def drools) {
		def type = params.type;

		def ct = RuleExecutionContext.getCurrentContext();

		if(!ct.result.requirements) {
			ct.result.requirements = new LinkedHashSet<Requirement>();
		}
		def req = new Requirement( new RequirementType(objid: type.key, title: type.value) );
		boolean b = ct.result.requirements.add(req);
		if(b) {
			ct.facts << req;	
		}
	}

}