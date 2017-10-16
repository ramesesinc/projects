package market.actions;

import com.rameses.rules.common.*;
import market.facts.*;
import com.rameses.util.*;
import java.util.*;
import com.rameses.osiris3.common.*;
import treasury.facts.*;
import treasury.utils.*;
import treasury.actions.*;

public class AddMarketRentalRate implements RuleActionHandler  {

	public void execute(def params, def drools) {

		int year = params.year;
		def rate = params.rate.decimalValue;
		def type = params.type;

		def ct = RuleExecutionContext.getCurrentContext();
		def facts = ct.facts;

		def entries = facts.findAll{ (it instanceof MarketRentalRate)  };
		if( !entries.find{ it.year == year} ) {
			def mr = new market.facts.MarketRentalRate( year: year, rate: rate, type: type );
			facts << mr;
		}


	}
}