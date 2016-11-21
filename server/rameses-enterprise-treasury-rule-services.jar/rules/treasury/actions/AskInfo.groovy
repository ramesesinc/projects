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
class AskInfo implements RuleActionHandler {

	public void execute(def params, def drools) {
		def infotype = params.infotype;
		def value = null;
		if(params.defaultvalue) {
			value = params.defaultvalue;
		}

		Info info = new Info(value: value, new InfoType( objid: infotype.key, title: infotype.value ));
		def ct = RuleExecutionContext.getCurrentContext();

		boolean include = true;
		//check first if input already exists in infos, do not add if its exists already.
		if( ct.result.askinfos ) {
			if( ct.result.askinfos.find{ it == info } ) include = false;
		}	

		if(include) {
			if( !ct.result.askinfos ) ct.result.askinfos = new LinkedHashSet<Info>();
			ct.result.askinfos.add( info );
		}
	}

}