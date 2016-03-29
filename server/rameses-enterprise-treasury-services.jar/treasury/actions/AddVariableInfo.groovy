package treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.osiris3.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;

public class AddVariableInfo implements RuleActionHandler {

	public void execute(def params, def drools) {
		def var = params.attribute;	//key value
		def defaultvalue = params.defaultvalue;

		def ct = RuleExecutionContext.getCurrentContext();
		
		//check first if var objid exists in the facts. if not exist add it.
		def varFact = ct.facts.find{ (it instanceof treasury.facts.VariableInfo) && it.objid == var.key };
		if(!varFact) {

			//check first if variable exists in database
			def em = EntityManagerUtil.lookup("variableinfo");
			def z = em.find( [objid: var.key] ).first();
			if(!z) throw new Exception("Variable id " + var.key + " does not exist!");

			//add facts to be processed in next pass
			VariableInfo vf = new VariableInfo( z.objid, z.name,  z.datatype, defaultvalue );
			ct.facts << vf;

			//add infos in result.
			if( !ct.result.infos ) ct.result.infos = [];
			ct.result.infos << vf.toMap();

		}

	}

}