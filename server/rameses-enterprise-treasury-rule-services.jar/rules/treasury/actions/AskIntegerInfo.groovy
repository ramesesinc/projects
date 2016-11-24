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
class AskIntegerInfo extends AbstractAskInfo {

	public def createInfo( def infotype, def value ) {
		if( value == null )
			value = 0;
		else
			value = (value+"").toInteger();	
		return new IntegerInfo( type:infotype, value: value)
	}

}