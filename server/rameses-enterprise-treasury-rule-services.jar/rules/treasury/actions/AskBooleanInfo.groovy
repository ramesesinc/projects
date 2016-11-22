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
class AskBooleanInfo extends AbstractAskInfo {

	public def createInfo( def infotype, def value ) {
		if(value==null) 
			value = false;
		else
			value = (value + "").toBoolean();	

		return new BooleanInfo( type:infotype, value: value)
	}

}