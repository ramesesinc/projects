package waterworks.actions;

import com.rameses.rules.common.*;
import com.rameses.util.*;
import java.util.*;
import treasury.facts.*;
import com.rameses.osiris3.common.*;
import waterworks.facts.*;

public class AddWaterConsumption implements RuleActionHandler {

	public void execute(def params, def drools) {
		if( !params.volume )
			throw new Exception( "AddWaterConsumption error. volume is required ");

		def vol = params.volume.intValue;
		def ct = RuleExecutionContext.getCurrentContext();

		if( !ct.facts.find{ it instanceof WaterConsumption }) {
			ct.facts << new WaterConsumption( volume: vol )	
		}
	}
}