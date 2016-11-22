package rules.treasury.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import rules.treasury.facts.*;
import com.rameses.osiris3.common.*;

/***
* Parameters:
*  infotype
*  defaultvalue  - 
****/
public abstract class AbstractAskInfo implements RuleActionHandler {

	public abstract def createInfo( def infotype, def value );

	public void execute(def params, def drools) {
		def infotype = params.type;
		if(!infotype) throw new Exception("type is required in any AskInfo action");

		def value = null;
		if(params.value) {
			if(params.value instanceof ActionExpression) 
				value = params.value.eval();
			else
				value = params.value;
		}

		def info = createInfo(new VariableInfoType( objid: infotype.key, name: infotype.value ), value );
		def ct = RuleExecutionContext.getCurrentContext();

		boolean include = true;
		//check first if input already exists in infos or askinfos do not add if its exists already.
		if( ct.facts.find{ (it instanceof VariableInfo) && it == info  }!=null ) {
			include = false;
		}
		else if(ct.result.infos) {
			if( ct.result.infos.find{ it == info } ) include = false;
		}
		else if( ct.result.askinfos ) {
			if( ct.result.askinfos.find{ it == info } ) include = false;
		}	

		if(include) {
			if( !ct.result.askinfos ) ct.result.askinfos = new LinkedHashSet<VariableInfo>();
			ct.result.askinfos.add( info );
		}
	}

}