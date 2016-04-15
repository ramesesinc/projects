package common.actions;

import com.rameses.rules.common.*;
import com.rameses.osiris3.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;

public class AddRequirement implements RuleActionHandler {

	public void execute(def params, def drools) {
		if(!params.reqtype) throw new Exception("reqtype required in AddRequirement rules");
		if(!params.message) throw new Exception("message required in AddRequirement rules");
		if(!params.sortorder)throw new Exception("sortorder required in AddRequirement rules");
		
		def var = params.reqtype;	//key value
		def msg = params.message.getStringValue();
		int sortorder = params.sortorder.toInteger();
		boolean required = params.required;

		def ct = RuleExecutionContext.getCurrentContext();
		
		//check first if var objid exists in the facts. if not exist add it.
		def varFact = ct.facts.find{ (it instanceof treasury.facts.Requirement) && it.code == var.key };
		if(!varFact) {

			//check first if variable exists in database
			def em = EntityManagerUtil.lookup("requirement_type");
			def z = em.find( [code: var.key] ).first();
			if(!z) throw new Exception("Requirement code " + var.key + " does not exist!");

			//add facts to be processed in next pass
			int irequired = (required)?1:0;
			Requirement rq = new Requirement( code:z.code, title:z.title, message:msg, sortorder: sortorder, required: irequired);
			ct.facts << rq;

			//add infos in result.
			if( !ct.result.requirements ) ct.result.requirements = [];
			ct.result.requirements << rq.toMap();
		}

	}

}