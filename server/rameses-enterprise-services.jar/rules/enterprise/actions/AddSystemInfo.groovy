package rules.enterprise.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import rules.enterprise.facts.*;
import com.rameses.osiris3.common.*;

/***
* Parameters:
*  infotype
*  datatype
*  value  
****/
class AddSystemInfo implements RuleActionHandler {

	public void execute(def params, def drools) {
		if(!params.name) throw new Exception("name is required in any AskSystemInfo action");
		if(!params.value) throw new Exception("value is required in any AskSystemInfo action");

		def infotype = params.name;
		
		String infoName = params.name.key;
		def value =  params.value.eval();

		def ct = RuleExecutionContext.getCurrentContext();
		
		/********************************************************************************************
		* check first if input already exists in infos or askinfos do not add if its exists already.
  		*********************************************************************************************/
		boolean include = true;
		if( ct.facts.find{ (it instanceof VariableInfo) && it.name == infoName  }==null ) {
			if(! ct.env.infoUtil ) {
				ct.env.infoUtil = new VariableInfoUtil();
			}	
			def vinfo = ct.env.infoUtil.createFact([name: infoName, value: value]);
			ct.facts << vinfo;
		}
	}

}