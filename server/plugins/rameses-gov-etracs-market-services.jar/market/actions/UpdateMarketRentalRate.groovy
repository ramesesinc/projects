package market.actions;

import com.rameses.rules.common.*;
import market.facts.*;
import com.rameses.util.*;
import java.util.*;
import com.rameses.osiris3.common.*;
import treasury.facts.*;
import treasury.utils.*;
import treasury.actions.*;

public class UpdateMarketRentalRate implements RuleActionHandler  {

	public void execute(def params, def drools) {

		def rentalrate = params.rentalrate;
		def rate = params.rate.decimalValue;

		if(!rentalrate.updated) {
			rentalrate.rate = rate;
			rentalrate.updated = true;
		}

	}
}